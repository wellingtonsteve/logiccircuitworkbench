package sim;

public class InputPin extends Pin implements OutputValueListener
{
    private OutputPin connectedTo;

    public InputPin(SimItem owner, String name)
    {
        super(owner, name);
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

    public void outputValueChanged(State value)
    {
        this.setValue (connectedTo.getValue());
    }
    
    public OutputPin getConnectedTo()
    {
        return this.connectedTo;
    }
}