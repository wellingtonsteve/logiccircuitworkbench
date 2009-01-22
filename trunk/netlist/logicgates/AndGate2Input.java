package netlist.logicgates;

import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author Matt
 */
public class AndGate2Input extends ImageSelectableComponent{    
    
    public AndGate2Input(ui.CircuitPanel parent, Point point) {
        super(parent, point);
    }
    
    @Override
    public String getName(){
        return "And Gate (2 Input)";
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x+18,(int)getOrigin().getY()+18-getCentre().y,34,24);
        invalidArea = rotate(invalidArea);        
    }
    
    @Override
    protected void setBoundingBox(){
        boundingBox = new Rectangle((int)getOrigin().getX()-getCentre().x+15,(int)getOrigin().getY()+15-getCentre().y,40, 30);
        boundingBox = rotate(boundingBox);        
    }
    
    @Override
    public Point getCentre(){
        return new Point(30,30);
    }

    @Override
    public void setLocalPins() {
        Point in1 = new Point(10, 20);
        Point in2 = new Point(10, 40);
        Point out1 = new Point(60, 30);
                
        localPins.add(in1);
        localPins.add(in2);
        localPins.add(out1);        
    }

    @Override
    protected void setNetlist() {
        nl = new netlist.logicgates.LogicGates();
    }

    @Override
    protected void setComponentTreeName() {
        componentTreeName = "Logic Gates.2 Input.AND";
    }
    
}
