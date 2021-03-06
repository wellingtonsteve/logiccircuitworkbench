package ui.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.Collection;
import java.util.LinkedList;
import sim.joinable.Joinable;
import ui.UIConstants;
import ui.components.SelectableComponent;
import ui.components.SelectableComponent.Pin;
import ui.components.Wire;

/** @author Matt */
public class ConnectionPoint extends GridObject {
    private LinkedList<Pin> connections = new LinkedList<Pin>();
    private boolean isActive = false;
    private boolean isCrossover = false;
    private String label = new String();

    public ConnectionPoint(Grid grid, Point p){
        super(grid, p);        
    }
    
    /** Connect a pin to this connection point */
    public void addConnection(Pin p){         
        if(!hasConnection(p)){          
            p.setConnectionPoint(this);
            connections.add(p);            
            setIsCrossover();
            if(isConnected() && p.getParent().isFixed() && !isCrossover){
                for(Pin pin: connections){
                    if(!pin.equals(p)){
                        sim.joinable.Joinable.connect(pin.getJoinable(), p.getJoinable());
                    }
                }
            }
        }        
    }

    /** Would connecting this wire be valid in the simulator? */
    public boolean canConnect(sim.joinable.Wire logicalWire) {
        for(Pin pin: connections){
            if(!Joinable.canConnect(pin.getJoinable(), logicalWire)){
                return false;
            }
        } return true;
    }
    
    /** Remove a pin from the connection point */
    public boolean removeConnection(Pin p){
        boolean retval = connections.remove(p);
        if(retval && isConnected() && p.getParent().isFixed()){
            for(Pin pin: connections){
                sim.joinable.Joinable.disconnect(pin.getJoinable(), p.getJoinable());
            }
        }                
        if(retval){p.setConnectionPoint(null);}
        if(noOfConnections() == 1)  {
            isCrossover = false;
        }
        return retval;
    }           
     
    public boolean hasConnection(Pin p){
        return connections.contains(p);
    }
    
    public int noOfConnections(){
        return connections.size();
    }
    

    public Collection<Pin> getConnections(){
        return (LinkedList<Pin>) connections.clone();
    }
    
    public boolean isConnected(){
        return !connections.isEmpty();
    }
    
    /** How many different components are connected here? */
    protected int noOfDifferentConnections() {
        LinkedList<SelectableComponent> found = new LinkedList<SelectableComponent>();
        for(Pin p: connections){
            if(!found.contains(p.getParent())){
                found.add(p.getParent());
            }
        } return found.size();
    }
    
    @Override
    /** Is the component a parent of a pin which is connected here? */
    public boolean hasParent(SelectableComponent sc){
        for(Pin p: connections){
            if(p.getParent().equals(sc)){
                return true;
            }
        } return false;
    }
        
    /** Activate this connection point */
    public void setActive(boolean isActive){
        if(!isCrossover){
            this.isActive = isActive;
        }
    }
    
    public boolean isActive(){
        return isActive;
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);               
        if(!isCrossover && noOfDifferentConnections() > 1){
             g2.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);
             g2.drawOval(x-2, y-2, 5, 5);
             g2.fillOval(x-2, y-2, 5, 5);
        }
        if(isActive){
            Stroke def = g2.getStroke();
            g2.setStroke(UIConstants.CONNECTED_POINT_STROKE);
            g2.setColor(UIConstants.CONNECTION_POINT_COLOUR);
            g2.drawRect(x-3, y-3, 7, 7); 
            g2.setStroke(def);
            isActive = false;
        }
        if(UIConstants.SHOW_GRID_OBJECTS){ 
            g2.setColor(UIConstants.CONNECTION_POINT_COLOUR);
            g2.drawOval(x-1, y-1, 3, 3);
            g2.fillOval(x-1, y-1, 3, 3);
        }            
    }
 
    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }
    
    @Override
    public boolean hasLabel(){
        return label != null || !label.equals("");
    }

    /** Determine whether two wire cross here but are not joined */
    private void setIsCrossover() {
       int noOfWireNonTerminus = 0;
       for(Pin p: connections){
           if(p.getParent() instanceof Wire // The current pin belongs to a wire 
           // Not the end point of a wire
          && !((Wire) p.getParent()).getEndPoint().equals(p.getGlobalLocation()) 
           // Not the start point of a wire
          && !((Wire) p.getParent()).getOrigin().equals(p.getGlobalLocation()) 
               ){ 
               noOfWireNonTerminus++;
           }
       }
       isCrossover = (noOfWireNonTerminus>1);
   }
}