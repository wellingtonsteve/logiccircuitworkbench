package ui.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import ui.UIConstants;
import ui.components.SelectableComponent;
import ui.components.SelectableComponent.Pin;
import ui.components.Wire;

/**
 *
 * @author Matt
 */
public class ConnectionPoint extends GridObject {
    private LinkedList<Pin> connections = new LinkedList<Pin>();
    private boolean isActive = false;
    private boolean isCrossover = false;
    private static BufferedImage defaultCrossover;
    private String label = new String();

    public ConnectionPoint(Point p){
        super(p);
        
        try {
            defaultCrossover = ImageIO.read(
                    getClass().getResource("/ui/images/components/default_wire_crossover.png"));
        } catch (IOException ex) {
            defaultCrossover = new BufferedImage(0,0,BufferedImage.TYPE_INT_RGB);
            ui.error.ErrorHandler.newError("Initialisation Error",
                    "Could not load \"Connection Point Crossover\" image.", ex);    
        }
    }
    
    public void addConnection(Pin p){      
        if(p.getParent() instanceof Wire // The current pin belongs to a wire
                && hasDifferentWire(p.getParent()) // Not the same wire               
                && !((Wire) p.getParent()).getEndPoint().equals(p.getGlobalLocation()) // Not the end point of a wire
                && !((Wire) p.getParent()).getOrigin().equals(p.getGlobalLocation())){ // Not the start point of a wire
            isCrossover = true;
        } 
        if(!hasConnection(p)){
            p.setConnectionPoint(this);
            connections.add(p);
        }
    }
    
    public boolean removeConnection(Pin p){
        boolean retval = connections.remove(p);
        p.setConnectionPoint(null);
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
    
    @SuppressWarnings("unchecked")
    public Collection<Pin> getConnections(){
        return (Collection<Pin>) connections.clone();
    }
    
    protected void moveWireEnds(Point newPoint){        
        for(Pin pin: connections){
            if(pin.getParent() instanceof Wire){
                Wire w = (Wire) pin.getParent();
                if(w.isFixed()){
                    if(w.getEndPoint().equals(this)){
                        w.moveEndPoint(newPoint);
                        break;
                    } else if(w.getOrigin().equals(this)){
                        w.moveStartPoint(newPoint);
                        break;
                    }     
                }                               
            } 
        } 
    }
    
    public boolean isConnected(){
        return !connections.isEmpty();
    }
    
    protected int noOfDifferentConnections() {
        LinkedList<SelectableComponent> found = new LinkedList<SelectableComponent>();
        for(Pin p: connections){
            if(!found.contains(p.getParent())){
                found.add(p.getParent());
            }
        }        
        return found.size();
    }
        
    protected boolean hasDifferentWire(SelectableComponent wire){
        for(Pin p: connections){
            if(p.getParent() instanceof Wire
                    && !p.getParent().equals(wire)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean hasParent(SelectableComponent sc){
        for(Pin p: connections){
            if(p.getParent().equals(sc)){
                return true;
            }
        }
        return false;
    }
    
    protected boolean hasAnyWirePins(){       
        for(Pin p: connections){
            if(p.getParent() instanceof Wire){
                return true;
            }
        }
        return false;
    }
    
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
        if(!isCrossover){        
            if(noOfDifferentConnections() > 1){
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
        } else {
            g2.drawImage(defaultCrossover, x-9, y-10, null);
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
}