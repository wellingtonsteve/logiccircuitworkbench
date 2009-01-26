package sim;

import java.util.ArrayList;
import ui.error.ErrorHandler;
import sim.exceptions.*;

/**
 *
 * @author Stephen
 */
public class Wire {
    private OutputPin valueSource;
    private ArrayList<InputPin> valueTargets = new ArrayList<InputPin>();
    
    public boolean connectPin(Pin pin){
        //If attempting to connect an output pin (an input to the wire)
        if(pin instanceof OutputPin){
            if(valueSource == null)
            {
                valueSource = (OutputPin)pin;
                for(InputPin target : valueTargets) target.connectToOutput(valueSource);
                return true;
            }
            else return false;
        }
        //If attempting to connect an input pin (an output of the wire)
        else if(pin instanceof InputPin) {
            if(!valueTargets.contains(pin))
            {
                valueTargets.add((InputPin) pin);
            }
            return true;
        }
        //Throw error if the pin isn't of the above two
        else {
            ErrorHandler.newError("Error Title","Error Message",new InvalidPinTypeException());
            return false;
        }
    }
    
}
