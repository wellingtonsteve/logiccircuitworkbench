package sim.componentLibrary;

import sim.*;
import java.util.*;
import java.util.ArrayList;
import sim.componentLibrary.standard.Input;
import sim.componentLibrary.standard.Output;
import sim.joinable.*;

public class Circuit implements SimItem {
    private ArrayList<SimItem> simItems = new ArrayList<SimItem>();
    private Simulator sim;
    private Map<String, InputPin> inputPins = new HashMap<String, InputPin>();
    private Map<String, OutputPin> outputPins = new HashMap<String, OutputPin>();
     
    public boolean addSimItem(SimItem simItem){
        if(!simItems.contains(simItem)){
            //System.out.println("Adding " + simItem.getLongName() + "(" + simItem +") to " + this);
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

    public void createExternalInput(SimItem simItem) {
        //create a input pin for the circuit, that passes its values onto the Input component
        InputPin connectingPin = new InputPin(this, ((Input) simItem).getPinName());
        inputPins.put(connectingPin.getName(), connectingPin);
        //add a listener to the new pin
        final Input pinCopy = (Input) simItem;
        connectingPin.addValueListener(new ValueListener() {

            //when the new pin changes, pass the value onto the input component
            public void valueChanged(Pin pin, LogicState value) {
                pinCopy.setValue(value);
            }
        });
    }

    public void createExternalOutput(SimItem simItem) {
        //create an output pin for the component that listens to the output component
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
         
    public boolean removeSimItem(SimItem simItem){
        if(simItems.contains(simItem)){
            simItems.remove(simItem);
            return true;
        }
        else{
            return false;
        }
    }
    
    public void clear(){
        simItems.clear();
        inputPins.clear();
        outputPins.clear();
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

    public String getLongName() {
        return "Sub-circuit";
    }

    public String getShortName() {
        return "Sub-circuit";
    }

    public void setSimulator(Simulator sim) {
        this.sim = sim;
        //System.out.println("Circuit " + this + " is using simulator " + sim);
        for(SimItem simItem:simItems){
            simItem.setSimulator(sim);
        }
    }

    public void initialize() {
        for(SimItem simItem:simItems){
            simItem.initialize();
        }
    }
    
    public void setProperties(netlist.properties.Properties properties){
    }
}
