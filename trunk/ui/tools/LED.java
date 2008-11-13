/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.tools;

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
import sim.Component;
import ui.UIConstants;

/**
 *
 * @author Matt
 */
public class LED extends ImageSelectableComponent{
    private boolean isOn;

    public LED(Component component, Point point) {
        super(component, point);
    }

     @Override
    protected void setDefaultImage() {
         try {
            defaultBi = ImageIO.read(new File("build/classes/ui/images/components/default_led.png"));
        } catch (IOException ex) {
            Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void setSelectedImage() {
         try {
            selectedBi = ImageIO.read(new File("build/classes/ui/images/components/default_led.png"));
        } catch (IOException ex) {
            Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void setActiveImage() {
         try {
            activeBi = ImageIO.read(new File("build/classes/ui/images/components/default_led_on_yellow.png"));
        } catch (IOException ex) {
            Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String getName(){
        //return getComponent().getType();
        return "LED";
    }

    @Override
    protected void setBoundingBox(){
        //Tight fitting box so that pins are not selected
        this.boundingBox = new Rectangle((int)getOrigin().getX()-getCentre().x+18,(int)getOrigin().getY()-getCentre().y,14,21);
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
    
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if(e != null) { isOn = !isOn; } // To check that this is an activation due to a click, not a selection
    }
    
    @Override
    public void draw(Graphics2D g, javax.swing.JComponent parent) {
        g.rotate(rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
        g.drawImage(getCurrentImage(), (int)getOrigin().getX(), (int)getOrigin().getY(), parent);
        
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
        // TODO: set stroke to dotted, possibly knock out image when active?
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
    
}
