package ui.components.flipflops;

import java.awt.Point;
import java.awt.Rectangle;
import ui.components.VisualComponent;

/**
 *
 * @author Matt
 */
public class DefaultLatch extends VisualComponent{    
    
    public DefaultLatch(ui.CircuitPanel parent, Point point, sim.SimItem simItem, netlist.properties.Properties properties) {
        super(parent, point, simItem,properties);
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x+23,(int)getOrigin().getY()-getCentre().y+3,34,54);
        invalidArea = rotate(invalidArea);        
    }
    
    @Override
    public Point getCentre(){
        return new Point(30,30);
    }    
}
