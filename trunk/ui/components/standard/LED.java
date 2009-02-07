package ui.components.standard;

import sim.LogicState;
import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 *
 * @author Matt
 */
public class LED extends ImageSelectableComponent implements sim.pin.ValueListener{
    private boolean isOn;
    private String colour = "yellow";
    private BufferedImage specialBi;

    public LED(ui.CircuitPanel parent, Point point, sim.SimItem simItem) {
        super(parent, point, simItem);
        setSpecialImage();
        logicalComponent.getPinByName("Input").addValueListener(this);
    }

    @Override
    protected void setNetlist() {
        nl = new netlist.Standard();
    }
    
    @Override
    protected void setComponentTreeName() {
        componentTreeName = "Standard.LED";
    }
    
    protected void setSpecialImage() {
        try {
            if(colour==null){
                colour = "yellow";
            }
            if(colour.equals("red")){
                specialBi = ImageIO.read(getClass().getResource("/ui/images/components/default_led_on_red.png"));
            } else if(colour.equals("green")){
                specialBi = ImageIO.read(getClass().getResource("/ui/images/components/default_led_on_green.png"));
            } else {
                specialBi = ImageIO.read(getClass().getResource("/ui/images/components/default_led_on_yellow.png"));
            }
                    
        } catch (IOException ex) {
             ui.error.ErrorHandler.newError(new ui.error.Error("Initialisation Error", "Could not load \"LED On\" image.", ex));    
             specialBi = activeBi;
        }
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

    @Override
    public void setLocalPins() {
        localPins.clear();
        Pin in1 = new Pin(10, 10, logicalComponent.getPinByName("Input"));             
        localPins.add(in1);        
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
            return specialBi;    
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
        setActiveImage();
    }

    public void valueChanged(sim.pin.Pin pin, LogicState value) {
        setValue(value.equals(sim.LogicState.ON));
        parent.repaint(getBoundingBox());
    }
}