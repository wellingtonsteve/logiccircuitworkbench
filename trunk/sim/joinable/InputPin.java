package sim.joinable;

import java.util.ArrayList;
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

    @Override
    public void setOutputSource(OutputPin joinable, ArrayList<Joinable> visited){
        super.setOutputSource(joinable, visited);
        if(joinable != null){
            connectToOutput(joinable);
            setValue(joinable.getValue());
        } else{
            disconnect();
            setValue(LogicState.FLOATING);
        }
        
    }
}