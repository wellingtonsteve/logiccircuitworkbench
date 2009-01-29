package sim.pin;

import sim.*;

public class InputPin extends Pin implements OutputValueListener {

    private OutputPin connectedTo;

    public InputPin(SimItem owner, String name) {
        super(owner, name);
    }

    public void connectToOutput(OutputPin output) {
        if (connectedTo != null) {
            disconnect();
        }
        connectedTo = output;
        output.addOutputValueListener(this);
    }

    public void disconnect() {
        if (connectedTo != null) {
            connectedTo.removeOutputValueListener(this);
            connectedTo = null;
        }
    }

    public void outputValueChanged(State value) {
        setValue(connectedTo.getValue());
    }

    public OutputPin getConnectedTo() {
        return connectedTo;
    }
}