/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package netlist.standard;

import ui.tools.*;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import ui.CircuitPanel;
import ui.UIConstants;

/**
 *
 * @author Matt
 */
public class Input extends ImageSelectableComponent{
    private boolean isOn = false;

    public Input(CircuitPanel parent, Point point) {
        super(parent, point);
    }
    
    @Override
    protected void setComponentTreeName() {
        componentTreeName = "Standard.Button Source";
    }
   
    @Override
    public String getName(){
        return "Button Source [On/Off]";
    }

    @Override
    protected void setNetlist() {
        nl = new netlist.standard.Standard();
    }
    
    @Override
    protected void setInvalidAreas(){
        //Tight fitting box so that pins are not selected
        this.invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x,(int)getOrigin().getY()-getCentre().y,22,22);
    }
    
    @Override
    public Point getCentre(){
        return new Point(10,10);
    }

    @Override
    public void setLocalPins() {
        Point out1 = new Point(30, 10);
                
        localPins.add(out1);        
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if(e != null) { isOn = !isOn; } // To check that this is an activation due to a click, not a selection
    }
    
    @Override
    public void draw(Graphics2D g) {
        if(hasLabel()){
            g.setColor(UIConstants.LABEL_TEXT_COLOUR);
            g.drawString(getLabel(), getOrigin().x, getOrigin().y-2);
        }
        
        g.rotate(rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
        g.drawImage(getCurrentImage(), (int)getOrigin().getX(), (int)getOrigin().getY(), null);
        
        g.translate(getCentre().x, getCentre().y);
        switch(getSelectionState()){            
            case ACTIVE:
                g.setColor(UIConstants.ACTIVE_WIRE_COLOUR);
                break;
            case HOVER:
                g.setColor(UIConstants.HOVER_WIRE_COLOUR);   
                break;
            default: 
                g.setColor(UIConstants.DEFAULT_WIRE_COLOUR);
                break;
        }   
        g.drawRect(invalidArea.x+3, invalidArea.y, 19, 19);
        g.translate(-getCentre().x, -getCentre().y);
        
        g.rotate(-rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
    }

    @Override
    protected BufferedImage getCurrentImage(){
        if(isOn){
            return getActiveImage();    
        } else {
            return getDefaultImage();            
        }    
    }
    
    @Override
    public void createXML(TransformerHandler hd) {
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "", "type", "CDATA", this.getClass().getSimpleName());
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
            Logger.getLogger(ImageSelectableComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isOn() {
        return isOn;
    }

    public void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }

}
