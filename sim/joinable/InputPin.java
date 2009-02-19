package sim.joinable;

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

    public void valueChanged(Pin pin, LogicState value) {
        setValue(value);
    }

    public OutputPin getConnectedTo() {
        return connectedTo;
    }
}