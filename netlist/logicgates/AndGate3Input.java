/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package netlist.logicgates;

import ui.tools.*;
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
        //return getComponent().getType();
        return "And Gate (3 Input)";
    }

    @Override
    protected void setInvalidAreas(){
        //Tight fitting box so that pins are not selected
        this.invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x+20,(int)getOrigin().getY()+20-getCentre().y,32,22);
    }
    
    @Override
    public Point getCentre(){
        return new Point(30,30);
    }
    
    @Override
    public void setLocalPins() {
        Point in1 = new Point(10, 20);
        Point in2 = new Point(10, 30);
        Point in3 = new Point(10, 40);
        Point out1 = new Point(60, 30);
                
        localPins.add(in1);
        localPins.add(in2);
        localPins.add(in3);
        localPins.add(out1);
        
    }
    
    @Override
    protected void setNetlist() {
        nl = new netlist.logicgates.LogicGates();
    }
    
    @Override
    protected void setComponentTreeName() {
        componentTreeName = "Logic Gates.3 Input.AND";
    }
    
}
