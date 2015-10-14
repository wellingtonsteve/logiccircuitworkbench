package ui.components.logicgates;

import java.awt.Point;
import java.awt.Rectangle;
import ui.components.VisualComponent;

/**
 *
 * @author Matt
 */
public class DefaultLogicGate extends VisualComponent{    
    
    public DefaultLogicGate(ui.CircuitPanel parent, Point point, sim.SimItem simItem, netlist.properties.Properties properties) {
        super(parent, point, simItem,properties);
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle(getOrigin().x+18,getOrigin().y+18,34,24);
        invalidArea = rotate(invalidArea);        
    }
    
    @Override
    public Point getCentre(){
        return new Point(30,30);
    }    
}
