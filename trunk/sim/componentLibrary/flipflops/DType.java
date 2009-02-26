package sim.componentLibrary.flipflops;

import sim.componentLibrary.Component;
import sim.*;
import sim.joinable.*;

public class DType extends Component {

    //Pins
    private InputPin d = createInputPin("D");
    private InputPin clock = createInputPin("Clock");
    private OutputPin q = createOutputPin("Q");
    private OutputPin notq = createOutputPin("NotQ");

    private LogicState previousClockValue = LogicState.FLOATING;

    //Properties storage
    private int propagationDelay = 5;

    //Names
    public String getLongName() { return "D Type Flip Flop"; }
    public String getShortName() { return "D Type"; }

    //Input changed
    @Override
    public void valueChanged(Pin pin, LogicState value) {
        final LogicState dValue = d.getValue();
        if(pin == clock){
            if(previousClockValue == LogicState.OFF
                    && value == LogicState.ON && sim != null){
                sim.addEvent(sim.getSimulationTime() + propagationDelay, new SimItemEvent() {
                    public void RunEvent() {
                        q.setValue(dValue);
                        if(dValue == LogicState.ON) notq.setValue(LogicState.OFF);
                        else if(dValue == LogicState.OFF) notq.setValue(LogicState.ON);
                        else notq.setValue(LogicState.FLOATING);
                    }
                });
            }
            previousClockValue = value;
        }
    }

    public void initialize(){
        d.setValue(LogicState.FLOATING);
        clock.setValue(LogicState.FLOATING);
        q.setValue(LogicState.OFF);
        notq.setValue(LogicState.ON);
    }
}