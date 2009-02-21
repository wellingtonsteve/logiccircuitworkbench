package sim.componentLibrary.standard;

import java.util.Random;
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
    private long t1 = 100000000l;
    private long t2 = 200000000l;
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
        t1 = 100000000l * r.nextInt(20);
        t2 = 100000000l * r.nextInt(20);
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

}
