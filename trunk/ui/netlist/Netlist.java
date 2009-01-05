package ui.netlist;

import java.util.HashMap;
import ui.tools.UITool;

/**
 *
 * @author matt
 */
public abstract class Netlist extends HashMap<String, UITool> {
    
    public Netlist(){
        super();
        setMappings();
    }

    /**
     * Populate the mappings upon intialisation
     */
    protected abstract void setMappings();
           
}
