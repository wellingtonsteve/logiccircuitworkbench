package ui.components.standard;

import sim.LogicState;
import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;
import netlist.properties.Properties;
import ui.UIConstants;

/**
 *
 * @author Matt
 */
public class Buzzer extends VisualComponent implements sim.joinable.ValueListener{
    public Buzzer(ui.CircuitPanel parent, Point point, sim.SimItem simItem, Properties properties) {
        super(parent, point, simItem,properties);
    }
        
    @Override
    public String getName(){
        return "Buzzer";
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()+16,(int)getOrigin().getY()-1,22,22);
        invalidArea = rotate(invalidArea);   
    }
    
    @Override
    public Point getCentre(){
        return new Point(30,10);
    }

    @Override
    public void addListeners() {
        logicalComponent.getPinByName("Input").addValueListener(this);
    }    
 
    public void valueChanged(sim.joinable.Pin pin, LogicState value) {
        if(value.equals(sim.LogicState.ON)){
            UIConstants.beep();
        }
    }
}
