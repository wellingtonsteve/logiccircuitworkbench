package sim;

import java.util.*;
import sim.pin.*;

class Circuit implements SimItem {
    
    public Collection<InputPin> getInputs() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<OutputPin> getOutputs() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Pin getPinByName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getLongName() {
        return "Sub-circuit";
    }

    public String getShortName() {
        return "Sub-circuit";
    }

    public void setSimulator(Simulator sim) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
