package ui.components.standard;

import java.awt.Graphics2D;
import sim.LogicState;
import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import sim.Simulator;
import ui.UIConstants;

/**
 *
 * @author Matt
 */
public class PinLogger extends VisualComponent implements sim.joinable.ValueListener {

    public PinLogger(ui.CircuitPanel parent, Point point, sim.SimItem simItem) {
        super(parent, point, simItem);
        this.pin = logicalComponent.getPinByName("Input");
        this.pin.addValueListener(this); 
        this.sim = parent.getSimulator();      
        setLabel("#"+(this.parent.getPinLoggers().size()+1));
    }
            
    /**
     * SELECTABLE COMPONENT CODE
     */
    
    private Pin in1;
    
    @Override
    protected void setComponentTreeName() {
        keyName = "Standard.Output Logger";
    }
    
    
    @Override
    public String getName(){
        return "Output Logger";
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x,(int)getOrigin().getY()-getCentre().y,21,21);
        invalidArea = rotate(invalidArea);   
    }
    
    @Override
    public Point getCentre(){
        return new Point(10,10);
    }

//    @Override
//    public void setLocalPins() {
//        localPins.clear(); 
//        in1 = new Pin(10, 30, logicalComponent.getPinByName("Input"));
//        localPins.add(in1);        
//    }
            
    @Override
    public void draw(Graphics2D g) {
        if(hasLabel()){
            g.setColor(UIConstants.LABEL_TEXT_COLOUR);
            g.drawString(getLabel(), 
                    getOrigin().x+UIConstants.LABEL_COMPONENT_X_OFFSET-10,
                    getOrigin().y+UIConstants.LABEL_COMPONENT_Y_OFFSET-10);
        }
        g.rotate(rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);      
        g.translate(getOrigin().x, getOrigin().y);               
        g.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);
        g.drawLine(10, 30, 10, 20);
        g.translate(-getOrigin().x, -getOrigin().y);
        g.drawImage(getCurrentImage(), getOrigin().x, getOrigin().y, null);
        g.rotate(-rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
    }
    
    /**
     * PIN LOGGER CODE
     */
    
    private LinkedList<Long> timeLog = new LinkedList<Long>();
    private LinkedList<LogicState> stateLog = new LinkedList<LogicState>();
    
    private LinkedList<Long> timeBuffer = new LinkedList<Long>();
    private LinkedList<LogicState> stateBuffer = new LinkedList<LogicState>();
    
    private sim.joinable.Pin pin;
    private Simulator sim;
    private Long firstTime = Long.MAX_VALUE;
    private Long lastTime = 0l;
    private boolean enabled = true;
    
    public int getStartIndex(Long startTime){
        int start = -1;
        for(int i =timeLog.size()-1; i>=0; i--){
            if(timeLog.get(i) <= startTime){
                start = i;
                break;
            }
        }
        if(start == -1){
            start = 0;
        }
        return start;
    }
    
    public int getEndIndex(Long endTime){
        int end = -1;
        for(int i =timeLog.size()-1; i>=0; i--){
            if(timeLog.get(i) <= endTime){
                end = i+1;
                break;
            }
        }
        if(end == -1){
            end = timeLog.size()-1;
        }
        if(end > timeLog.size()-1){
            end = timeLog.size()-1;
        }
        //System.out.println(endTime + " " + timeLog.get(end));
        return end;
    }
    
    public void commitBuffersToMemory(){
        timeLog.addAll(timeBuffer);
        stateLog.addAll(stateBuffer);
        timeBuffer.clear();
        stateBuffer.clear();
    }
    
    public List<Long> getTimesBetween(Long startTime, Long endTime){  
        return timeLog.subList(getStartIndex(startTime), getEndIndex(endTime)+1);
    } 
    
    public List<LogicState> getStatesBetween(Long startTime, Long endTime){  
        return stateLog.subList(getStartIndex(startTime), getEndIndex(endTime)+1);
    } 
 
    public List<LogicState> getSavedStates(){
        return stateLog;
    }
    
    public List<Long> getSavedTimes(){
        return timeLog;
    }
        
    public Long getStartTime(){
        return firstTime;
    }
    
    public Long getEndTime(){
        return lastTime;
    }

    public void valueChanged(sim.joinable.Pin pin, LogicState value) {
        assert(timeLog.size() == stateLog.size());
        Long nextSimTime = parent.getSimulator().getSimulationTime();
        timeBuffer.add(nextSimTime);
        stateBuffer.add(value);
        if(firstTime == Long.MAX_VALUE){
            firstTime = nextSimTime;
        }
        lastTime = nextSimTime;
    }
    
    public void clear(){
        timeLog.clear();
        stateLog.clear();
        firstTime = Long.MAX_VALUE;
        lastTime = 0l;
    }

    public void setEnabled(boolean b) {
        this.enabled = b;
    }
    
    public boolean isEnabled(){
        return enabled;
    }
    
}
