package ui.components.standard;

import sim.LogicState;
import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;
import ui.UIConstants;

/**
 *
 * @author Matt
 */
public class Buzzer extends VisualComponent implements sim.pin.ValueListener{
    public Buzzer(ui.CircuitPanel parent, Point point, sim.SimItem simItem) {
        super(parent, point, simItem);
        logicalComponent.getPinByName("Input").addValueListener(this);
    }

    @Override
    protected void setComponentTreeName() {
        keyName = "Standard.Buzzer";
    }
        
    @Override
    public String getName(){
        return "Buzzer";
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x+16,(int)getOrigin().getY()-getCentre().y-1,22,22);
        invalidArea = rotate(invalidArea);   
    }
    
    @Override
    public Point getCentre(){
        return new Point(30,10);
    }

//    @Override
//    public void setLocalPins() {
//        localPins.clear();
//        Pin in1 = new Pin(10, 10, logicalComponent.getPinByName("Input"));             
//        localPins.add(in1);        
//    }
 
    public void valueChanged(sim.pin.Pin pin, LogicState value) {
        if(value.equals(sim.LogicState.ON)){
            UIConstants.beep();
        }
    }
}