package sim;

import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Queue;

public class Simulator {
    // The circuit we are going to simulate - created by the constructor. All 
    // SimItems (components, sub-circuits etc..) are added to this.
    private SimItem simItem;    // The time we are in nanoseconds in the simulation - will let us run the 
    // simulation for about 290 years (probably long enough!)
    private long simulationTime;
    private Queue<SimItemEvent> eventQueue = new PriorityQueue<SimItemEvent>();
    
    public boolean addEvent(SimItemEvent event){
        if(event.getTime() > this.simulationTime){
            return this.eventQueue.offer(event);
        }
        else{
            return false;
        }
    }
    
    //some priority queue object here
    public Simulator(SimItem simItem) {
        this.simItem = simItem;
        simItem.setSimulator(this);
    }

    public Simulator() {
        throw new Error("Don't call this!  It's here until Steve remembers to tell Matt to change his call to this constructor in ui.log.ViewWindow");
    }

    public long getSimulationTime() {
        return simulationTime;
    }
}
