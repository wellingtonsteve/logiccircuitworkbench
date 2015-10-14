package sim.componentLibrary.flipflops;

import netlist.properties.Attribute;
import netlist.properties.AttributeListener;
import netlist.properties.Properties;
import sim.componentLibrary.Component;
import sim.*;
import sim.joinable.*;

/**
 * This class models a JK Type flip flop.  The flip flop has five inputs (j, k, clock, set and reset)
 * and two outputs (q and notq).  When a rising edge is seen on the clock input we set q as follows:
 * J K Qnext
 * 0 0 Qprev
 * 0 1 reset
 * 1 0 set
 * 1 1 toggle
 * When a rising edge is seen the set input, q and notq are set to ON and OFF respectively.
 * When a rising edge is seen the reset input, q and notq are set to OFF and ON respectively.
 */
public class JKType extends Component {

    /**
     * Pin creation:
     */
    private InputPin j = createInputPin("J");
    private InputPin k = createInputPin("K");
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
    public String getLongName() { return "JK Type Flip Flop"; }
    public String getShortName() { return "JK Type"; }

    /**
     * Called when an input pin changes. The action we take here depends on whether the pin that has
     * changed is the clock, set or reset (if d has changed we do nothing - we only need to read d
     * when the clock changes).
     */
    public void valueChanged(Pin pin, LogicState value) {
        if(pin == clock){
            //If a rising edge on the clock...
            if(previousClockValue == LogicState.OFF && value == LogicState.ON && sim != null){
                //Set next value of Q. The Java type system enforces that this must be final
                final LogicState qValue;
                if(j.getValue() == LogicState.OFF && k.getValue() == LogicState.OFF) {
                    qValue = q.getValue();
                } else if(j.getValue() == LogicState.OFF && k.getValue() == LogicState.ON) {
                    qValue = LogicState.OFF;
                } else if(j.getValue() == LogicState.ON && k.getValue() == LogicState.OFF) {
                    qValue = LogicState.ON;
                } else if(j.getValue() == LogicState.ON && k.getValue() == LogicState.ON) {
                    qValue = notq.getValue();
                } else {
                    qValue = LogicState.FLOATING;
                }
                //And then add an event to update q and not q after the propagation delay
                sim.addEvent(sim.getSimulationTime() + propagationDelay, new SimItemEvent() {
                    public void RunEvent() {
                        q.setValue(qValue);
                        if(qValue == LogicState.ON) notq.setValue(LogicState.OFF);
                        else if(qValue == LogicState.OFF) notq.setValue(LogicState.ON);
                        //If q is FLOATING, then make both q and notq floating
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
        j.setValue(LogicState.FLOATING);
        k.setValue(LogicState.FLOATING);
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