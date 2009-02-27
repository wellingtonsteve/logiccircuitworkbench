package ui.components.flipflops;

import java.awt.Point;
import java.awt.Rectangle;
import ui.components.VisualComponent;

/**
 *
 * @author Matt
 */
public class DefaultFlipFlop extends VisualComponent{    
    
    public DefaultFlipFlop(ui.CircuitPanel parent, Point point, sim.SimItem simItem, netlist.properties.Properties properties) {
        super(parent, point, simItem,properties);
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x+13,(int)getOrigin().getY()+8-getCentre().y,34,54);
        invalidArea = rotate(invalidArea);        
    }
    
    @Override
    public Point getCentre(){
        return new Point(30,30);
    }    
}