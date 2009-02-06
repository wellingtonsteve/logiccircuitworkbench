package sim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

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
    
    private SimulatorState currentState = SimulatorState.STOPPED;
    private Timer timer;
    private Simulator thisPtr = this;

    private void runUntilSimTime(long time)
    {
        //System.out.println(time);
        while(time > currentSimulationTime && !eventQueue.isEmpty()){
            long nextQueueTime = eventQueue.peekK();
            if(nextQueueTime <= time){
                Collection<SimItemEvent> events = eventQueue.pollV();
                for(SimItemEvent event:events){
                    event.RunEvent();
                }
                setSimulationTime(nextQueueTime);
            }
            else break;
        }
        setSimulationTime(time);
    }
    
    private void setState(SimulatorState state){
        currentState = state;
        for(SimulatorStateListener stateListener: stateListeners){
            stateListener.SimulatorStateChanged(state);
        }
    }
    
    private void setSimulationTime(long time){
        currentSimulationTime = time;
        System.out.println(time);
        for(SimulatorStateListener stateListener : stateListeners) {
            stateListener.SimulationTimeChanged(time);
        }
    }
    
    public boolean play(){
        if(currentState == SimulatorState.STOPPED){
            setSimulationTime(0);
            simItem.initialize();
            timer = new Timer();
            timer.schedule(new TimerTask(){
                public void run() {
                    //System.out.println(thisPtr.currentSimulationTime+100000000);
                    thisPtr.runUntilSimTime(thisPtr.currentSimulationTime+100000000);
                }
            }, 0, 100);
            setState(SimulatorState.PLAYING);
            return true;
        }
        else if(currentState == SimulatorState.PAUSED){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean pause(){
        return false;
    }
    public boolean stepthrough(){
        return false;
    }
    public boolean stop(){
        if(currentState == SimulatorState.PLAYING || currentState == SimulatorState.PAUSED){
            timer.cancel();
            setState(SimulatorState.STOPPED);
            return true;
        }
        else{
            return false;
        }
    }
}
