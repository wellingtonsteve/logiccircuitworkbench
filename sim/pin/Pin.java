package sim.pin;

import sim.*;
import java.util.ArrayList;

public abstract class Pin {

    private SimItem owner;
    private String name;
    private ArrayList<OutputValueListener> listeners = new ArrayList<OutputValueListener>();
    private State value = State.FLOATING;

    public Pin(SimItem owner, String name) {
        this.owner = owner;
        this.name = name;
    }

    public SimItem getOwner() {
        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public void addOutputValueListener(OutputValueListener listener) {
        this.listeners.add(listener);
    }

    public void removeOutputValueListener(OutputValueListener listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }

    public void setValue(State value) {
        this.value = value;
        for (OutputValueListener listener : this.listeners) {
            listener.outputValueChanged(value);
        }
    }

    public State getValue() {
        return this.value;
    }
}
