package sim;

/**
 * A very simple interface for events that can be added to the Simulator's event queue.  When a
 * SimItem wants to schedule an event for the future, it creates an implementation of this
 * interface in which the RunEvent() method performs the event itself.
 */
public interface SimItemEvent {
    /**
     * This method will be called by the Simulator, when it is time for this event to execute
     */
    void RunEvent();
}
