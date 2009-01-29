package sim;

import java.util.Collection;
import sim.pin.*;

public interface SimItem {

    Collection<InputPin> getInputs();

    Collection<OutputPin> getOutputs();

    Pin getPinByName(String name);

    String getLongName();

    String getShortName();

    void setSimulator(Simulator sim);
}
