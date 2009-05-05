package sim.componentLibrary.standard;

import netlist.properties.*;
import sim.*;
import sim.componentLibrary.Component;
import sim.joinable.OutputPin;

/**
 * An Oscillator outputs a square wave at a frequency determined by two properties: t1 and t2.  The output of
 * the oscillator will be ON for t1 nanoseconds, then off for t2 nanoseconds.
 */
public class Oscillator extends Component {

    /**
     * Define the only pin of the oscillator - that is, its output
     */
    private OutputPin output = createOutputPin("Output");

    /**
     * Timing properties
     */
    private long t1, t2;
    
    /**
     * Simulation event objects for the oscillation
     */
    private SimItemEvent on = new SimItemEvent() {
        public void RunEvent() {
            //Set the output ON...
            output.setValue(LogicState.ON);
            //...and add the OFF event to run in t1 ns time
            sim.addEvent(sim.getSimulationTime()+t1, off);
        }
    };
    private SimItemEvent off = new SimItemEvent() {
        @Override
        public void RunEvent() {
            //Set the output OFF...
            output.setValue(LogicState.OFF);
            //...and add the ON event to run in t1 ns time
            sim.addEvent(sim.getSimulationTime()+t2, on);
        }
    };

    /**
     * Initization method.  Sets the initial output to OFF and add the next event to the event queue
     */
    @Override
    public void initialize(){
        output.setValue(LogicState.OFF);
        sim.addEvent(sim.getSimulationTime()+t2, on);
    }

    /**
     * Component names
     */
    @Override
    public String getLongName() {
        return "Oscillator";
    }
    @Override
    public String getShortName() {
        return "Oscillator";
    }

    /**
     * Called when an instance of the component is created.  The properties parameter takes a Properties
     * object. Here we add listeners to the timing properties that we are interested in.
     */
    @Override
    public void setProperties(Properties properties) {
        //Extract the t1 property from the properties object and read its current value.
        Attribute t1Att = properties.getAttribute("t1 (ns)");
        t1 = (Integer) t1Att.getValue();
        //Create a listener that updates the oscillator when the timing property is changed in the GUI
        t1Att.addAttributeListener(new AttributeListener() {
            @Override
            public void attributeValueChanged(Attribute attr, Object value) {
                t1 = (Integer) value;
            }
        });
        //Same for t2
        Attribute t2Att = properties.getAttribute("t2 (ns)");
        t2 = (Integer) t2Att.getValue();
        t2Att.addAttributeListener(new AttributeListener() {
            @Override
            public void attributeValueChanged(Attribute attr, Object value) {
                t2 = (Integer) value;
            }
        });
    }
}