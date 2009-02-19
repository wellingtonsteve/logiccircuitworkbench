package sim.joinable;

import sim.*;

public interface ValueListener {
    void valueChanged(Pin pin, LogicState value);
}
