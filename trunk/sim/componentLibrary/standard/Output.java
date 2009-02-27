package sim.componentLibrary.standard;

import netlist.properties.*;
import sim.componentLibrary.Component;
import sim.joinable.*;
/**
 *
 * @author Stephen
 */
public class Output extends Component {
    private InputPin input = createInputPin("Input");
    private String name = "";
    private boolean external = false;

    public String getLongName() { return "Output"; }
    public String getShortName() { return "Output"; }

    @Override
    public void setProperties(Properties properties) {
        name = (String) properties.getAttribute("Label").getValue();
        external = (Boolean) properties.getAttribute("External?").getValue();
    }

    public boolean isEXternal() { return external; }
    public String getPinName() { return name; }
}
