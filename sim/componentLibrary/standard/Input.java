package sim.componentLibrary.standard;

import sim.componentLibrary.Component;
import sim.*;
import sim.joinable.*;

/**
 *
 * @author Stephen
 */
public class Input extends Component {
    
    private LogicState currentValue = LogicState.OFF;

    //Pins
    private OutputPin output = createOutputPin("Output");
    
    //Names
    public String getLongName() { return "Input"; }
    public String getShortName() { return "Input"; }
    
    //Set Input value
    public void setValue(LogicState value) {
        currentValue = value;
        if(sim != null && (sim.getCurrentState() == SimulatorState.PLAYING || sim.getCurrentState() == SimulatorState.PAUSED)){
            output.setValue(value);
        }
    }

    @Override
    public void initialize() {
        output.setValue(currentValue);
    }

}
