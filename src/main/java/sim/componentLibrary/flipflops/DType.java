package sim.componentLibrary.flipflops;

import netlist.properties.Attribute;
import netlist.properties.AttributeListener;
import netlist.properties.Properties;
import sim.componentLibrary.Component;
import sim.*;
import sim.joinable.*;

/**
 * This class models a D Type flip flop.  The flip flop has four inputs (d, clock, set and reset)
 * and two outputs (q and notq).  When a rising edge is seen on the clock input we set q to whatever
 * value on the d pin, and notq to its negative.
 * When a rising edge is seen the set input, q and notq are set to ON and OFF respectively.
 * When a rising edge is seen the reset input, q and notq are set to OFF and ON respectively.
 */
public class DType extends Component {

    /**
     * Pin creation:
     */
    private InputPin d = createInputPin("D");
    private InputPin clock = createInputPin("Clock");
    private InputPin set = createInputPin("Set");
    private InputPin reset = createInputPin("Reset");
    private OutputPin q = createOutputPin("Q");
    private OutputPin notq = createOutputPin("NotQ");

    /**
     * In order to detect rising edges on the clock, set and reset inputs, we need to store their
     * previous states here.  Every time on of these inputs changes we examine the difference
     * betweem this stored value and the value it has changed to.  We then overwrite the stored
     * value.
     */
    private LogicState previousClockValue = LogicState.FLOATING;
    private LogicState previousSetValue = LogicState.FLOATING;
    private LogicState previousResetValue = LogicState.FLOATING;

    /**
     * Basic SimItem implementation for the component names
     */
    public String getLongName() { return "D Type Flip Flop"; }
    public String getShortName() { return "D Type"; }

    /**
     * Called when an input pin changes. The action we take here depends on whether the pin that has
     * changed is the clock, set or reset (if d has changed we do nothing - we only need to read d
     * when the clock changes).
     */
    public void valueChanged(Pin pin, LogicState value) {
        if(pin == clock){
            //If a rising edge on the clock...
            if(previousClockValue == LogicState.OFF && value == LogicState.ON && sim != null){
                //Find the current value on d. The Java type system enforces that this must be final
                final LogicState dValue = d.getValue();
                //And then add an event to update q and not q after the propagation delay
                sim.addEvent(sim.getSimulationTime() + propagationDelay, new SimItemEvent() {
                    public void RunEvent() {
                        q.setValue(dValue);
                        if(dValue == LogicState.ON) notq.setValue(LogicState.OFF);
                        else if(dValue == LogicState.OFF) notq.setValue(LogicState.ON);
                        //If d is FLOATING, then make both q and notq floating
                        else notq.setValue(LogicState.FLOATING);
                    }
                });
            }
            //Update the saved value for the clock pin
            previousClockValue = value;
        }
        else if(pin == set){
            /**
             * Do something similar for the set pin - set q and notq to ON and OFF respectively
             */
            if(previousSetValue == LogicState.OFF && value == LogicState.ON && sim != null){
                sim.addEvent(sim.getSimulationTime() + propagationDelay, new SimItemEvent() {
                    public void RunEvent() {
                        q.setValue(LogicState.ON);
                        notq.setValue(LogicState.OFF);
                    }
                });
            }
            previousSetValue = value;
        }
        else if(pin == reset){
            /**
             * And again, something similar for reset
             */
            if(previousResetValue == LogicState.OFF && value == LogicState.ON && sim != null){
                sim.addEvent(sim.getSimulationTime() + propagationDelay, new SimItemEvent() {
                    public void RunEvent() {
                        q.setValue(LogicState.OFF);
                        notq.setValue(LogicState.ON);
                    }
                });
            }
            previousResetValue = value;
        }
    }

    /**
     * Override the default initialization by setting q and notq to OFF and ON respectively
     */
    public void initialize(){
        d.setValue(LogicState.FLOATING);
        clock.setValue(LogicState.FLOATING);
        q.setValue(LogicState.OFF);
        notq.setValue(LogicState.ON);
    }

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
}