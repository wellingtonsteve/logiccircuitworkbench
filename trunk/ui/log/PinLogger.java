package ui.log;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import sim.State;
import sim.pin.*;
import sim.Simulator;

/**
 *
 * @author matt
 */
public class PinLogger implements ValueListener {

    private List<Long> timeLog = new LinkedList<Long>();
    private List<State> stateLog = new LinkedList<State>();
    
    private OutputPin pin; 
    private Simulator sim;
    private Long startTime;
    private Long endTime;
    // Remove this line when complete
    Stack<Long> testTimes = new Stack<Long>();
    
    public PinLogger(Simulator sim, OutputPin pin){
        this.sim = sim;
        this.pin = pin;
        pin.addValueListener(this);
        
        // Remove these lines when complete
        testTimes.push(new Long("2600"));
        testTimes.push(new Long("2521"));        
        testTimes.push(new Long("2497"));
        testTimes.push(new Long("2430"));
        testTimes.push(new Long("2390"));
        testTimes.push(new Long("2356"));
        
    }
    
    public void valueChanged(Pin pin, State value) {
        // Remove this line when complete
            Long nextSimTime = testTimes.pop();
            //Long nextSimTime = sim.getSimulationTime();
        timeLog.add(nextSimTime);
        stateLog.add(value);

        if(startTime == null){
            startTime = new Long(nextSimTime);
        }
        endTime = nextSimTime;
    }
    
    public Collection<State> getValues(){
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
}
