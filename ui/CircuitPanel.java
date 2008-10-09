/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


/**
 *
 * @author Matt
 */
class CircuitPanel extends JPanel {
    
    private int circuitX, circuitY, frameX, frameY;
    private BufferedImage bi;
    private Map<String,String> componentImageLoc = new HashMap<String,String>();
    
    public CircuitPanel(){
        frameX = this.getX();
        frameY = this.getY();        
        
        addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseDragged(MouseEvent e) {
              circuitX = e.getX()-frameX;
              circuitY = e.getY()-frameY;
              repaint();
            }
        });
        
        registerComponents();
        
    }

    
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
        
        g.translate(-40, -30);
        
        if(UIConstants.SNAP_TO_GRID){
            g.drawImage(bi, (circuitX / 10)*10, (circuitY / 10)*10, this);
            
        } else {
            g.drawImage(bi, circuitX, circuitY, this);
        }
    }
    
    public void addComponent(String name, String loc){
        componentImageLoc.put(name, loc);
    }
    
    private void registerComponents(){
        addComponent("Select","build/classes/ui/images/components/select.png");
        addComponent("AndGate","build/classes/ui/images/components/andgate.png");
        // Etc...
    }
    
    public void selectComponent(String name){
        String loc = componentImageLoc.get(name); 
        if(loc!=null){
            try {
                bi = ImageIO.read(new File(loc));
            } catch (IOException ex) {
                //Logger.getLogger(CircuitPanel.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }
    }
    

}
