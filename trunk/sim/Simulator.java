package sim;

import java.util.*;

/**
 * The Simulator class controls the simulation of a given SimItem.  When running, the simulation can
 * be paused or stopped.  When paused, the stepthrough() method causes the simulator to skip on
 * until the next event due to happen.
 *
 * We simulate the delay as a signal propagates through a component using a priority queue of
 * 'events' (See the class sim.SimItemEvent)
 */
public class Simulator {
    private SimItem simItem;
    private long currentSimulationTime;
    private CollectionPriorityQueue<Long, SimItemEvent> eventQueue = new CollectionPriorityQueue<Long, SimItemEvent>();
    private ArrayList<SimulatorStateListener> stateListeners = new ArrayList<SimulatorStateListener>();
    private SimulatorState currentState = SimulatorState.STOPPED;

    private int simulatorSpeed = 9;


    public boolean addEvent(long time, SimItemEvent event) {
        if (time > this.currentSimulationTime) {
            eventQueue.offer(time, event);
            return true;
        } else {
            return false;
        }
    }
    public Simulator(SimItem simItem) {
        this.simItem = simItem;
        simItem.setSimulator(this);
    }

    public void setSimulatorSpeed(int value) {
        if(timerRunning) stopTimer();
        simulatorSpeed = value;
        if(timerRunning) startTimer();
        for(SimulatorStateListener stateListener : stateListeners) {
            stateListener.SimulationRateChanged(value);
        }

    }
    
    public int getSimulatorSpeed(){
        return simulatorSpeed;
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
    
    public SimulatorState getCurrentState() {
        return currentState;
    }

    private Timer timer;
    private boolean timerRunning;

    private void runUntilSimTime(long time)
    {
        //System.out.println(time);
        while(time > currentSimulationTime && !eventQueue.isEmpty()){
            long nextQueueTime = eventQueue.peekK();
            if(nextQueueTime <= time){
                setSimulationTime(nextQueueTime);
                Collection<SimItemEvent> events = eventQueue.pollV();
                for(SimItemEvent event:events){
                    event.RunEvent();
                }
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
        for(SimulatorStateListener stateListener : stateListeners) {
            stateListener.SimulationTimeChanged(time);
        }
    }

    private void startTimer(){
        timer = new Timer();
        if(simulatorSpeed == 0){
            timer.schedule(new TimerTask(){
                public void run() {
                    runUntilSimTime(currentSimulationTime + 1);
                }
            }, 0, 1000);
        }
        else{
            timer.schedule(new TimerTask(){
                public void run() {
                    runUntilSimTime(currentSimulationTime + (long)(Math.pow(10,simulatorSpeed-1)));
                }
            }, 0, 100);
        }
        timerRunning = true;
    }

    private void stopTimer(){
        timer.cancel();
        timerRunning = false;
    }
    
    public void play(){
        if(currentState == SimulatorState.STOPPED){
            setSimulationTime(0);
            simItem.initialize();
            startTimer();
            setState(SimulatorState.PLAYING);
        }
        else if(currentState == SimulatorState.PAUSED){
            startTimer();
            setState(SimulatorState.PLAYING);
        }
    }

    public void pause(){
        if(currentState == SimulatorState.PLAYING){
            stopTimer();
            setState(SimulatorState.PAUSED);
        }
    }

    public void stepthrough(){
        if(currentState == SimulatorState.PAUSED && !eventQueue.isEmpty()){
            runUntilSimTime(eventQueue.peekK());
        }
    }

    public void stop(){
        if(currentState == SimulatorState.PLAYING || currentState == SimulatorState.PAUSED){
            stopTimer();
            setState(SimulatorState.STOPPED);
        }
    }
}
