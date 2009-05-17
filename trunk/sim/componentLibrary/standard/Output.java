package sim.componentLibrary.standard;

import netlist.properties.*;
import sim.componentLibrary.Component;
import sim.joinable.*;

/**
 * The Output class corresponding to a basic logic output that appears as an LED in the GUI.
 */
public class Output extends Component{

    /**
     * The Output has one logical input pin
     */
    private InputPin input = createInputPin("Input");
    
    /**
     * Basic SimItem implementation for the component names
     */
    public String getLongName() { return "Output"; }
    public String getShortName() { return "Output"; }


    /**
     * Sub circuits:
     * When a circuit is added as subcomponent of a parent circuit, a Output component (and similarly
     * an input component) can be set to appear as an external pin of the subcircuit as it appears
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
