package ui.components.logicgates;

import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author Matt
 */
public class AndGate2Input extends ImageSelectableComponent{    
    
    public AndGate2Input(ui.CircuitPanel parent, Point point, sim.SimItem simItem) {
        super(parent, point, simItem);
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
    public Point getCentre(){
        return new Point(30,30);
    }

    @Override
    public void setLocalPins() {
        localPins.clear();
        Pin in1 = new Pin(10, 20, logicalComponent.getPinByName("Input 1"));
        Pin in2 = new Pin(10, 40, logicalComponent.getPinByName("Input 2"));
        Pin out1 = new Pin(60, 30, logicalComponent.getPinByName("Output"));
                
        localPins.add(in1);
        localPins.add(in2);
        localPins.add(out1);        
    }

    @Override
    protected void setNetlist() {
        nl = new netlist.LogicGates();
    }

    @Override
    protected void setComponentTreeName() {
        componentTreeName = "Logic Gates.2 Input.AND";
    }
    
}
