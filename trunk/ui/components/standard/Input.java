package ui.components.standard;

import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 *
 * @author Matt
 */
public class Input extends VisualComponent{
    private boolean isOn = false;
    private BufferedImage specialBi;

    public Input(ui.CircuitPanel parent, Point point, sim.SimItem simItem) {
        super(parent, point, simItem);
        setSpecialImage();
    }
    
    @Override
    protected void setComponentTreeName() {
        keyName = "Standard.Button Source";
    }
   
    @Override
    public String getName(){
        return "Button Source [On/Off]";
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x-1,(int)getOrigin().getY()-getCentre().y-1,22,22);
        invalidArea = rotate(invalidArea);   
    }
    
    @Override
    public Point getCentre(){
        return new Point(10,10);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if(isOn){
            ((sim.componentLibrary.standard.Input) logicalComponent).setValue(sim.LogicState.OFF);
        } else {
            ((sim.componentLibrary.standard.Input) logicalComponent).setValue(sim.LogicState.ON);
        }
        isOn = !isOn; 
    }
    
    @Override
    protected BufferedImage getCurrentImage(){
        if(isOn){
            return specialBi;    
        } else {
            return super.getCurrentImage();            
        }    
    }
    
    protected void setSpecialImage() {
        specialBi = nl.getImage(keyName, "default_on");
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
                atts.addAttribute("", "", "name", "CDATA", "value");
                atts.addAttribute("", "", "value", "CDATA", (isOn)?"On":"Off");
                hd.startElement("", "", "attr", atts);
                hd.endElement("", "", "attr");

            hd.endElement("", "", "component");
        } catch (SAXException ex) {
             ui.error.ErrorHandler.newError("XML Creation Error","Please refer to the system output below.",ex);
        }
    }
    
    public boolean isOn() {
        return isOn;
    }

    public void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }

}
