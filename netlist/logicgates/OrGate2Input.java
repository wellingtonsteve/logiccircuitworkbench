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
 * @author Matt
 */
public class OrGate2Input extends ImageSelectableComponent{

    public OrGate2Input(ui.CircuitPanel parent, Point point) {
        super(parent, point);
    }

    @Override
    protected void setNetlist() {
        nl = new netlist.logicgates.LogicGates();
    }
        
    @Override
    protected void setComponentTreeName() {
        componentTreeName = "Logic Gates.2 Input.OR";
    }
    
    @Override
    public String getName(){
        return "Or Gate";
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
        Point in2 = new Point(10, 40);
        Point out1 = new Point(60, 30);
                
        localPins.add(in1);
        localPins.add(in2);
        localPins.add(out1);
        
    }
}
