package sim.pin;

import sim.*;

public interface ValueListener {
    void valueChanged(Pin pin, LogicState value);
}