package sim.componentLibrary.latches;

import netlist.properties.Attribute;
import netlist.properties.AttributeListener;
import netlist.properties.Properties;
import sim.componentLibrary.Component;
import sim.*;
import sim.joinable.*;

/**
 * This class models a SR Latch.  The flip flop has two inputs (set and reset) and two outputs (q
 * and notq). When a rising edge is seen the set input, q and notq are set to ON and OFF
 * respectively. When a rising edge is seen the reset input, q and notq are set to OFF and ON
 * respectively.
 */
public class SRLatch extends Component {

    /**
     * Pin creation:
     */
    private InputPin set = createInputPin("Set");
    private InputPin reset = createInputPin("Reset");
    private OutputPin q = createOutputPin("Q");
    private OutputPin notq = createOutputPin("NotQ");

    /**
     * In order to detect rising edges on the set and reset inputs, we need to store their
     * previous states here.  Every time on of these inputs changes we examine the difference
     * betweem this stored value and the value it has changed to.  We then overwrite the stored
     * value.
     */
    private LogicState previousSetValue = LogicState.FLOATING;
    private LogicState previousResetValue = LogicState.FLOATING;

    /**
     * Basic SimItem implementation for the component names
     */
    public String getLongName() { return "SR Latch"; }
    public String getShortName() { return "SR Latch"; }

    /**
     * Called when an input pin changes. The action we take here depends on whether the pin that has
     * changed is the clock, set or reset (if d has changed we do nothing - we only need to read d
     * when the clock changes).
     */
    public void valueChanged(Pin pin, LogicState value) {
        if(pin == set){
            /**
             * Set q and notq to ON and OFF respectively
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
             * Set q and notq to OFF and ON respectively
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
        set.setValue(LogicState.FLOATING);
        reset.setValue(LogicState.FLOATING);
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