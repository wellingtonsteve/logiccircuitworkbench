package ui.components.standard;

import java.awt.event.ActionEvent;
import sim.LogicState;
import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.image.BufferedImage;
import javax.swing.JComboBox;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 *
 * @author Matt
 */
public class LED extends VisualComponent implements sim.pin.ValueListener{
    private boolean isOn;
    private String colour = "yellow";
    private BufferedImage yellow, red, green;

    public LED(ui.CircuitPanel parent, Point point, sim.SimItem simItem) {
        super(parent, point, simItem);
        setSpecialImage();
        logicalComponent.getPinByName("Input").addValueListener(this);
        ((JComboBox)nl.getProperties(keyName).getAttribute("Colour").getJComponent()).addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                
            }
        
        });
    }
    
    @Override
    protected void setComponentTreeName() {
        keyName = "Standard.LED";
    }
    
    protected void setSpecialImage() {
        yellow = nl.getImage(keyName, "yellow");
        red = nl.getImage(keyName, "red");
        green = nl.getImage(keyName, "green");
    }
    
    @Override
    public String getName(){
        return "Light Emitting Diode";
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x+18,(int)getOrigin().getY()-getCentre().y-1,14,22);
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
        if(isOn){
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
    
    @Override
    public void createXML(TransformerHandler hd) {
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "", "type", "CDATA", getComponentTreeName());
            atts.addAttribute("", "", "x", "CDATA", String.valueOf(getOrigin().x));
            atts.addAttribute("", "", "y", "CDATA", String.valueOf(getOrigin().y));
            atts.addAttribute("", "", "rotation", "CDATA", String.valueOf(rotation));
            
            hd.startElement("", "", "component", atts);

                atts.clear();
                atts.addAttribute("", "", "name", "CDATA", "colour");
                atts.addAttribute("", "", "value", "CDATA", colour);
                hd.startElement("", "", "attr", atts);
                hd.endElement("", "", "attr");

            hd.endElement("", "", "component");
        } catch (SAXException ex) {
             ui.error.ErrorHandler.newError("XML Creation Error","Please refer to the system output below",ex);
        }
    }
    
    
    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour.toLowerCase();
    }

    public void valueChanged(sim.pin.Pin pin, LogicState value) {
        setValue(value.equals(sim.LogicState.ON));
        parent.repaint(getBoundingBox());
    }
}
