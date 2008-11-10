/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.tools;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import sim.Component;

/**
 *
 * @author Matt
 */
public class Input extends ImageSelectableComponent{

    public Input(Component component, Point point) {
        super(component, point);
    }

       @Override
    protected void setDefaultImage() {
         try {
            defaultBi = ImageIO.read(new File("build/classes/ui/images/components/default_input_off.png"));
        } catch (IOException ex) {
            Logger.getLogger(LED.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void setSelectedImage() {
         try {
            selectedBi = ImageIO.read(new File("build/classes/ui/images/components/default_input_off.png"));
        } catch (IOException ex) {
            Logger.getLogger(LED.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void setActiveImage() {
         try {
            activeBi = ImageIO.read(new File("build/classes/ui/images/components/default_input_on.png"));
        } catch (IOException ex) {
            Logger.getLogger(LED.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String getName(){
        //return getComponent().getType();
        return "Input";
    }

    @Override
    protected void setBoundingBox(){
        //Tight fitting box so that pins are not selected
        this.boundingBox = new Rectangle((int)getOrigin().getX()-getCentre().x+3,(int)getOrigin().getY()-getCentre().y,20,20);
    }
    
    @Override
    public Point getCentre(){
        return new Point(13,10);
    }

    @Override
    public void setLocalPins() {
        Point out1 = new Point(30, 10);
             
        localPins.add(out1);        
    }

    
}
