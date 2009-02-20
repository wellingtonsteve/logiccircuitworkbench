package sim.joinable;

import sim.*;
import java.util.ArrayList;

public abstract class Pin extends Joinable {

    private SimItem owner;
    private String name;
    private ArrayList<ValueListener> listeners = new ArrayList<ValueListener>();
    private LogicState value = LogicState.FLOATING;

    public Pin(SimItem owner, String name) {
        this.owner = owner;
        this.name = name;
    }

    public SimItem getOwner() { return this.owner; }

    public String getName() { return this.name; }

    public void addValueListener(ValueListener listener) {
        this.listeners.add(listener);
    }

    public void removeValueListener(ValueListener listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }

    public void setValue(LogicState value) {
        this.value = value;
        for (ValueListener listener : this.listeners) {
            listener.valueChanged(this, this.value);
        }
    }

    public LogicState getValue() { return this.value; }
    
}
