package sim.componentLibrary.standard;

import sim.*;
import sim.pin.*;

/**
 *
 * @author Stephen
 */
public class Input extends Component {

    //Pins
    private OutputPin output = createOutputPin("Output");
    
    //Names
    public String getLongName() { return "Input"; }
    public String getShortName() { return "Input"; }
    
    //Set Input value
    public void setValue(LogicState value) { output.setValue(value); }
}
