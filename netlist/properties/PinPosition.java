package netlist.properties;

import ui.components.ComponentEdge;

/**
 *
 * @author matt
 */
public class PinPosition {
    private ComponentEdge edge;
    private int place;
    
    public PinPosition(ComponentEdge edge, int n){
        this.edge = edge;
        this.place = n;
    }
    
    public ComponentEdge getEdge() {
        return edge;
    }

    public int getPlace() {
        return place;
    }
    
}
