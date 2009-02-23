package ui.components.logicgates;

import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author Matt
 */
public class AndGate2Input extends VisualComponent{    
    
    public AndGate2Input(ui.CircuitPanel parent, Point point, sim.SimItem simItem, netlist.properties.Properties properties) {
        super(parent, point, simItem,properties);
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x+18,(int)getOrigin().getY()+18-getCentre().y,34,24);
        invalidArea = rotate(invalidArea);        
    }
    
    @Override
    public Point getCentre(){
        return new Point(30,30);
    }

    @Override
    protected void setKeyName() {
        keyName = "Logic Gates.2 Input.AND";
    }
    
}
