package sim;

import java.util.Collection;
import netlist.properties.Properties;
import sim.joinable.*;

/**
 * A SimItem is anything that can be simulated by the sim.Simulator class.  This interface defines
 * the expected functionality of all components, and for the purpose of sub-components, a circuit
 * itself.
 */
public interface SimItem {
    /**
     * InputPin and OutputPin are the (only) two classes that inherit from the abstract Pin class
     * (See sim.joinable.Pin).  All SimItems have a collection of these Pins - the following two
     * methods returns Collections of the input and output pins respectively of this SimItem.
     */
    Collection<InputPin> getInputs();
    Collection<OutputPin> getOutputs();

    /**
     * All Pins in a SimItem have a name assigned to them.  SimItems implement the following method
     * to return the Pin mapped to a particular name.
     */
    Pin getPinByName(String name);

    /**
     * Simple methods for displaying names of SimItems in the GUI
     */
    String getLongName();
    String getShortName();

    /**
     * When a SimItem is simulated, the Simulator passes itself to the setSimulator() method.  This
     * gives the SimiItem indirect access to the Simulator event queue, allowing it to schedule
     * events to happen at a future time in the simulation.
     */
    void setSimulator(Simulator sim);

    /**
     * This method is used by the Simulator to allow the SimItem to do any inititalisation it may
     * need to do before a simulation starts (or restarts - this is also called each time the
     * simulation is stopped and restarted).  In most components this just sets all pins to FLOATING
     * (in anticipation that pins that are actually connected to something will have values
     * propagated to them as the simulation starts running)
     */
    void initialize();

    /**
     * The netlist definition of each component describes a set of properties associated with that
     * component that appear in the GUI.  These properties may be visual (e.g. the colour of an LED
     * output component) or related to the actual simulation of that component (e.g. the propagation
     * delay within an AND gate).  When an instance of a component is created, the netlist code that
     * creates it, passes it a Properties object from which those properties that are relevant to
     * the simulation can be extracted (and listeners set up that respond to changes to the
     * properties in the GUI - See netlist.properties package).
     */
    public void setProperties(Properties properties);
}
