/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.tools;

import java.awt.Point;
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
public class Wire extends SelectableComponent {

    public Wire(Component component, Point wireStart, Point wireEnd) {
        
        
        
        super(component, 0, 0);
    }

    @Override
    protected void setDefaultImage() {
         try {
            defaultBi = ImageIO.read(new File("build/classes/ui/images/components/transparent.png"));
        } catch (IOException ex) {
            Logger.getLogger(AndGate2Input.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void setSelectedImage() {
        selectedBi = defaultBi;
    }

    @Override
    protected void setActiveImage() {
       activeBi = defaultBi;
    }


}
