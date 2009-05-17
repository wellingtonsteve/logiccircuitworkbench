package sim.joinable;

import sim.*;

/**
 * Very simple interface for classes that want to listen to the value of a pin
 */
public interface ValueListener {
    /**
     * The method that classes must implement to be notified of the pin that has changed, and its
     * new value
     */
    void valueChanged(Pin pin, LogicState value);
}
