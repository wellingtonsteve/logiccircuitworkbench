package sim;

import java.util.Collection;

public interface SimItem
{

    String getName();

    String getDescription();

    void setDescription(String description);

    Collection<InputPin> getInputs();

    Collection<OutputPin> getOutputs();
}
