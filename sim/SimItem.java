package sim;

import java.util.Collection;

public interface SimItem
{
	String getName();
	String getDescription();
	void setDescription(String description);
	Collection<Pin> getPins();
	Collection<InputPin> getInputs();
	Collection<OutputPin> getOutputs();
	void step();
}
