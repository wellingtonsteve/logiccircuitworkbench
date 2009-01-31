package sim.componentLibrary;

import java.util.ArrayList;
import sim.pin.*;

/**
 * A Wire connects multiple Pins to each other.  Any number of InputPins, and at most one Output pin can be connected together
 * 
 * @author Stephen
 */
public class Wire {

    protected OutputPin valueSource;
    protected ArrayList<InputPin> valueTargets = new ArrayList<InputPin>();

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
    
    public static Wire mergeWires(Wire wire1, Wire wire2){
        Wire newWire = null;
        
        //If both Wires are already connected to an OutputPin, give up because the merged Wire can only connect to one of them.
        if(wire1.valueSource == null && wire2.valueSource == null){
            newWire = new Wire();
            //If the first Wire has an OutputPin, disconnect it and reconnect it to the new Wire.
            if(wire1.valueSource != null){
                OutputPin pin = wire1.valueSource;
                wire1.disconnectPin(pin);
                newWire.connectPin(pin);
            }
            //If the second Wire has a- OutputPin, disconnect it and reconnect it to the new Wire.
            if(wire2.valueSource != null){
                OutputPin pin = wire2.valueSource;
                wire2.disconnectPin(pin);
                newWire.connectPin(pin);
            }
            
            //Cycle through the InputPins connected to the first Wire, disconnect them and add them to the new Wire
            for(InputPin pin:wire1.valueTargets){
                wire1.disconnectPin(pin);
                newWire.connectPin(pin);
            }
            //Cycle through the InputPins connected to the second Wire, disconnect them and add them to the new Wire
            for(InputPin pin:wire2.valueTargets){
                wire2.disconnectPin(pin);
                newWire.connectPin(pin);
            }
        }
        //Return the new Wire (or null if the wires couldn't be joined)
        return newWire;
    }
}
