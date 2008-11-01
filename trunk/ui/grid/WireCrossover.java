/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import ui.UIConstants;
import ui.tools.SelectableComponent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 *
 * @author matt
 */
public class WireCrossover extends InvalidPoint {

    private static BufferedImage defaultBi;
    
    public WireCrossover(Point p, SelectableComponent parent){
        super(p,parent);
        try {
            defaultBi = ImageIO.read(new File("build/classes/ui/images/components/default_wire_crossover.png"));
        } catch (IOException ex) {
            Logger.getLogger(WireCrossover.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void draw(Graphics2D g2) {
         g2.drawImage(defaultBi, x-9, y-10, null);
    }
}
