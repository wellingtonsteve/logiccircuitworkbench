package sim.componentLibrary;

import java.util.ArrayList;
import sim.pin.*;

/**
 * A Wire connects multiple Pins to each other.  Any number of InputPins, and at most one Output pin can be connected together
 * 
 * @author Stephen
 */
public class Wire {

    private OutputPin valueSource;
    private ArrayList<InputPin> valueTargets = new ArrayList<InputPin>();

    public boolean connectPin(Pin pin) {
        //If attempting to connect an output pin (an input to the wire)
        if (pin instanceof OutputPin) {
            if (valueSource == null) {
                valueSource = (OutputPin) pin;
                for (InputPin target : valueTargets) {
                    target.connectToOutput(valueSource);
                }
                return true;
            } else {
                return false;
            }
        } //If attempting to connect an input pin (an output of the wire)
        else {
            if (!valueTargets.contains(pin) && ((InputPin) pin).getConnectedTo() == null) {
                valueTargets.add((InputPin) pin);
                if (valueSource != null) {
                    ((InputPin) pin).connectToOutput(valueSource);
                }
            }
            return true;
        }
    }

    public void disconnectPin(Pin pin) {
        //If attempting to disconnect an output pin (an input to the wire)
        if (pin instanceof OutputPin) {
            if (valueSource == pin) {
                valueSource = null;
                for (InputPin target : valueTargets) {
                    target.disconnect();
                }
            }
        } //If attempting to disconnect an input pin (an output of the wire)
        else {
            if (valueTargets.contains(pin)) {
                valueTargets.remove((InputPin) pin);
                if (valueSource != null) {
                    ((InputPin) pin).disconnect();
                }
            }
        }
    }
}