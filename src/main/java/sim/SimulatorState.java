package sim;

/**
 * The possible states of the Simulator.  The GUI contains standard play/pause/stop/skip buttons for
 * controlling the Simulator.  This enum is used the Simulator to store the state it is currently in
 * and to share this information with an implementations of sim.SimulatorStateListener.
 */
public enum SimulatorState {
    PLAYING, PAUSED, STOPPED
}
