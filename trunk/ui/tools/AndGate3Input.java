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
public class AndGate3Input extends ImageSelectableComponent{

    public AndGate3Input(Point point) {
        super(point);
    }

  @Override
    protected void setDefaultImage() {
         try {
            defaultBi = ImageIO.read(new File("build/classes/ui/images/components/default_3in_and.png"));
        } catch (IOException ex) {
            Logger.getLogger(AndGate2Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void setSelectedImage() {
         try {
            selectedBi = ImageIO.read(new File("build/classes/ui/images/components/selected_3in_and.png"));
        } catch (IOException ex) {
            Logger.getLogger(AndGate2Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void setActiveImage() {
         try {
            activeBi = ImageIO.read(new File("build/classes/ui/images/components/active_3in_and.png"));
        } catch (IOException ex) {
            Logger.getLogger(AndGate2Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String getName(){
        //return getComponent().getType();
        return "And Gate (2 Input)";
    }

    @Override
    protected void setInvalidAreas(){
        //Tight fitting box so that pins are not selected
        this.invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x+20,(int)getOrigin().getY()+20-getCentre().y,32,22);
    }
    
    @Override
    public Point getCentre(){
        return new Point(30,30);
    }
    
    @Override
    public void setLocalPins() {
        Point in1 = new Point(10, 20);
        Point in2 = new Point(10, 40);
        Point in3 = new Point(10, 40);
        Point out1 = new Point(60, 30);
                
        localPins.add(in1);
        localPins.add(in2);
        localPins.add(in3);
        localPins.add(out1);
        
    }
    
}
