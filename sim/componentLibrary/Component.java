package sim.componentLibrary;

import sim.*;
import java.util.*;
import sim.joinable.*;

/**
 *
 * @author Stephen
 */
public abstract class Component implements SimItem, ValueListener {
    private Map<String, InputPin> inputPins = new HashMap<String, InputPin>();
    private Map<String, OutputPin> outputPins = new HashMap<String, OutputPin>();
    protected Simulator sim;

    protected OutputPin createOutputPin(String name) {
        OutputPin pin = new OutputPin(this, name);
        outputPins.put(name, pin);
        return pin;
    }

    protected InputPin createInputPin(String name) {
        InputPin pin = new InputPin(this, name);
        inputPins.put(name, pin);
        pin.addValueListener(this);
        return pin;
    }

    public Collection<InputPin> getInputs() { return inputPins.values(); }

    public Collection<OutputPin> getOutputs() { return outputPins.values(); }

    public Pin getPinByName(String name) {
        if (inputPins.containsKey(name)) {
            return inputPins.get(name);
        }
        else if (outputPins.containsKey(name)) {
            return outputPins.get(name);
        }
        else {
            return null;
        }
    }

    public void setSimulator(Simulator sim) { this.sim = sim; }
    
    //Input pin changed.
    public void valueChanged(Pin pin, LogicState value) { }
    
    public void initialize(){
        for(OutputPin pin: outputPins.values()){
            pin.setValue(LogicState.FLOATING);
        }
        for(InputPin pin: inputPins.values()){
            if(pin.getConnectedTo() == null) pin.setValue(LogicState.FLOATING);
        }
    }
}
