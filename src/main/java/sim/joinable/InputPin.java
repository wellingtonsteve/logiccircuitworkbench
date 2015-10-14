package sim.joinable;

import java.util.ArrayList;
import sim.*;

/**
 * InputPin is a subclass of Pin that represents an Input into a component.  It implements
 * ValueListener so that it can listen to changes to the connected OutputPin
 */
public class InputPin extends Pin implements ValueListener {

    /**
     * A reference to the OutputPin to which this InputPin is listening.  A copy of superclass's
     * outputSource reference.  We keep this copy so that when setOutputSource sets outputSource to
     * null (if the outputSource is disconnected), we can remove this InputPin as a listener of the
     * outputSource.
     */
    private OutputPin connectedTo;

    /**
     * Pass on the constructor variables to the parent.
     */
    public InputPin(SimItem owner, String name) {
        super(owner, name);
    }

    /**
     * We implement ValueListener, so when the connected OutputPin changes, update the value of this
     * pin here.
     */
    public void valueChanged(Pin pin, LogicState value) {
        setValue(value);
    }

    /**
     * Return a reference to the connected OutputPin.  Used by the default initialization method in
     * Component - when a simulation starts, unconnected Inputs have a value of FLOATING
     */
    public OutputPin getConnectedTo() {
        return connectedTo;
    }


    /**
     * Override of Joinable's setOutputSource method.  When the output source is set to an actual
     * non-null OutputPin, we add this InputPin as a ValueListener of it.  If it's set to null we
     * remove this InputPin as a listener of the old OutputPin
     */
    public void setOutputSource(OutputPin joinable, ArrayList<Joinable> visited){
        super.setOutputSource(joinable, visited);
        if(joinable != null){
            connectedTo = joinable;
            connectedTo.addValueListener(this);
            setValue(connectedTo.getValue());
        } else{
            if(connectedTo != null){
                connectedTo.removeValueListener(this);
                connectedTo = null;
            } 
            setValue(LogicState.FLOATING);
        }
    }
}