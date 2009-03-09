package sim.componentLibrary.standard;

import java.util.Random;
import netlist.properties.Attribute;
import netlist.properties.AttributeListener;
import netlist.properties.Properties;
import netlist.properties.SpinnerAttribute;
import sim.LogicState;
import sim.SimItemEvent;
import sim.componentLibrary.Component;
import sim.joinable.OutputPin;

/**
 *
 * @author Stephen
 */
public class Oscillator extends Component {

    private static Random r = new Random();

    private OutputPin output = createOutputPin("Output");
    private long t1 = 10000000l;
    private long t2 = 10000000l;
    private LogicState initialState = LogicState.OFF;
    


    private SimItemEvent on = new SimItemEvent() {
        public void RunEvent() {
            output.setValue(LogicState.ON);
            sim.addEvent(sim.getSimulationTime()+t1, off);
        }
    };
    private SimItemEvent off = new SimItemEvent() {
        public void RunEvent() {
            output.setValue(LogicState.OFF);
            sim.addEvent(sim.getSimulationTime()+t2, on);
        }
    };

    public Oscillator(){
        //t1 = 100000000l * r.nextInt(20);
        //t2 = 100000000l * r.nextInt(20);
    }

    public void initialize(){
        if(initialState == LogicState.FLOATING) initialState = LogicState.ON;
        output.setValue(initialState);
        if(initialState == LogicState.ON){
            sim.addEvent(t1, off);
        } else {
            sim.addEvent(t2, on);
        }
    }

    public String getLongName() {
        return "Oscillator";
    }

    public String getShortName() {
        return "Oscillator";
    }

    public void setProperties(Properties properties) {
        Attribute t1Att = properties.getAttribute("t1 (ns)");
        Attribute t2Att = properties.getAttribute("t2 (ns)");
        t1 = (Integer) t1Att.getValue();
        t2 = (Integer) t2Att.getValue();
        t1Att.addAttributeListener(new AttributeListener() {
            public void attributeValueChanged(Attribute attr, Object value) {
                t1 = (Integer) value;
            }
        });
        t2Att.addAttributeListener(new AttributeListener() {
            public void attributeValueChanged(Attribute attr, Object value) {
                t2 = (Integer) value;
            }
        });
    }



}
