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
 * @author matt
 */
public class AndGate3Input extends ImageSelectableComponent{
    
    public AndGate3Input(ui.CircuitPanel parent, Point point) {
        super(parent, point);
    }
    
    @Override
    public String getName(){
        //return getLogicalComponent().getType();
        return "And Gate (3 Input)";
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
    
    @Override
    public void setLocalPins() {
        localPins.clear();
        
        Pin in1 = new Pin(10, 20);
        Pin in2 = new Pin(10, 30);
        Pin in3 = new Pin(10, 40);
        Pin out1 = new Pin(60, 30);
                
        localPins.add(in1);
        localPins.add(in2);
        localPins.add(in3);
        localPins.add(out1);
        
    }
    
    @Override
    protected void setNetlist() {
        nl = new netlist.LogicGates();
    }
    
    @Override
    protected void setComponentTreeName() {
        componentTreeName = "Logic Gates.3 Input.AND";
    }
    
}
