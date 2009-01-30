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
public class NandGate2Input extends ImageSelectableComponent{

    public NandGate2Input(ui.CircuitPanel parent, Point point, sim.SimItem simItem) {
        super(parent, point, simItem);
    }
    
    @Override
    protected void setNetlist() {
        nl = new netlist.LogicGates();
    }
    
    @Override
    protected void setComponentTreeName() {
        componentTreeName = "Logic Gates.2 Input.NAND";
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
        Pin in2 = new Pin(10, 40);
        Pin out1 = new Pin(60, 30);
                
        localPins.add(in1);
        localPins.add(in2);
        localPins.add(out1);
        
    }

}
