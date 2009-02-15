package ui.components.standard.log;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import sim.LogicState;
import sim.Simulator;
import sim.pin.*;

/**
 *
 * @author matt
 */
public class PinLogger implements sim.pin.ValueListener {

    private LinkedList<Long> timeLog = new LinkedList<Long>();
    private LinkedList<LogicState> stateLog = new LinkedList<LogicState>();
    
    private LinkedList<Long> timeBuffer = new LinkedList<Long>();
    private LinkedList<LogicState> stateBuffer = new LinkedList<LogicState>();
    
    private Pin pin; 
    private Long startTime = -1l;
    private Long endTime = 0l;
    private Simulator sim;
    private boolean enabled = true;
    
    public PinLogger(ui.components.SelectableComponent.Pin pin){
        this.pin = (OutputPin) pin.getParent().getLogicalComponent().getPinByName("Output");
        this.sim = pin.getParent().getParent().getSimulator();
        this.pin.addValueListener(this);        
    }
    
    public PinLogger(sim.pin.Pin pin, Simulator sim){
        this.pin = pin;
        this.sim = sim;
        this.pin.addValueListener(this);        
    }
    
    public int getStartIndex(Long startTime){
        int start = timeLog.indexOf(startTime);
        if(start == -1){
            for(int i = 0; i<timeLog.size(); i++){
                if(timeLog.get(i) >= startTime){
                    start = i;
                    break;
                }
            }
        }
        if(start == -1){
            start = 0;
        }
        return start;
    }
    
    public int getEndIndex(Long endTime){
        int end = timeLog.indexOf(endTime);        
        if(end == -1){
            for(int i =timeLog.size()-1; i>=0; i--){
                if(timeLog.get(i) <= endTime){
                    end = i+1;
                    break;
                }
            }
        }
        if(end == -1){
            end = timeLog.size()-1;
        }
        if(end == timeLog.size()){
            end = timeLog.size()-1;
        }
        return end;
    }
    
    public void commitBuffersToMemory(){
        timeLog.addAll(timeBuffer);
        stateLog.addAll(stateBuffer);
        timeBuffer.clear();
        stateBuffer.clear();
    }
    
    public List<Long> getTimesBetween(Long startTime, Long endTime){  
        return timeLog.subList(getStartIndex(startTime), getEndIndex(endTime));
    } 
    
    public List<LogicState> getStatesBetween(Long startTime, Long endTime){  
        return stateLog.subList(getStartIndex(startTime), getEndIndex(endTime));
    } 
 
    public Collection<LogicState> getSavedStates(){
        return stateLog;
    }
    
    public Collection<Long> getSavedTimes(){
        return timeLog;
    }
    
    public String getName(){
        return toString();
    }
    
    public Long getStartTime(){
        return startTime;
    }
    
    public Long getEndTime(){
        return endTime;
    }

    public void valueChanged(Pin pin, LogicState value) {
        if(enabled){
            assert(timeLog.size() == stateLog.size());
            
            Long nextSimTime = sim.getSimulationTime();
            timeBuffer.add(nextSimTime);
            stateBuffer.add(value);

            if(startTime == -1l){
                startTime = new Long(nextSimTime);
            }
        }
    }
    
    public void clear(){
        timeLog.clear();
        stateLog.clear();
        startTime = -1l;
        endTime = 0l;
    }

    public void setEnabled(boolean b) {
        this.enabled = b;
    }
    
    public boolean isEnabled(){
        return enabled;
    }
    
}
