package sim.componentLibrary.logicgates;

import sim.componentLibrary.Component;
import sim.joinable.*;
import sim.*;
import netlist.properties.*;

public class NotGate extends Component {
    
    /**
     * Pin creation
     */
    private InputPin input = createInputPin("Input");
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
    public String getLongName() { return "Not Gate"; }
    public String getShortName() { return "¬ input"; }

    /**
     * ValueListener method implementation to update output when an input changes
     */
    @Override
    public void valueChanged(Pin pin, LogicState value) {
        //Java type system makes us declare the new output value as final because we'll be using in
        //the anonymous SimItemEvent implementation below
        final LogicState newOutputValue;
        if (input.getValue() == LogicState.ON){
            newOutputValue = LogicState.OFF;
        } else if (input.getValue() == LogicState.OFF) {
            newOutputValue = LogicState.ON;
        } else {
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
