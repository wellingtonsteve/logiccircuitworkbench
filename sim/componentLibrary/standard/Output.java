package sim.componentLibrary.standard;

import netlist.properties.*;
import sim.componentLibrary.Component;
import sim.joinable.*;
/**
 *
 * @author Stephen
 */
public class Output extends Component implements AttributeListener{
    private InputPin input = createInputPin("Input");
    private String name = "";
    private boolean external = false;

    public String getLongName() { return "Output"; }
    public String getShortName() { return "Output"; }

    @Override
    public void setProperties(Properties properties) {
        if(properties.hasAttribute("Label")){
            name = (String) properties.getAttribute("Label").getValue();
            properties.getAttribute("Label").addAttributeListener(this);
        }
        if(properties.hasAttribute("External?")){
            external = (Boolean) properties.getAttribute("External?").getValue();        
            properties.getAttribute("External?").addAttributeListener(this);
        }
    }

    public boolean isExternal() { return external; }
    public String getPinName() { return name; }

    public void attributeValueChanged(Attribute attr, Object value) {
        if(attr.getName().equals("Label")){
            name = (String) value;
        } else if(attr.getName().equals("External?")){
            external = (Boolean) value;
        }
    }
}
