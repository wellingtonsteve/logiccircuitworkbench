package ui.components.standard.log;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
        if(g2.getClip() == null){
            g2.setClip(((JScrollPane)getParent().getParent()).getViewportBorderBounds());
        }        
        
        if(startTime == Long.MAX_VALUE){
            for(PinLogger p: loggers){
                if(p.getStartTime()<startTime){
                    startTime = p.getStartTime();
                }
            }
        }
        
        // Background Colour
        g2.setColor(UIConstants.LOGGER_BACKGROUND_COLOUR);
        g2.fill(g2.getClipBounds());
        
        // Draw Vertical Grid Lines
        g2.setColor(UIConstants.LOGGER_GRID_COLOUR);
        double d = (startTime*(scaleFactor/100));
        for(int i=(int)((g2.getClipBounds().x-d)%20)*20; i< g2.getClipBounds().getMaxX(); i+=20){
            if(i % 100 == 0){
                
                g2.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);
                String label = ((double)(i/(double) 100)) + "\u00D710\u2079ns";
                g2.drawString(label, i+1, 15);
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
 
                Long prevTime =(long) (g2.getClipBounds().x/scaleFactor)-startTime;
                LogicState prevState = LogicState.FLOATING;            
                p.commitBuffersToMemory();
                if(!p.getSavedTimes().isEmpty()){
                    prevState = p.getSavedStates().get(Math.max(p.getStartIndex(startTime)-1,0));   
                    List<Long> timeBuffer = p.getTimesBetween(prevTime, endTime);
                    List<LogicState> stateBuffer = p.getStatesBetween(prevTime, endTime);              
                       
                    g2.translate(xOffset, yOffset);
                    g2.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);
                    g2.drawString(p.getName(), g2.getClipBounds().x+1, (UIConstants.LOGGER_HEIGHT/2)-3);
                    
                    // Draw Graphs
                    g2.setColor(UIConstants.LOGGER_GRAPH_COLOR);

                    // Do we have any values to display
                    for(int i = 0; i<timeBuffer.size(); i++){
                        Long currTime = timeBuffer.get(i);
                        LogicState currState = stateBuffer.get(i);

                        currTime -= startTime;

                        if(!prevState.equals(LogicState.FLOATING) && !currState.equals(LogicState.FLOATING)){                        
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
                        }
                        prevTime = currTime;
                        prevState = currState;
                    }
                }
                
                if(!prevState.equals(LogicState.FLOATING)){          
                g2.drawLine((int)(prevTime*scaleFactor),
                         (prevState.equals(LogicState.OFF))?UIConstants.LOGGER_HEIGHT:0,
                        (int)((endTime-startTime)*scaleFactor),
                        (prevState.equals(LogicState.OFF))?UIConstants.LOGGER_HEIGHT:0);
                }                          

                g2.translate(-xOffset, -yOffset);
                
                // Move down to next pin logger
                yOffset += UIConstants.LOGGER_VIEWER_MARGIN+UIConstants.LOGGER_HEIGHT;
            }                        
        }                               
    }
    
    public void clear(){
        startTime = Long.MAX_VALUE;
        endTime = 0l;
        for(PinLogger p: loggers){
            p.clear();
        }
        Dimension b = getPreferredSize();
        setPreferredSize(new Dimension(500, b.height));
        ((JScrollPane)getParent().getParent()).getViewport().setViewPosition(new Point(0,0));
    }

    public void SimulatorStateChanged(SimulatorState state) {
    }

    public void SimulationTimeChanged(long time) {
        // Auto-Scrolling policy
        Dimension b = getPreferredSize();
        if((endTime-startTime)*scaleFactor > b.width){
            setPreferredSize(new Dimension((int)((endTime-startTime)*scaleFactor), b.height));
            revalidate();
        }
        endTime = time;
        repaint(((JScrollPane)getParent().getParent()).getViewportBorderBounds());
                
        
    }
}
