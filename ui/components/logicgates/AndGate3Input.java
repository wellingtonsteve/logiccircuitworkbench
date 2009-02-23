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
public class AndGate3Input extends VisualComponent{
    
    public AndGate3Input(ui.CircuitPanel parent, Point point, sim.SimItem simItem, netlist.properties.Properties properties) {
        super(parent, point, simItem,properties);
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
    
//    @Override
//    public void setLocalPins() {
//        localPins.clear();
//        
//        Pin in1 = new Pin(10, 20, logicalComponent.getPinByName("Input 1"));
//        Pin in2 = new Pin(10, 30, logicalComponent.getPinByName("Input 2"));
//        Pin in3 = new Pin(10, 40, logicalComponent.getPinByName("Input 3"));
//        Pin out1 = new Pin(60, 30, logicalComponent.getPinByName("Output"));
//                
//        localPins.add(in1);
//        localPins.add(in2);
//        localPins.add(in3);
//        localPins.add(out1);
//        
//    }
    
    @Override
    protected void setKeyName() {
        keyName = "Logic Gates.3 Input.AND";
    }
    
}
