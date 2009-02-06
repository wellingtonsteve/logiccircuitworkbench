package ui.components.standard;

import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author Matt
 */
public class Logger extends ImageSelectableComponent{
    private Pin in1;

    public Logger(ui.CircuitPanel parent, Point point, sim.SimItem simItem) {
        super(parent, point, simItem);
        parent.addLogger(simItem.getPinByName("Input"), parent.getSimulator());
    }

    @Override
    protected void setNetlist() {
        nl = new netlist.Standard();
    }
    
    @Override
    protected void setComponentTreeName() {
        componentTreeName = "Standard.Output Logger";
    }
    
    
    @Override
    public String getName(){
        return "Output Logger";
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x+9,(int)getOrigin().getY()-getCentre().y+8,46,43);
        invalidArea = rotate(invalidArea);   
    }
    
    @Override
    public Point getCentre(){
        return new Point(30,30);
    }

    @Override
    public void setLocalPins() {
        localPins.clear(); 
        in1 = new Pin(30, 70, logicalComponent.getPinByName("Input"));
        localPins.add(in1);        
    }
            
}
