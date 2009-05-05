package sim;

/**
 * An interface implenented by any part of the program that listening to the state of the simulator
 * (in practice, the various parts of the GUI).
 * SimulatorStateChanged() is called when the Simulator starts running, is paused, or is stopped. A
 * SimulatorState value describes which state we have changed to.
 * SimulationTimeChanged() is called when the time changes in the simulation.
 * SimulationRateChanged() is called when the at which the simulation is running changes.
 */
public interface SimulatorStateListener {
    void SimulatorStateChanged(SimulatorState state);
    void SimulationTimeChanged(long time);
    void SimulationRateChanged(int rate);
}
