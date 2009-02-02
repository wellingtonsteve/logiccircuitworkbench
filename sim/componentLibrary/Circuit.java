package sim.componentLibrary;

import sim.*;
import java.util.*;
import java.util.ArrayList;
import sim.pin.*;

public class Circuit implements SimItem {
    private ArrayList<SimItem> simItems = new ArrayList<SimItem>();
    private Simulator sim;
    private Map<String, InputPin> inputPins = new HashMap<String, InputPin>();
    private Map<String, OutputPin> outputPins = new HashMap<String, OutputPin>();
     
    public boolean addSimItem(SimItem simItem){
        if(!simItems.contains(simItem)){
            simItems.add(simItem);
            simItem.setSimulator(sim);
            return true;
        }
        else{
            return false;
        }
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
        for(SimItem simItem:simItems){
            simItem.setSimulator(sim);
        }
    }

    public void initialize() {
        for(SimItem simItem:simItems){
            simItem.initialize();
        }
    }
}
