package ui.components.standard;

import netlist.properties.Attribute;
import sim.LogicState;
import sim.SimulatorState;
import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import netlist.properties.AttributeListener;
import netlist.properties.Properties;

/** This component represents an LED (Light emitting diode). It has two states
 * corresponding to on and off and is available in three colours (yellow, red
 * and green). @author Matt */
public class LED extends VisualComponent implements sim.joinable.ValueListener, 
                                                    sim.SimulatorStateListener{
    private boolean isOn = true;
    private String colour = "yellow";
    private BufferedImage yellow, red, green;

    public LED(ui.CircuitPanel parent, Point point, sim.SimItem simItem, Properties properties) {
        super(parent, point, simItem,properties);
        setSpecialImage();
    }

    @Override
    public void addListeners() {
        logicalComponent.getPinByName("Input").addValueListener(this);
        parent.getSimulator().addStateListener(this);
        properties.getAttribute("Colour").addAttributeListener(this);
        properties.getAttribute("Colour").addAttributeListener(new AttributeListener(){
            public void attributeValueChanged(Attribute attr, Object value) {
                parent.getParentFrame().getEditor().repaintOptionsPanel();
            }            
        });
        setColour((String) properties.getAttribute("Colour").getValue());
    }
    
    protected void setSpecialImage() {
        yellow = properties.getImage("yellow");
        red = properties.getImage("red");
        green = properties.getImage("green");
    }
    
    @Override
    public String getName(){
        return "Light Emitting Diode";
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()+18,
                (int)getOrigin().getY()+2,14,14);
        invalidArea = rotate(invalidArea);   
    }
    
    @Override
    public Point getCentre(){
        return new Point(20,10);
    }
        
    public void setValue(boolean isOn){
        this.isOn = isOn;
    }
    
    public boolean getValue(){
        return isOn;
    }

    @Override
    protected BufferedImage getCurrentImage(){
        if(isOn && !selectionState.equals(selectionState.ACTIVE)){
            if(colour.equals("yellow")){
                return yellow;
            } else if(colour.equals("red")){
                return red;
            } else {
                return green;
            }
        } else {
            return super.getCurrentImage();            
        }    
    }
    
    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour.toLowerCase();
    }

    public void valueChanged(sim.joinable.Pin pin, LogicState value) {
        setValue(value.equals(sim.LogicState.ON));
        parent.repaint(getBoundingBox());
    }

    @Override
    public void SimulatorStateChanged(SimulatorState state) {
        if(state.equals(SimulatorState.STOPPED)){
            isOn = true;
        } else if(state.equals(SimulatorState.PLAYING)){
            isOn = false;
        }
    }

    @Override
    public void SimulationTimeChanged(long time) {}
    

    @Override
    public void SimulationRateChanged(int rate) {}

    @Override
    public void attributeValueChanged(Attribute attr, Object value) {
        if(attr.getName().equals("Colour")){
           setColour((String) attr.getValue());
        }
    }
}