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
import ui.ConnectionPoint;

/**
 *
 * @author Matt
 */
public class NorGate2Input extends ImageSelectableComponent{

    public NorGate2Input(Component component, Point point) {
        super(component, point);
    }

    @Override
    protected void setDefaultImage() {
         try {
            defaultBi = ImageIO.read(new File("build/classes/ui/images/components/default_2in_nor.png"));
        } catch (IOException ex) {
            Logger.getLogger(NorGate2Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void setSelectedImage() {
         try {
            selectedBi = ImageIO.read(new File("build/classes/ui/images/components/selected_2in_nor.png"));
        } catch (IOException ex) {
            Logger.getLogger(NorGate2Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void setActiveImage() {
         try {
            activeBi = ImageIO.read(new File("build/classes/ui/images/components/active_2in_nor.png"));
        } catch (IOException ex) {
            Logger.getLogger(NorGate2Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String getName(){
        //return getComponent().getType();
        return "And Gate (2 Input)";
    }

    @Override
    protected void setBoundingBox(){
        //Tight fitting box so that pins are not selected
        this.boundingBox = new Rectangle((int)getOrigin().getX()-getCentre().x+20,(int)getOrigin().getY()+15-getCentre().y,30,30);
    }
    
    @Override
    public Point getCentre(){
        return new Point(30,30);
    }
    
    @Override
    public void setConnectionPoints() {
        ConnectionPoint in1 = new ConnectionPoint(this, 10, 20);
        ConnectionPoint in2 = new ConnectionPoint(this, 10, 40);
        ConnectionPoint out1 = new ConnectionPoint(this, 60, 30);
                
        connectionPoints.add(in1);
        connectionPoints.add(in2);
        connectionPoints.add(out1);
        
    }
}
