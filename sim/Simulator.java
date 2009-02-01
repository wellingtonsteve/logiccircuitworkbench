package sim;

import java.util.ArrayList;

public class Simulator {
    // The circuit we are going to simulate - created by the constructor. All 
    // SimItems (components, sub-circuits etc..) are added to this.
    private SimItem simItem;    // The time we are in nanoseconds in the simulation - will let us run the 
    // simulation for about 290 years (probably long enough!)
    private long currentSimulationTime;
    private CollectionPriorityQueue<Long, SimItemEvent> eventQueue = new CollectionPriorityQueue<Long, SimItemEvent>();
    private ArrayList<SimulatorStateListener> stateListeners = new ArrayList<SimulatorStateListener>();

    public boolean addEvent(long time, SimItemEvent event) {
        if (time > this.currentSimulationTime) {
            eventQueue.offer(time, event);
            return true;
        } else {
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
        return currentSimulationTime;
    }
    
    public void addStateListener(SimulatorStateListener listener) {
        this.stateListeners.add(listener);
    }

    public void removeStateListener(SimulatorStateListener listener) {
        if (this.stateListeners.contains(listener)) {
            this.stateListeners.remove(listener);
        }
    }
    
    
    
    //Simulator control
    
    private SimulatorState currentState;
    
    private void setState(SimulatorState state){
        currentState = state;
        for(SimulatorStateListener stateListener: stateListeners){
            stateListener.SimulatorStateChanged(state);
        }
    }
    
    private void setSimulationTime(long time){
        currentSimulationTime = time;
        for(SimulatorStateListener stateListener : stateListeners) {
            stateListener.SimulationTimeChanged(time);
        }
    }
    
    public boolean play(){
        if(currentState == SimulatorState.STOPPED){
            setSimulationTime(0);
            simItem.initialize();
            return true;
        }
    }
    public boolean pause(){
    
    }
    public boolean stepthrough(){
        
    }
    public boolean stop(){
        
    }
}
