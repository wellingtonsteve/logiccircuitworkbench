package sim.componentLibrary.logicgates;

import netlist.properties.*;
import sim.componentLibrary.Component;
import sim.joinable.*;
import sim.*;

public class OrGate3Input extends Component {

    /**
     * Pin creation
     */
    private InputPin input1 = createInputPin("Input 1");
    private InputPin input2 = createInputPin("Input 2");
    private InputPin input3 = createInputPin("Input 3");
    private OutputPin output = createOutputPin("Output");

    /**
     * The propagation delay off the component, as set by the property in the GUI
     */
    private int propagationDelay;
    /**
     * Called when an instance of the component is created.  The properties parameter takes a
     * Properties object. Here we add a listener to the propagation delay property.
     */
    @Override
    public void setProperties(Properties properties) {
        //Extract the propagation delay property from the properties object and read its current
        //value.
        Attribute pdAtt = properties.getAttribute("Propagation delay (ns)");
        propagationDelay = (Integer) pdAtt.getValue();
        //Create a listener that updates the propagation delay when changed in the GUI
        pdAtt.addAttributeListener(new AttributeListener() {
            @Override
            public void attributeValueChanged(Attribute attr, Object value) {
                propagationDelay = (Integer) value;
            }
        });
    }

    /**
     * Basic SimItem implementation for the component names
     */
    public String getLongName() { return "Or Gate with 3 Inputs"; }
    public String getShortName() { return "|| 3 input"; }

    /**
     * ValueListener method implementation to update output when an input changes
     */
    @Override
    public void valueChanged(Pin pin, LogicState value) {
        //Java type system makes us declare the new output value as final because we'll be using in
        //the anonymous SimItemEvent implementation below
        final LogicState newOutputValue;
        if (input1.getValue() == LogicState.OFF && input2.getValue() == LogicState.OFF && input3.getValue() == LogicState.OFF){
            //If all inputs are OFF, output OFF
            newOutputValue = LogicState.OFF;
        } else if (input1.getValue() == LogicState.ON || input2.getValue() == LogicState.ON || input3.getValue() == LogicState.ON) {
            //If any input is ON, ouput ON
            newOutputValue = LogicState.ON;
        } else {
            //Otherwise standard XOR logic..
            newOutputValue = LogicState.FLOATING;
        }
        if(sim != null){
            sim.addEvent(sim.getSimulationTime() + propagationDelay, new SimItemEvent() {
                public void RunEvent() {
                    output.setValue(newOutputValue);
                }
            });
        }
    }
}
