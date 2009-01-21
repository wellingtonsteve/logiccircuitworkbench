package ui.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import ui.UIConstants;
import ui.tools.SelectableComponent;
import netlist.standard.Wire;

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
            defaultCrossover = ImageIO.read(getClass().getResource("/ui/images/components/default_wire_crossover.png"));
        } catch (IOException ex) {
            defaultCrossover = new BufferedImage(0,0,BufferedImage.TYPE_INT_RGB);
            ui.error.ErrorHandler.newError(new ui.error.Error("Initialisation Error", "Could not load \"Connection Point Crossover\" image.", ex));    
        }
    }
    
    public void addConnection(Pin p){      
        if(p.getParent() instanceof Wire                                // The current pin belongs to a wire
                && hasDifferentWire(p.getParent())                      // Not the same wire               
                && !((Wire) p.getParent()).getEndPoint().equals(p)      // Not the end point of a wire
                && !((Wire) p.getParent()).getOrigin().equals(p)){      // Not the start point of a wire
            isCrossover = true;
        } 
        connections.add(p);

    }
    
    public boolean removeConnection(Pin p){
        boolean retval = connections.remove(p);
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
    
    public void moveWireEnds(Point newPoint){        
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
    
    public boolean hasDifferentWire(SelectableComponent wire){
        for(Pin p: connections){
            if(p.getParent() instanceof Wire
                    && !p.getParent().equals(wire)){
                return true;
            }
        }
        return false;
    }
    
    public boolean hasSameComponent(SelectableComponent sc){
        for(Pin p: connections){
            if(p.getParent().equals(sc)){
                return true;
            }
        }
        return false;
    }
    
    public boolean isWire(){       
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

            if(UIConstants.SHOW_CONNECTION_POINTS){ 
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
    
    @Override
    public boolean equals(Object obj){
        return super.equals(obj);        
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private int noOfDifferentConnections() {
        LinkedList<SelectableComponent> found = new LinkedList<SelectableComponent>();
        for(Pin p: connections){
            if(!found.contains(p.getParent())){
                found.add(p.getParent());
            }
        }        
        return found.size();
    }
    
    public Enumeration<SelectableComponent> getConnectedComponents(){
        return new Enumeration<SelectableComponent>(){
            int i = 0;
                
            public boolean hasMoreElements() {
                return i<noOfConnections();
            }

            public SelectableComponent nextElement() {
                return connections.get(i++).getParent();
            }
            
        };
    }
        
}
