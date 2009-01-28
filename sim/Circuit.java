package sim;

import sim.componentLibrary.logicgates.AndGate2Input;
import java.util.*;
import sim.componentLibrary.*;

class Circuit implements SimItem
{
    Simulator simulator;
    String name;
    String description;
    Collection<InputPin> inputs = new ArrayList<InputPin>();
    Collection<OutputPin> outputs = new ArrayList<OutputPin>();
    Collection<SimItem> childSimItems = new ArrayList<SimItem>();

    public Circuit(Simulator simulator)
    {
        this.simulator = simulator;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Collection<InputPin> getInputs()
    {
        return inputs;
    }

    public Collection<OutputPin> getOutputs()
    {
        return outputs;
    }
    
    public void addChildSimItem(SimItem simitem)
    {
        childSimItems.add(simitem);
    }
    
    public boolean connectInput(InputPin pin)
    {
        SimItem pinOwner = pin.getOwner();
        if(childSimItems.contains(pinOwner))
        {
            this.inputs.add(pin);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public SimItem makeComponent(String componentType)
    {
        if(componentType.equals("AndGate2Input"))
        {
            return new AndGate2Input(this.simulator);
        }
        else
        {
            return null; //or throw an error or something?
        }
    }
    
    public boolean connectOutput(OutputPin pin)
    {
        SimItem pinOwner = pin.getOwner();
        if(childSimItems.contains(pinOwner))
        {
            this.outputs.add(pin);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public Circuit makeSubCircuit()
    {
        Circuit newCircuit = new Circuit(this.simulator);
        this.addChildSimItem(newCircuit);
        return newCircuit;
    }
}
