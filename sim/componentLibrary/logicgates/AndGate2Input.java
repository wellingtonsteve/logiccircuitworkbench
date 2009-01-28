package sim.componentLibrary.logicgates;

import sim.*;

public class AndGate2Input extends Component
{
    private InputPin input1 = new InputPin(this, "Input 1");
    private InputPin input2 = new InputPin(this, "Input 2");
    private OutputPin output = new OutputPin(this, "Output");
    private int propagationDelay = 5;

    public AndGate2Input(){
        this.addPinToMap(input1);
        this.addPinToMap(input2);
        this.addPinToMap(output);
    }
    
    public void inputChanged()
    {
        final State value;
        if(input1.getValue() == State.ON && input2.getValue() == State.ON)
        {
            value = State.ON;
        }
        else if(input1.getValue() == State.OFF || input2.getValue() == State.OFF)
        {
            value = State.OFF;
        }
        else
        {
            value = State.FLOATING;
        }
        
        sim.addEvent(sim.getSimulationTime() + propagationDelay,new SimItemEvent() {
            public void RunEvent() {
                output.setValue(value);
            }
        });
    }

    public String getLongName(){
        return "And Gate with 2 Inputs";
    }

    public String getShortName()
    {
        return "& 2 input";
    }
}
