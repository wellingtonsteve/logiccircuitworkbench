package sim.componentLibrary.standard;

import sim.componentLibrary.Component;
import sim.pin.*;
/**
 *
 * @author Stephen
 */
public class Output extends Component {
    private InputPin input = createInputPin("Input");

    public String getLongName() { return "Output"; }
    public String getShortName() { return "Output"; }
    
}
