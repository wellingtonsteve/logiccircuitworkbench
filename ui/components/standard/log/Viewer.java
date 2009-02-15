package ui.components.standard.log;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import sim.LogicState;
import sim.SimulatorState;
import ui.UIConstants;

/**
 *
 * @author matt
 */
public class Viewer extends JPanel implements sim.SimulatorStateListener {
    public static final double scaleFactor = 1.0E-7;

    private List<PinLogger> loggers = new LinkedList<PinLogger>();
    private Long startTime = Long.MAX_VALUE;
    private Long endTime = 0l;
               
    public void addLogger(PinLogger pl){
        if(!loggers.contains(pl)){
            loggers.add(pl);
            if(pl.getStartTime()<startTime){
                startTime = pl.getStartTime();
            }
        }
    }
     
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Background Colour
        g2.setColor(UIConstants.LOGGER_BACKGROUND_COLOUR);
        g2.fill(g2.getClipBounds());

//        g2.setColor(Color.ORANGE);
//        g2.draw(g2.getClip());
        
        // Draw Vertical Grid Lines
        g2.setColor(UIConstants.LOGGER_GRID_COLOUR);
        for(int i=(g2.getClipBounds().x%20)*20; i< g2.getClipBounds().getMaxX(); i+=20){
            if(i % 100 == 0){
                g2.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);
                g2.drawString(((double)(i/(double) 100)) + "\u00D710\u2079ns", i+1, 15);
                g2.drawLine(i, 0, i, g2.getClipBounds().height);            
            } else {
                g2.setColor(UIConstants.LOGGER_GRID_COLOUR);
                g2.drawLine(i, 0, i, g2.getClipBounds().height);
            }
        }        
        
        float xOffset = 0f;
        float yOffset = 0.5f * UIConstants.LOGGER_VIEWER_MARGIN;
        
        for(PinLogger p: loggers){            
            
            if(p.isEnabled()){
                yOffset += UIConstants.LOGGER_VIEWER_MARGIN;
                p.commitBuffersToMemory();
                Long prevTime =(long) (g2.getClipBounds().x/scaleFactor);
                List<Long> timeBuffer = p.getTimesBetween(prevTime, endTime);
                List<LogicState> stateBuffer = p.getStatesBetween(prevTime, endTime);              
                LogicState prevState = LogicState.FLOATING;
                
                g2.translate(xOffset, yOffset);

                // Draw Graphs
                g2.setColor(UIConstants.LOGGER_GRAPH_COLOR);
                
                // Do we have any values to display
                for(int i = 0; i<timeBuffer.size(); i++){
                    Long currTime = timeBuffer.get(i);
                    LogicState currState = stateBuffer.get(i);

                    currTime -= startTime;

                    // Draw Horizontal Part
                    g2.drawLine((int)(prevTime*scaleFactor), 
                                (prevState.equals(LogicState.OFF))?UIConstants.LOGGER_HEIGHT:0,
                                (int)(currTime*scaleFactor),
                                (prevState.equals(LogicState.OFF))?UIConstants.LOGGER_HEIGHT:0);

                    // Draw Vertical Part (May be of zero length)
                    g2.drawLine((int)(currTime*scaleFactor),
                                (prevState.equals(LogicState.OFF))?UIConstants.LOGGER_HEIGHT:0, 
                                (int)(currTime*scaleFactor), 
                                (currState.equals(LogicState.OFF))?UIConstants.LOGGER_HEIGHT:0);

                    prevTime = currTime;
                    prevState = currState;
                }
                             
                g2.drawLine((int)(prevTime*scaleFactor), 
                            (prevState.equals(LogicState.OFF))?UIConstants.LOGGER_HEIGHT:0,
                            (int)((endTime-startTime)*scaleFactor),
                            (prevState.equals(LogicState.OFF))?UIConstants.LOGGER_HEIGHT:0);

                g2.translate(-xOffset, -yOffset);
                
                // Move down to next pin logge
                yOffset += UIConstants.LOGGER_VIEWER_MARGIN+UIConstants.LOGGER_HEIGHT;
            }                        
        }                               
    }
    
    public void clear(){
        startTime = Long.MAX_VALUE;
        endTime = 0l;
        for(PinLogger p: loggers){
            p.clear();
            if(p.getStartTime()<startTime){
                startTime = p.getStartTime();
            }
        }
    }

    public void SimulatorStateChanged(SimulatorState state) {
        
    }

    public void SimulationTimeChanged(long time) {
        // Auto-Scrolling policy
        Dimension b = getPreferredSize();
        if((time*scaleFactor)%100 == 0
                &&(endTime-startTime)*scaleFactor > b.width){
            setPreferredSize(new Dimension((int) b.width+500, b.height));
            revalidate();
        }
        endTime = time;
        repaint(((JViewport)getParent()).getViewRect());
                
        
    }

    private Rectangle getRepaintClipBounds(long time) {        
        int x = (int) (endTime * scaleFactor);
        int y = 0;
        int width = (int) ((time-endTime) * scaleFactor);
        int height = getHeight();
        endTime = time;
        return new Rectangle(x, y, width, height);
        
    }
}
