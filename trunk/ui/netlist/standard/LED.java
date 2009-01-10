/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.netlist.standard;

import ui.tools.*;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import sim.Component;
import ui.CircuitPanel;
import ui.UIConstants;

/**
 *
 * @author Matt
 */
public class LED extends ImageSelectableComponent{
    private boolean isOn;
    private String colour = "yellow";

    public LED(CircuitPanel parent, Point point) {
        super(parent, point);
    }

    @Override
    protected void setNetlist() {
        nl = new ui.netlist.standard.Standard();
    }
    
    @Override
    protected void setComponentTreeName() {
        componentTreeName = "Standard.LED";
    }
    
    @Override
    protected void setActiveImage() {
        try {
            if(colour==null){
                colour = "yellow";
            }
            if(colour.equals("red")){
                activeBi = ImageIO.read(new File("build/classes/ui/images/components/default_led_on_red.png"));
            } else if(colour.equals("green")){
                activeBi = ImageIO.read(new File("build/classes/ui/images/components/default_led_on_green.png"));
            } else {
                activeBi = ImageIO.read(new File("build/classes/ui/images/components/default_led_on_yellow.png"));
            }
                    
        } catch (IOException ex) {
            Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String getName(){
        //return getComponent().getType();
        return "Light Emitting Diode";
    }

    @Override
    protected void setInvalidAreas(){
        //Tight fitting box so that pins are not selected
        this.invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x+18,(int)getOrigin().getY()-getCentre().y,14,21);
    }
    
    @Override
    public Point getCentre(){
        return new Point(20,10);
    }

    @Override
    public void setLocalPins() {
        Point in1 = new Point(10, 20);
             
        localPins.add(in1);        
    }
        
    public void setValue(boolean isOn){
        this.isOn = isOn;
    }
    
    public boolean getValue(){
        return isOn;
    }
    
    @Override
    public void draw(Graphics2D g) {
        if(hasLabel()){
            g.setColor(UIConstants.LABEL_TEXT_COLOUR);
            g.drawString(getLabel(), getOrigin().x+10, getOrigin().y-2);
        }
        
        g.rotate(rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
        g.drawImage(getCurrentImage(), (int)getOrigin().getX(), (int)getOrigin().getY(), null);
        
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
        g.drawOval(getOrigin().x+18,getOrigin().y+3,13,13);
        
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
                atts.addAttribute("", "", "name", "CDATA", "colour");
                atts.addAttribute("", "", "value", "CDATA", colour);
                hd.startElement("", "", "attr", atts);
                hd.endElement("", "", "attr");

            hd.endElement("", "", "component");
        } catch (SAXException ex) {
            Logger.getLogger(ImageSelectableComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour.toLowerCase();
        setActiveImage();
    }
    
    
}
