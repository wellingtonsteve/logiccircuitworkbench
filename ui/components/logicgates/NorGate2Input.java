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
public class NorGate2Input extends ImageSelectableComponent{

    public NorGate2Input(ui.CircuitPanel parent, Point point) {
        super(parent, point);
    }

    @Override
    protected void setNetlist() {
        nl = new netlist.LogicGates();
    }
    
    @Override
    protected void setComponentTreeName() {
        componentTreeName = "Logic Gates.2 Input.NOR";
    }
    
    @Override
    public String getName(){
        return "Nor Gate";
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
        Point in1 = new Point(10, 20);
        Point in2 = new Point(10, 40);
        Point out1 = new Point(60, 30);
        
        localPins.add(in1);
        localPins.add(in2);
        localPins.add(out1);
        
    }
}