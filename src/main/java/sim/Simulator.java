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

    /**
     * The SimItem that this Simulator will be simulating.  Set by the constructor function
     */
    private SimItem simItem;

    /**
     * The constructor function for the class - takes an implementation of the SimItem interface
     * that we will be simulating.  We pass a reference to this object so that the SimItem can add
     * events to event priority queue.
     */
    public Simulator(SimItem simItem) {
        this.simItem = simItem;
        simItem.setSimulator(this);
    }

    /**
     * Uses the sim.CollectionPriorityQueue class to create a priority queue of events that will
     * happen in the simulation
     */
    private CollectionPriorityQueue<Long, SimItemEvent> eventQueue = new CollectionPriorityQueue<Long, SimItemEvent>();

    /**
     * The SimItem (and in the case of a circuit, its subcomponents) add events to the above priorty
     * queue using the addEvent method, passing an implementation of the SimItemEvent interface and
     * the simulation time at which it should be run.  Obviously, events can only be added for
     * future times - we return false if the time was invalid.
     */
    public boolean addEvent(long time, SimItemEvent event) {
        if (time > this.currentSimulationTime) {
            eventQueue.offer(time, event);
            return true;
        } else {
            return false;
        }
    }

    /**
     * The SimulatorState enum provides three different states in which the simulation can be:
     * PLAYING, PAUSED and STOPPED. Naturally the inital state should be STOPPED.
     */
    private SimulatorState currentState = SimulatorState.STOPPED;

    /**
     * simulatorSpeed represents the speed at which the simulator runs.  The simulator will attempt
     * to simulate 10^simulatorSpeed nanoseconds per real second.  The default value of 9 therefore
     * corresponds to running the simulator in real time.
     */
    private int simulatorSpeed = 9;

     /**
     * The time in the simulation, nanoseconds. The maximum value of a long, 2^63-1 gives a maximum
     * simulation time of 292 years. Probably enough!
     */
    private long currentSimulationTime;

    /**
     * The above three variables together describe the state of the simulator.  Any part of the
     * program (in practice mainly the GUI) can listen for changes to any of these variables by
     * implementing the sim.SimulatorStateListener interface.  The following ArrayList stores a
     * collection of such implementations.
     */
    private ArrayList<SimulatorStateListener> stateListeners = new ArrayList<SimulatorStateListener>();

    /**
     * Simple methods to allow the GUI to add or remove listeners to/from the above list.
     */
    public void addStateListener(SimulatorStateListener listener) {
        this.stateListeners.add(listener);
    }
    public void removeStateListener(SimulatorStateListener listener) {
        if (this.stateListeners.contains(listener)) {
            this.stateListeners.remove(listener);
        }
    }

    /**
     * This method allows the GUI to set the simulation speed.  As long as the speed is within a
     * valid range (corresponding to between 1ns per real second, and 10s per real second), we stop
     * the timer (see below) and then retart it at the correct speed.
     */
    public void setSimulatorSpeed(int value) {
        if(value >= 0 && value <= 10){
            if(timerRunning){
                stopTimer();
                simulatorSpeed = value;
                startTimer();
            }
            else{
                simulatorSpeed = value;
            }
            /**
             * Finally update the listeners to let them know that the speed has changed.
             */
            for(SimulatorStateListener stateListener : stateListeners) {
                stateListener.SimulationRateChanged(value);
            }
        }
    }

    /**
     * Simple method to allow the GUI to find the current simulationSpeed
     */
    public int getSimulatorSpeed(){
        return simulatorSpeed;
    }

    /**
     * This method is used by the runUntilSimTime method to update the current simulation time, and
     * notify listeners.
     */
    private void setSimulationTime(long time){
        currentSimulationTime = time;
        for(SimulatorStateListener stateListener : stateListeners) {
            stateListener.SimulationTimeChanged(time);
        }
    }

    /**
     * As suggested by its name, this method simply returns the current time inside the simulation.
     * Used in several places by the GUI, but mainly by the component implementations to calculate
     * (using their progagation delay settings) times that events for which SimItemEvents should be
     * added to the priority queue
     */
    public long getSimulationTime() {
        return currentSimulationTime;
    }

    /**
     * This method is used by the pause, play etc.. functions to change to the state of the
     * simulator and notify listeners.
     */
    private void setState(SimulatorState state){
        currentState = state;
        for(SimulatorStateListener stateListener: stateListeners){
            stateListener.SimulatorStateChanged(state);
        }
    }

    /**
     * This method returns the current state of the simulator - used only by the Input component
     * (sim.componentLibrary.standard.Input)
     */
    public SimulatorState getCurrentState() {
        return currentState;
    }

    /**
     * Pulls events off the event queue we reach a given time
     */
    private void runUntilSimTime(long time)
    {
        /**
         * While there are evemts left on the queue and the next event (set of events) is scheduled
         * at or before the given time...
         */
        while(!eventQueue.isEmpty() && eventQueue.peekK() <= time){
            //Notify listeners that the time is changing...
            setSimulationTime(eventQueue.peekK());
            //Grab the next set of events...
            Collection<SimItemEvent> events = eventQueue.pollV();
            //...and run them
            for(SimItemEvent event:events){
                event.RunEvent();
            }
        }
        //Finally notify listeners that we have reached the time requested.
        setSimulationTime(time);
    }

    /**
     * We use a Java Timer object to control timing of the simulator.  The Timer object accepts a
     * TimerTask implementation which is run regularly at a given time interval.
     */
    private Timer timer;

    /**
     * If the timer is running when the simulation speed changes, the timer gets restarted.  This
     * variable simply keeps track of whether the timer is running.
     */
    private boolean timerRunning;

    /**
     * Starts the timer (when the simulation state changes to PLAYING or if the timer is being
     * restarted after a simulation speed change).
     */
    private void startTimer(){
        timer = new Timer();
        /**
         * If the simulation speed is 0, corresponding to simulating 1ns per real second, we set the
         * timer to advance currentSimulationTime by 1 every second (using the runUntilSimTime()
         * method)
         */
        if(simulatorSpeed == 0){
            timer.schedule(new TimerTask(){
                @Override
                public void run() {
                    runUntilSimTime(currentSimulationTime + 1);
                }
            }, 0, 1000);
        }
        /**
         * Otherwise, we have the timer run the task 10 times per second, and update
         * currentSimulationTime by an appropiate amount. i.e. 10^(simulatorSpeed-1) nanoseconds
         */
        else{
            timer.schedule(new TimerTask(){
                @Override
                public void run() {
                    runUntilSimTime(currentSimulationTime + (long)(Math.pow(10,simulatorSpeed-1)));
                }
            }, 0, 100);
        }
        timerRunning = true;
    }

    /**
     * Stops the timer, because we are stopping or pausing the simulation, or about restart it after
     * a simulation a speed change.
     */
    private void stopTimer(){
        timer.cancel();
        timerRunning = false;
    }

    /**
     * Start running the simulation from either a STOPPED or PAUSED state.  If starting from STOPPED
     * we set the simulation time to 0 and initialize the SimItem.
     */
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

    /**
     * Pause the simulation, from a PLAYING state.
     */
    public void pause(){
        if(currentState == SimulatorState.PLAYING){
            stopTimer();
            setState(SimulatorState.PAUSED);
        }
    }

    /**
     * If the simulation is PAUSED, step the simulation onto the next [set of] event[s] in the event
     * queue.  If there no more events in the queue do nothing.
     */
    public void stepthrough(){
        if(currentState == SimulatorState.PAUSED && !eventQueue.isEmpty()){
            runUntilSimTime(eventQueue.peekK());
        }
    }

    /**
     * Stop the simulatior, from a PLAYING or PAUSED state
     */
    public void stop(){
        if(currentState == SimulatorState.PLAYING || currentState == SimulatorState.PAUSED){
            stopTimer();
            setState(SimulatorState.STOPPED);
        }
    }
}
