/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.components.logicgates;

import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author Matt
 */
public class OrGate2Input extends VisualComponent{

    public OrGate2Input(ui.CircuitPanel parent, Point point, sim.SimItem simItem, netlist.properties.Properties properties) {
        super(parent, point, simItem,properties);
    }
        
    @Override
    protected void setKeyName() {
        keyName = "Logic Gates.2 Input.OR";
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x+19
                ,(int)getOrigin().getY()+19-getCentre().y ,32 ,22);
        invalidArea = rotate(invalidArea);   
    }
    
    @Override
    public Point getCentre(){
        return new Point(30,30);
    }
}
