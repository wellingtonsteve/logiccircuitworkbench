package sim.joinable;

import sim.*;

/**
 * OutputPin is a subclass of Pin that represents an Output of a component. 
 */
public class OutputPin extends Pin {

     /**
     * Pass on the constructor variables to the parent.
     */
    public OutputPin(SimItem owner, String name) {
        super(owner, name);
        outputSource = this;
    }
}