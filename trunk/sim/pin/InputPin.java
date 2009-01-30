package sim.pin;

import sim.*;

public class InputPin extends Pin implements ValueListener {

    private OutputPin connectedTo;

    public InputPin(SimItem owner, String name) {
        super(owner, name);
    }

    public void connectToOutput(OutputPin output) {
        if (connectedTo != null) {
            disconnect();
        }
        connectedTo = output;
        output.addValueListener(this);
    }

    public void disconnect() {
        if (connectedTo != null) {
            connectedTo.removeValueListener(this);
            connectedTo = null;
        }
    }

    public void valueChanged(State value) {
        setValue(connectedTo.getValue());
    }

    public OutputPin getConnectedTo() {
        return connectedTo;
    }
}