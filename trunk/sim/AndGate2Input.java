package sim;

import java.util.ArrayList;
import java.util.Collection;

public class AndGate2Input implements SimItem
{
	private final String name = "And Gate with 2 Inputs";
	private String description;
	private InputPin input1 = new InputPin(this, "Input 1");
	private InputPin input2 = new InputPin(this, "Input 2");
	private OutputPin output = new OutputPin(this, "Output");

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

	public void step()
	{
		if(input1.getValue() == State.ON && input2.getValue() == State.ON)
		{
			output.setValue(State.ON);
		}
		else if(input1.getValue() == State.OFF || input2.getValue() == State.OFF)
		{
			output.setValue(State.OFF);
		}
		else
		{
			output.setValue(State.FLOATING);
		}
		
	}
	
	public Collection<Pin> getPins()
	{
		ArrayList<Pin> pins = new ArrayList<Pin>();
		pins.add(this.input1);
		pins.add(this.input2);
		pins.add(this.output);
		return pins;
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
