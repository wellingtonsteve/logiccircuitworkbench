package sim.componentLibrary.logicgates;

import sim.componentLibrary.Component;
import sim.joinable.*;
import sim.*;

public class XorGate2Input extends Component {
    
    //Pins
    private InputPin input1 = createInputPin("Input 1");
    private InputPin input2 = createInputPin("Input 2");
    private OutputPin output = createOutputPin("Output");
    
    //Properties storage
    private int propagationDelay = 5;

    //Names
    public String getLongName() { return "Xor Gate "; }
    public String getShortName() { return "Xor"; }

    //Input changed
    @Override
    public void valueChanged(Pin pin, LogicState value) {
        final LogicState newOutputValue;
        if (input1.getValue() == LogicState.FLOATING || input2.getValue() == LogicState.FLOATING){
            newOutputValue = LogicState.OFF;
        } else if (input1.getValue() == input2.getValue()) {
            newOutputValue = LogicState.OFF;
        } else {
            newOutputValue = LogicState.ON;
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
