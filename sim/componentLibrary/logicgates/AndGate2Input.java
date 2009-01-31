package sim.componentLibrary.logicgates;

import sim.*;
import sim.pin.*;

public class AndGate2Input extends Component {
    
    //Pins
    private InputPin input1 = createInputPin("Input 1");
    private InputPin input2 = createInputPin("Input 2");
    private OutputPin output = createOutputPin("Output");
    
    //Properties storage
    private int propagationDelay = 5;

    //Names
    public String getLongName() {
        return "And Gate with 2 Inputs";
    }
    public String getShortName() {
        return "& 2 input";
    }

    //Input changed
    public void valueChanged(Pin pin, State value) {
        final State newOutputValue;
        if (input1.getValue() == State.ON && input2.getValue() == State.ON) {
            newOutputValue = State.ON;
        } else if (input1.getValue() == State.OFF || input2.getValue() == State.OFF) {
            newOutputValue = State.OFF;
        } else {
            newOutputValue = State.FLOATING;
        }
        sim.addEvent(sim.getSimulationTime() + propagationDelay, new SimItemEvent() {
            public void RunEvent() {
                output.setValue(newOutputValue);
            }
        });
    }
}