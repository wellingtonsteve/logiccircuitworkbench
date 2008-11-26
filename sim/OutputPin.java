package sim;

import java.util.ArrayList;

public class OutputPin extends Pin
{
    private State value = State.FLOATING;
    private ArrayList<OutputValueListener> listeners = new ArrayList<OutputValueListener>();

    public OutputPin(SimItem owner, String name)
    {
        super(owner, name);
    }

    public void setValue(State value)
    {
        this.value = value;
        for (OutputValueListener listener : this.listeners)
        {
            listener.outputValueChanged();
        }
    }

    public State getValue()
    {
        return this.value;
    }

    public void addOutputValueListener(OutputValueListener listener)
    {
        this.listeners.add(listener);
    }

    public void removeOutputValueListener(OutputValueListener listener)
    {
        if (this.listeners.contains(listener))
        {
            this.listeners.remove(listener);
        }
    }
}