package sim;

import java.util.Collection;
import netlist.properties.Properties;
import sim.joinable.*;

public interface SimItem {

    Collection<InputPin> getInputs();
    Collection<OutputPin> getOutputs();
    Pin getPinByName(String name);
    String getLongName();
    String getShortName();
    void setSimulator(Simulator sim);
    void initialize();
    public void setProperties(Properties properties);
}
