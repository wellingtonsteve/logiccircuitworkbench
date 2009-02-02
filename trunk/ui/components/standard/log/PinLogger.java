package ui.components.standard.log;

import java.util.Collection;
import java.util.LinkedList;
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
 
    public Collection<LogicState> getValues(){
        return stateLog;
    }
    
    public Collection<Long> getKeys(){
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
            Long nextSimTime = sim.getSimulationTime();
            timeLog.add(nextSimTime);
            stateLog.add(value);

            if(startTime == -1l){
                startTime = new Long(nextSimTime);
            }
            endTime = nextSimTime;
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
