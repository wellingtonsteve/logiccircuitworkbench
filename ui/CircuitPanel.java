/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Matt
 */
class CircuitPanel extends JPanel {
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);        
        
        // Background Colour
        g.setColor(UIConstants.CIRCUIT_BACKGROUND_COLOUR);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        // Grid Snap Dots
        g.setColor(UIConstants.GRID_DOT_COLOUR);
        for(int i =  0; i< this.getWidth(); i+=UIConstants.GRID_DOT_SPACING){
            for(int j = 0; j < this.getHeight(); j+=UIConstants.GRID_DOT_SPACING){
                g.fillRect(i, j, 1, 1);
            }
        }
               
    }

}
