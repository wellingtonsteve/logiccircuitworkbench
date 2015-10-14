package sim.componentLibrary;

import sim.*;
import java.util.*;
import sim.joinable.*;

/**
 * Component is an abstract class that forms the basis of all main components in
 * sim.componentLibrary.  It implements SimItem, so all sub-classes of Component can be added to a
 * circuit.
 * Component also implements ValueListener.  ValueListener is an interface used to allow parts of
 * the program to listen for changes to the value of an Input or Output pin.  Since a component will
 * need to be able to respond to changes to any of its inputs, the most efficent way to do this is
 * to make the component itself a ValueListener of its own Input pins.
 */
public abstract class Component implements SimItem, ValueListener {
    
    /**
     * The simulator that is running this component.
     */
    protected Simulator sim;
    /**
     * Sets the simulator reference above.  Called ultimately by the Simulator when this component
     * or its highest ancestor circuit is passed to the simulator's constructor
     */
    public void setSimulator(Simulator sim) { this.sim = sim;}

    /**
     * Maps containing the input and output pins of this component.  Populated by the
     * createOutputPin and createInputPin methods below.
     */
    private Map<String, InputPin> inputPins = new HashMap<String, InputPin>();
    private Map<String, OutputPin> outputPins = new HashMap<String, OutputPin>();

    /**
     * Component subclasses use this and the next method to create pins for themselves.
     */
    protected OutputPin createOutputPin(String name) {
        //A pin object is created using the name provided.
        OutputPin pin = new OutputPin(this, name);
        //The pin added to the map, using the name as the key.
        outputPins.put(name, pin);
        //We then return a reference to the newly created pin to the subclass that created it.
        return pin;
    }
    /**
     * Creating an InputPin is identical to above, except that we also add the component as a
     * listener of the new pin, allowing the component to react to changes to these inputs.
     */
    protected InputPin createInputPin(String name) {
        InputPin pin = new InputPin(this, name);
        inputPins.put(name, pin);
        pin.addValueListener(this);
        return pin;
    }

    /**
     * Simple method implementations for retrieving lists of the pins on a component
     */
    public Collection<InputPin> getInputs() { return inputPins.values(); }
    public Collection<OutputPin> getOutputs() { return outputPins.values(); }

    /**
     * Method implementation for finding a pin with a give name.
     */
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

    /**
     * As part of the Components implementation of ValueListener, this method is called when a
     * component's input pins changes.  This implementation does nothing, component subclasses that
     * want to listen to their inputs (i.e. anything that actually has am input!) should override
     * this.
     */
    public void valueChanged(Pin pin, LogicState value) { }

    /**
     * A basic initiazation method for all components, called when a simulation is started. All
     * output pins are set to FLOATING, followed by any unconnected input pins.
     */
    public void initialize(){
        for(OutputPin pin: outputPins.values()){
            pin.setValue(LogicState.FLOATING);
        }
        for(InputPin pin: inputPins.values()){
            if(pin.getConnectedTo() == null) pin.setValue(LogicState.FLOATING);
        }
    }

    /**
     * The netlist code defines properties for each type of component that can be changed in the GUI
     * This method allows a component subclass to explore these properties and listen to those that
     * are relevant to the simulation (Some properties are purely GUI related).  Subclasses that
     * wish to do this should override this method.
     */
    public void setProperties(netlist.properties.Properties properties){
    }
}
