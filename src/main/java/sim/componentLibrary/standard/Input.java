package sim.componentLibrary.standard;

import netlist.properties.*;
import sim.componentLibrary.Component;
import sim.*;
import sim.joinable.*;

/**
 * The Input class corresponding to a basic logic input that acts as an ON or OFF source, and
 * appears as a toggle button in the GUI.
 */
public class Input extends Component{

    /**
     * The current value of the Input component
     */
    private LogicState currentValue = LogicState.OFF;

    /**
     * The Input has one logical output pin that feeds anything that is joined to that output.
     */
    private OutputPin output = createOutputPin("Output");
    
    /**
     * Basic SimItem implementation for the component names
     */
    public String getLongName() { return "Input"; }
    public String getShortName() { return "Input"; }
    
    /**
     * A method that allows the value of the Input to be set
     */
    public void setValue(LogicState value) {
        currentValue = value;
        /**
         * The output pin is only changed if the simulation currently running.  This is to prevent
         * the appearance that values are being propagated when the simulation is supposed to be
         * stopped.
         */
        if(sim != null && (sim.getCurrentState() == SimulatorState.PLAYING || sim.getCurrentState() == SimulatorState.PAUSED)){
            output.setValue(value);
        }
    }

    /**
     * When the simulations starts, override the default initialization method and set the output
     * pin to the value of the component itself.
     */
    public void initialize() {
        output.setValue(currentValue);
    }


    /**
     * Sub circuits:
     * When a circuit is added as subcomponent of a parent circuit, a Input component (and similarly
     * an output component) can be set to appear as an external pin of the subcircuit as it appears
     * in the parent circuit
     */

    /**
     * When a circuit is added to a parent circuit, it (the child circuit) creates logical pins for
     * Input and Output components that are external.  The following two methods are used by the
     * Circuit class to query which of its Inputs and Outputs are external, and to get a name that
     * will be used to identify the logical pins that are created.
     */
    public String getPinName() { return name; }
    public boolean isExternal() { return external; }

    /**
     * Storage variables for the above two methods
     */
    private String name = "";
    private boolean external = false;

    /**
     * The name and external visibility are set properties that appear in the GUI.  The following
     * method creates listeners that listeners for changes to the two properties and update the
     * variables above.
     */
    public void setProperties(Properties properties) {
        name = (String) properties.getAttribute("Label").getValue();
        properties.getAttribute("Label").addAttributeListener(new AttributeListener() {
            @Override
            public void attributeValueChanged(Attribute attr, Object value) {
                name = (String) value;
            }
        });

        external = (Boolean) properties.getAttribute("External?").getValue();
        properties.getAttribute("External?").addAttributeListener(new AttributeListener() {
            @Override
            public void attributeValueChanged(Attribute attr, Object value) {
                external = (Boolean) value;
            }
        });
    }

}
