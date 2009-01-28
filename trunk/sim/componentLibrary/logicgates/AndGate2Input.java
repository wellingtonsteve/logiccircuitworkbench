package sim.componentLibrary.logicgates;

import sim.*;

public class AndGate2Input extends Component
{
    private InputPin input1 = new InputPin(this, "Input 1");
    private InputPin input2 = new InputPin(this, "Input 2");
    private OutputPin output = new OutputPin(this, "Output");
    private int propagationDelay = 5;

    public void inputChanged()
    {
        State value;
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
        //simulator.addOutputChange(output, simulator.getSimulationTime() + this.propagationDelay, value);
    }

    public String getLongName(){
        return "And Gate with 2 Inputs";
    }

    public String getShortName()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
