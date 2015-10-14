package sim.joinable;

import sim.*;
import java.util.ArrayList;

/**
 * Pin is an abstract sub class of Joinable and a superclass of InputPin and OutputPin.
 */
public abstract class Pin extends Joinable {

    /**
     * The SimItem that owns this pin.  Either a Component subclass, or a Circuit if this pin is
     * used to represent an external Input/Output Component from the child Circuit in the parent
     * Circuit.
     */
    private SimItem owner;

    /**
     * The name of the pin.  This is either assigned by the Component that owns this pin, by the
     * label of the external Input/Output Component that this pin represents.
     */
    private String name;

    /**
     * Accessor functions for the above two variables
     */
    public SimItem getOwner() { return this.owner; }
    public String getName() { return this.name; }

    /**
     * Constructor method to set the above variables
     */
    public Pin(SimItem owner, String name) {
        this.owner = owner;
        this.name = name;
    }

    /**
     * The pin's current value
     */
    private LogicState value = LogicState.FLOATING;

    /**
     * A list of ValueListener implementations that are listening to this pin.
     */
    private ArrayList<ValueListener> listeners = new ArrayList<ValueListener>();

    /**
     * Public method to allow ValueListener implementations to add themselves to/remove themselves
     * from the above list.
     */
    public void addValueListener(ValueListener listener) {
        this.listeners.add(listener);
    }

    public void removeValueListener(ValueListener listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }

    /**
     * Sets the pin's value.  If the value it's being set to is different from its old value, we
     * notify all listeners in the list above.
     */
    public void setValue(LogicState value) {
        if(this.value != value){
            this.value = value;
            for (ValueListener listener : this.listeners) {
                listener.valueChanged(this, this.value);
            }
        }
    }

    /**
     * Returns the current value of the pin
     */
    public LogicState getValue() { return this.value; }
    
}
