package sim.componentLibrary.logicgates;

import sim.componentLibrary.Component;
import sim.joinable.*;
import sim.*;
import netlist.properties.*;

public class XorGate2Input extends Component {
    
    /**
     * Pin creation
     */
    private InputPin input1 = createInputPin("Input 1");
    private InputPin input2 = createInputPin("Input 2");
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
    public String getLongName() { return "Xor Gate "; }
    public String getShortName() { return "Xor"; }

    /**
     * ValueListener method implementation to update output when an input changes
     */
    @Override
    public void valueChanged(Pin pin, LogicState value) {
        //Java type system makes us declare the new output value as final because we'll be using in
        //the anonymous SimItemEvent implementation below
        final LogicState newOutputValue;
        //If either input is FLOATING, make the output FLOATING too.
        if (input1.getValue() == LogicState.FLOATING || input2.getValue() == LogicState.FLOATING){
            newOutputValue = LogicState.FLOATING;
        } else if (input1.getValue() == input2.getValue()) {
            //Otherwise standard XOR logic..
            newOutputValue = LogicState.OFF;
        } else {
            //Otherwise standard XOR logic..
            newOutputValue = LogicState.ON;
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
