/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim;

import java.util.*;
import sim.pin.*;

/**
 *
 * @author Stephen
 */
public abstract class Component implements SimItem {

    private Map<String, InputPin> inputPins = new HashMap<String, InputPin>();
    private Map<String, OutputPin> outputPins = new HashMap<String, OutputPin>();
    protected Simulator sim;

    protected OutputPin addOutputPinToMap(String name) {
        OutputPin pin = new OutputPin(this, name);
        outputPins.put(name, pin);
        return pin;
    }

    protected InputPin addInputPinToMap(String name) {
        InputPin pin = new InputPin(this, name);
        inputPins.put(name, pin);
        return pin;
    }

    public Collection<InputPin> getInputs() {
        return inputPins.values();
    }

    public Collection<OutputPin> getOutputs() {
        return outputPins.values();
    }

    public Pin getPinByName(String name) {
        if (inputPins.containsKey(name)) {
            return inputPins.get(name);
        } else if (outputPins.containsKey(name)) {
            return outputPins.get(name);
        } else {
            return null;
        }
    }

    public void setSimulator(Simulator sim) {
        this.sim = sim;
    }
}
