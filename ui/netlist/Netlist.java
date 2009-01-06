package ui.netlist;

import java.util.LinkedHashMap;
import ui.tools.SelectableComponent;

/**
 *
 * @author matt
 */
public abstract class Netlist extends LinkedHashMap<String, Class<? extends SelectableComponent>> {
    
    public Netlist(){
        super();
        setMappings();
    }

    /**
     * Populate the mappings upon intialisation
     */
    protected abstract void setMappings();
           
}
