package sim.componentLibrary.standard;

import netlist.properties.*;
import sim.componentLibrary.Component;
import sim.*;
import sim.joinable.*;

/**
 *
 * @author Stephen
 */
public class Input extends Component implements AttributeListener{
    
    private LogicState currentValue = LogicState.OFF;
    private String name = "";
    private boolean external = false;

    //Pins
    private OutputPin output = createOutputPin("Output");
    
    //Names
    public String getLongName() { return "Input"; }
    public String getShortName() { return "Input"; }
    
    //Set Input value
    public void setValue(LogicState value) {
        currentValue = value;
        //System.out.println("INPUT COMPONENT " + name + " CHANGED TO " + value);
        if(sim != null && (sim.getCurrentState() == SimulatorState.PLAYING || sim.getCurrentState() == SimulatorState.PAUSED)){
            output.setValue(value);
        }
        else System.out.println("something funny happened with sim. sim is " + sim + " and state is " + sim.getCurrentState());
    }

    @Override
    public void initialize() {
        output.setValue(currentValue);
    }

    @Override
    public void setProperties(Properties properties) {
        name = (String) properties.getAttribute("Label").getValue();
        external = (Boolean) properties.getAttribute("External?").getValue();
        properties.getAttribute("Label").addAttributeListener(this);
        properties.getAttribute("External?").addAttributeListener(this);
    }
    
    public boolean isExternal() { return external; }
    public String getPinName() { return name; }

    @Override
    public void attributeValueChanged(Attribute attr, Object value) {
        if(attr.getName().equals("Label")){
            name = (String) value;
        } else if(attr.getName().equals("External?")){
            external = (Boolean) value;
        }
    }

}
