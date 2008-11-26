package sim.componentLibrary;

import sim.*;
import java.util.*;

public class AndGate2Input implements SimItem
{
    private Simulator simulator;
    private final String name = "And Gate with 2 Inputs";
    private String description;
    private InputPin input1 = new InputPin(this, "Input 1");
    private InputPin input2 = new InputPin(this, "Input 2");
    private OutputPin output = new OutputPin(this, "Output");
    private int propagationDelay = 5;

    public AndGate2Input(Simulator simulator)
    {
        this.simulator = simulator;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getName()
    {
        return this.name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
    
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
        simulator.addOutputChange(output, simulator.getSimulationTime() + this.propagationDelay, value);
    }

    public Collection<InputPin> getInputs()
    {
        ArrayList<InputPin> inputs = new ArrayList<InputPin>();
        inputs.add(this.input1);
        inputs.add(this.input2);
        return inputs;
    }

    public Collection<OutputPin> getOutputs()
    {
        ArrayList<OutputPin> outputs = new ArrayList<OutputPin>();
        outputs.add(this.output);
        return outputs;
    }
}
