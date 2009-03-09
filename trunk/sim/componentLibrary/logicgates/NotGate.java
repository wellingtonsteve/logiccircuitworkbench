package sim.componentLibrary.logicgates;

import sim.componentLibrary.Component;
import sim.joinable.*;
import sim.*;

public class NotGate extends Component {
    
    //Pins
    private InputPin input = createInputPin("Input");
    private OutputPin output = createOutputPin("Output");
    
    //Properties storage
    private int propagationDelay = 5;

    //Names
    public String getLongName() { return "Not Gate"; }
    public String getShortName() { return "¬ input"; }

    //Input changed
    @Override
    public void valueChanged(Pin pin, LogicState value) {
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
