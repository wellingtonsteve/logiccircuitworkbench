package sim;

public class InputPin extends Pin implements OutputValueListener
{
    private State value = State.FLOATING;
    private OutputPin connectedTo;

    public InputPin(SimItem owner, String name)
    {
        super(owner, name);
    }

    public void setValue(State value) throws Exception
    {
        if (this.connectedTo != null)
        {
            this.value = value;
        }
        else
        {
            throw new Exception("Can set value when connected to an output");
        }
    }

    public State getValue()
    {
        return this.value;
    }

    public void connectToOutput(OutputPin output)
    {
        if (this.connectedTo != null)
        {
            this.disconnect();
        }
        this.connectedTo = output;
        output.addOutputValueListener(this);
    }

    public void disconnect()
    {
        if (this.connectedTo != null)
        {
            this.connectedTo.removeOutputValueListener(this);
        }
    }

    public void outputValueChanged()
    {
        this.value = connectedTo.getValue();
    }
}