package netlist.properties;

import ui.components.ComponentEdge;

/**
 *
 * @author matt
 */
public class PinPosition {
    private ComponentEdge edge;
    private int n;
    
    public PinPosition(ComponentEdge edge, int n){
        this.edge = edge;
        this.n = n;
    }
    
    public ComponentEdge getEdge() {
        return edge;
    }

    public int getN() {
        return n;
    }
    
}
