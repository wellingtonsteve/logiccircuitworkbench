package ui.log;

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
        return pin.getName();
    }
    
    public Long getStartTime(){
        return startTime;
    }
    
    public Long getEndTime(){
        return endTime;
    }

    public void valueChanged(Pin pin, LogicState value) {
        Long nextSimTime = sim.getSimulationTime();
        timeLog.add(nextSimTime);
        stateLog.add(value);

        if(startTime == -1l){
            startTime = new Long(nextSimTime);
        }
        endTime = nextSimTime;
    }
}
