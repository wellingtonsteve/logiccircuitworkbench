package sim;

import java.util.Collection;

public interface SimItem
{
    Collection<InputPin> getInputs();
    Collection<OutputPin> getOutputs();
}
