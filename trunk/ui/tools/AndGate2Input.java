/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.tools;

import ui.*;
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
public class AndGate2Input extends SelectableComponent{

    public AndGate2Input(Component component, int x, int y) {
        super(component, x, y);
    }

    @Override
    protected void setDefaultImage() {
         try {
            defaultBi = ImageIO.read(new File("build/classes/ui/images/components/default_andgate.png"));
        } catch (IOException ex) {
            Logger.getLogger(AndGate2Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void setSelectedImage() {
         try {
            selectedBi = ImageIO.read(new File("build/classes/ui/images/components/selected_andgate.png"));
        } catch (IOException ex) {
            Logger.getLogger(AndGate2Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void setActiveImage() {
         try {
            activeBi = ImageIO.read(new File("build/classes/ui/images/components/active_andgate.png"));
        } catch (IOException ex) {
            Logger.getLogger(AndGate2Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     @Override
     public String getName(){
         return "And Gate (2 Input)";
     }

}
