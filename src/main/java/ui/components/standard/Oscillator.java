package ui.components.standard;

import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;
import netlist.properties.Properties;

/**
 *
 * @author Matt
 */
public class Oscillator extends VisualComponent{
    public Oscillator(ui.CircuitPanel parent, Point point, sim.SimItem simItem, Properties properties) {
        super(parent, point, simItem,properties);
    }
   
    @Override
    public String getName(){
        return "Oscillator";
    }
    
    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()-1,(int)getOrigin().getY()-1,22,22);
        invalidArea = rotate(invalidArea);   
    }
    
    @Override
    public Point getCentre(){
        return new Point(10,10);
    }

}
