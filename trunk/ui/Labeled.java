/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.awt.Graphics;
import ui.tools.*;
import java.awt.Graphics2D;

/**
 *
 * @author matt
 */
public interface Labeled {
    
    public void draw(Graphics g);
                
    public String getLabel();

    public void setLabel(String label);
    
    public void removeLabel();
    
    public boolean hasLabel();
}
