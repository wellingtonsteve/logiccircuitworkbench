package sim;

/**
 * This enum gives the three possible logic states for any part of the simulation circuit.  ON and 
 * OFF are the obvious 1 and 0 logic levels.  FLOATING represents an unknown logic level - perhaps a
 * disconnected pin or for example the output of an AND gate whose two inputs are ON and FLOATING
 * (the actual output depends on the value of that FLOATING input is unknown itself).
 */
public enum LogicState {
    FLOATING, ON, OFF
}
