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
public class Buzzer extends VisualComponent implements sim.joinable.ValueListener{
    public Buzzer(ui.CircuitPanel parent, Point point, sim.SimItem simItem) {
        super(parent, point, simItem);
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

    @Override
    public void addPinListeners() {
        logicalComponent.getPinByName("Input").addValueListener(this);
    }    
 
    public void valueChanged(sim.joinable.Pin pin, LogicState value) {
        if(value.equals(sim.LogicState.ON)){
            UIConstants.beep();
        }
    }
}
