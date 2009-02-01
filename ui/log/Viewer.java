/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.log;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import sim.LogicState;
import ui.UIConstants;

/**
 *
 * @author matt
 */
public class Viewer extends JPanel {

    private List<PinLogger> loggers = new LinkedList<PinLogger>();
    private Long startTime = Long.MAX_VALUE;
    
    public Viewer(){
        Timer timer = new Timer();
            timer.schedule(new TimerTask(){
                public void run() {
                    Viewer.this.repaint();
                }
            }, 0, 10);
    }
    
    public void addLogger(PinLogger pl){
        loggers.add(pl);
        if(pl.getStartTime()<startTime){
            startTime = pl.getStartTime();
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Background Colour
        g2.setColor(UIConstants.LOGGER_BACKGROUND_COLOUR);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        // Draw Vertical Grid Lines
        g2.setColor(UIConstants.LOGGER_GRID_COLOUR);
        for(int i=0; i< getWidth(); i+=20){
            g2.drawLine(i, 0, i, getHeight());
        }
                        
        float xOffset = 0f;
        float yOffset = 0.5f * UIConstants.LOG_VIEWER_MARGIN;
        
        for(PinLogger p: loggers){
            yOffset += UIConstants.LOG_VIEWER_MARGIN;
            
            Iterator<LogicState> states = p.getValues().iterator();
            Iterator<Long>  times  = p.getKeys().iterator();
            
            g2.translate(xOffset, yOffset);
                        
            // Draw Graphs
            g2.setColor(UIConstants.LOGGER_GRAPH_COLOR);
            
            // Do we have any values to display
            if(times.hasNext()){

                LogicState currState, prevState = states.next();
                Long currTime, prevTime = times.next() - startTime;

                while(times.hasNext()){
                    currState = states.next();
                    currTime = times.next();

                    // Offset from earliest log time shown
                    currTime -= startTime;
                                     
                    
                    // Draw Horizontal Part
                    g2.drawLine((int)(prevTime.intValue()*0.000001), 
                                (prevState.equals(LogicState.OFF))?UIConstants.LOG_HEIGHT:0,
                                (int)(currTime.intValue()*0.000001),
                                (prevState.equals(LogicState.OFF))?UIConstants.LOG_HEIGHT:0);
                    
                    // Draw Vertical Part (May be of zero length)
                    g2.drawLine((int)(currTime.intValue()*0.000001),
                                (prevState.equals(LogicState.OFF))?UIConstants.LOG_HEIGHT:0, 
                                (int)(currTime.intValue()*0.000001), 
                                (currState.equals(LogicState.OFF))?UIConstants.LOG_HEIGHT:0);
                    
                    System.out.println(currTime + " @ " + currState.toString());
                    
                    
                    prevTime = currTime;
                    prevState = currState;
                }
            
            }
            
            g2.translate(-xOffset, -yOffset);

            // Move down to next pin logger
            yOffset += UIConstants.LOG_VIEWER_MARGIN+UIConstants.LOG_HEIGHT;
            
        }        
        
    }
}
