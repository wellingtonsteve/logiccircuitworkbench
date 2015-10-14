package sim.componentLibrary;

import sim.*;
import java.util.*;
import java.util.ArrayList;
import sim.componentLibrary.standard.Input;
import sim.componentLibrary.standard.Output;
import sim.joinable.*;

/**
 * A Circuit is an implementation of sim.SimItem, and acts a collection of other SimItems that are
 * connected to each other.
 */
public class Circuit implements SimItem {

    /**
     * A collection of the SimItems in this circuit
     */
    private ArrayList<SimItem> simItems = new ArrayList<SimItem>();

    /**
     * A reference to the Simulator that is simulating this circuit.  The circuit implements
     * SimItem's 'setSimulator' method which then passes the simulator reference on to the SimItems
     * in the circuit (i.e. all the objects in the collection above).
     */
    private Simulator sim;

    /**
     * Pins:
     * Since a circuit is an implementation of SimItem, it must be able to provide a list of its
     * Input and Output pins.  Obviously this is meaningless in a main circuit that is being run
     * directly by the simulator, but the circuit could be a subcomponent in a larger circuit (the
     * whole point of Circuit implementing SimItem).
     * Any Input component (sim.componentLibrary.standard.Input) or Output component (sim.
     * componentLibrary.standard.Output) in a circuit can be marked as external. When such a circuit
     * is added as a subcomponent of a another circuit, those external components will be treated
     * as input and output pins in the parent circuit.
     *
     * When this circuit exists as a sub component, the inputPins and outputPins maps below contain
     * the pins that will be returned by this class' implementation of getInputs() and getOutputs()
     */
    private Map<String, InputPin> inputPins = new HashMap<String, InputPin>();
    private Map<String, OutputPin> outputPins = new HashMap<String, OutputPin>();

    /**
     * Implementations of the getInputs() and getOutputs() methods, called if the circuit is a sub-
     * component in a larger circuit.  These simply return the values from the maps above.
     */
    public Collection<InputPin> getInputs() { return inputPins.values(); }
    public Collection<OutputPin> getOutputs() { return outputPins.values(); }

    /**
     * Adds a SimItem to this circuit.  We set the simulator of that SimItem to the simulator of
     * this circuit.
     * If the SimItem is an External Input or Output, either the createExternalInput or
     * createExternalInput method is called to create an InputPin or OutputPin that is added to
     * corresponding map above.
     * Note that we only check if an Input or Output is external when it is added.  So if one if
     * these is changed in the GUI to be external it will not be added to the map; similarly if one
     * is changed to not be external it will not be removed from the map.  However this is not a
     * problem - When editing a circuit directly the maps, and the getInputs/getOutputs methods are
     * not used, and when adding a circuit as a sub component the External setting of the component
     * is set programmatically by the GUI's file loader code *before* is it added to the circuit.
     */
    public boolean addSimItem(SimItem simItem){
        if(!simItems.contains(simItem)){
            simItems.add(simItem);
            simItem.setSimulator(sim);
            if(simItem instanceof Input && ((Input) simItem).isExternal()){
                createExternalInput(simItem);
            }
            if(simItem instanceof Output && ((Output) simItem).isExternal()){
                createExternalOutput(simItem);
            }
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Removes a SimItem from the circuit.  Similar arguments as above explain why we do not need to
     * check whether or not an Input or Output component that is being deleted is external
     */
    public boolean removeSimItem(SimItem simItem){
        if(simItems.contains(simItem)){
            simItems.remove(simItem);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * When adding a SimItem (in practice an Input) that is External, this function creates a 
     * InputPin that 'belongs' to the circuit as a whole.  The new pin is added to the map above.
     * We then implement a listener that is added to the pin.  When the value of the input pin
     * changes while the simulation is running, the listener implementation updates the actual Input
     * component within the sub circuit
     */
    public void createExternalInput(SimItem simItem) {
        //Create a input pin for the circuit, that passes its values onto the Input component
        InputPin connectingPin = new InputPin(this, ((Input) simItem).getPinName());
        inputPins.put(connectingPin.getName(), connectingPin);
        /**
         * The Java type system makes us create a final reference to the Input component here.
         * pinCopy and simItem are the same thing!
         */
        final Input pinCopy = (Input) simItem;
        connectingPin.addValueListener(new ValueListener() {
            //when the new pin changes, pass the value onto the input component
            public void valueChanged(Pin pin, LogicState value) {
                pinCopy.setValue(value);
            }
        });
    }

    /**
     * When adding a SimItem (in practice an Output) that is External, this function creates a
     * OutputPin that 'belongs' to the circuit as a whole.  The new pin is added to the map above.
     * 
     * In addition to this OutputPin pin, the Output component has an InputPin in the sub circuit.
     * We implement a listener that is added to that inner InputPin. When the value of that InputPin
     * changes while the simulation is running the listener implementation updates the sub-circuit's
     * OutputPin in the parent ciruit.
     */
    public void createExternalOutput(SimItem simItem) {
        //Create an output pin for the component that listens to the output component
        final OutputPin connectingPin = new OutputPin(this, ((Output) simItem).getPinName());
        outputPins.put(connectingPin.getName(), connectingPin);
        //add listener to the input pin of the output component
        simItem.getPinByName("Input").addValueListener(new ValueListener() {
            //when that pin changes, pass the value onto the new output pin
            public void valueChanged(Pin pin, LogicState value) {
                connectingPin.setValue(value);
            }
        });
    }

    /**
     * If the circuit is emptied in the GUI, clear out any SimItems that are left behind.  In
     * practice there shouldn't be any - the GUI should call removeSimItem on everything first.
     */
    public void clear(){
        simItems.clear();
    }

    /**
     * Return a given InputPin or OutputPin from the maps of External Inputs and Outputs
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
     * Basic name functions
     */
    public String getLongName() {
        return "Sub-circuit";
    }
    public String getShortName() {
        return "Sub-circuit";
    }

    /**
     * When the Simulator of the circuit is set, we need to notify all the SimItems in the circuit.
     * We also keep a copy of the Simulator so that newly added SimItems we have their setSimulator
     * called by addSimItem above
     */
    public void setSimulator(Simulator sim) {
        this.sim = sim;
        for(SimItem simItem:simItems){
            simItem.setSimulator(sim);
        }
    }

    /**
     * When initializing a circuit before the start of a simulation, just initialize all of the
     * contained SimItems
     */
    public void initialize() {
        for(SimItem simItem:simItems){
            simItem.initialize();
        }
    }

    /**
     * Implementation of interface method.  A circuit has no properties that are used by the
     * Simulator, so this methos does nothing
     */
    public void setProperties(netlist.properties.Properties properties){
    }
}
