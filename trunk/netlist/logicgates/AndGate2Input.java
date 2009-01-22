package netlist.logicgates;

import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author Matt
 */
public class AndGate2Input extends ImageSelectableComponent{    
    
    public AndGate2Input(ui.CircuitPanel parent, Point point) {
        super(parent, point);
    }
    
    @Override
    public String getName(){
        return "And Gate (2 Input)";
    }

    @Override
    protected void setInvalidAreas(){
        this.invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x+20,(int)getOrigin().getY()+20-getCentre().y,32,22);
        Point rotOrigin = rotate(new Point(getOrigin().x, getOrigin().y));
        java.awt.geom.AffineTransform test = new java.awt.geom.AffineTransform();
        test.rotate(Math.toRadians(rotation), -rotOrigin.x, -rotOrigin.y);
        //test.translate(getOrigin().x+getCentre().x, getOrigin().y+getCentre().y);
        //System.out.println(test.createTransformedShape(new Rectangle(invalidArea)).getBounds().toString());
        //Rectangle bounds = test.createTransformedShape(new Rectangle(invalidArea))..getBounds();
        //bounds.translate(getOrigin().x, getOrigin().y);
        //return bounds;
        this.invalidArea = test.createTransformedShape(new Rectangle(invalidArea)).getBounds();
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

    @Override
    protected void setNetlist() {
        nl = new netlist.logicgates.LogicGates();
    }

    @Override
    protected void setComponentTreeName() {
        componentTreeName = "Logic Gates.2 Input.AND";
    }
    
}
