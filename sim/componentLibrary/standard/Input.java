package sim.componentLibrary.standard;

import netlist.properties.*;
import sim.componentLibrary.Component;
import sim.*;
import sim.joinable.*;

/**
 *
 * @author Stephen
 */
public class Input extends Component {
    
    private LogicState currentValue = LogicState.OFF;
    private String name = "";
    private boolean external = false;

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

    @Override
    public void setProperties(Properties properties) {
        name = (String) properties.getAttribute("Label").getValue();
        external = (Boolean) properties.getAttribute("External?").getValue();
    }
    
    public boolean isExternal() { return external; }
    public String getPinName() { return name; }

}
