package ui.components.standard.log;

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
    private Long firstTime = Long.MAX_VALUE;
    private Long lastTime = 0l;
    private Simulator sim;
    private boolean enabled = true;
    private String name;
    
    public PinLogger(ui.components.SelectableComponent.Pin pin, String name){
        this.pin = (OutputPin) pin.getParent().getLogicalComponent().getPinByName("Output");
        this.sim = pin.getParent().getParent().getSimulator();
        this.pin.addValueListener(this);        
        this.name = name;
    }
    
    public PinLogger(sim.pin.Pin pin, Simulator sim, String name){
        this.pin = pin;
        this.sim = sim;
        this.pin.addValueListener(this);        
        this.name = name;
    }
    
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
    
    public String getName(){
        return name;
    }
    
    public Long getStartTime(){
        return firstTime;
    }
    
    public Long getEndTime(){
        return lastTime;
    }

    public void valueChanged(Pin pin, LogicState value) {
        assert(timeLog.size() == stateLog.size());

        Long nextSimTime = sim.getSimulationTime();
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
