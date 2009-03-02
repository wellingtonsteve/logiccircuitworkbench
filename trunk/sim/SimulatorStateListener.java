package sim;

/**
 *
 * @author Stephen
 */
public interface SimulatorStateListener {
    void SimulatorStateChanged(SimulatorState state);
    void SimulationTimeChanged(long time);
    void SimulationRateChanged(int rate);
}
