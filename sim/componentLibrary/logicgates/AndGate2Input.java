package sim.componentLibrary.logicgates;

import sim.*;
import sim.pin.*;

public class AndGate2Input extends Component {
    private InputPin input1 = addInputPinToMap("Input 1");
    private InputPin input2 = addInputPinToMap("Input 2");
    private OutputPin output = addOutputPinToMap("Output");
    private int propagationDelay = 5;

    public void inputChanged() {
        final State value;
        if (input1.getValue() == State.ON && input2.getValue() == State.ON) {
            value = State.ON;
        } else if (input1.getValue() == State.OFF || input2.getValue() == State.OFF) {
            value = State.OFF;
        } else {
            value = State.FLOATING;
        }
        sim.addEvent(sim.getSimulationTime() + propagationDelay, new SimItemEvent() {
            public void RunEvent() {
                output.setValue(value);
            }
        });
    }

    public String getLongName() {
        return "And Gate with 2 Inputs";
    }

    public String getShortName() {
        return "& 2 input";
    }
}