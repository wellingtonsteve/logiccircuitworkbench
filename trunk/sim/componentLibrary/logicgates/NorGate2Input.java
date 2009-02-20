package sim.componentLibrary.logicgates;

import sim.componentLibrary.Component;
import sim.*;
import sim.joinable.*;

public class NorGate2Input extends Component {

    //Pins
    private InputPin input1 = createInputPin("Input 1");
    private InputPin input2 = createInputPin("Input 2");
    private OutputPin output = createOutputPin("Output");

    //Properties storage
    private int propagationDelay = 5;

    //Names
    public String getLongName() { return "Nor Gate with 2 Inputs"; }
    public String getShortName() { return "Nor 2 input"; }

    //Input changed
    @Override
    public void valueChanged(Pin pin, LogicState value) {
        final LogicState newOutputValue;
        if (input1.getValue() == LogicState.ON || input2.getValue() == LogicState.ON){
            newOutputValue = LogicState.OFF;
        } else if (input1.getValue() == LogicState.OFF && input2.getValue() == LogicState.OFF) {
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