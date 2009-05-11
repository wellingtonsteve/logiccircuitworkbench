package netlist;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import netlist.properties.Properties;

/**
 * A netlist class is a module for adding new components to the editor. Each 
 * component is identified by a key which corresponds to the structure that it 
 * takes in the component tree. The component tree is created dynamically from 
 * all loaded netlists with different levels delimited by dots in the key 
 * string. A complete netlist contains mappings to Class files which extend the 
 * Properties class. Each properties class once instantiated contains all the 
 * information required for logical calculation and visual display. 
 * 
 * @author matt
 */
public abstract class Netlist{
    
    protected HashMap<String, Class<? extends Properties>> netlist =
            new LinkedHashMap<String, Class<? extends Properties>>();

    /**Create a properties object from the class description specified in the map.
     * @param key The key to the map.
     * @return An instantiated version of the corresponding properties class.
     */
    public Properties getProperties(String key){
        // Remove "Components." from begining of key
        if(key.length() > 11 && key.subSequence(0, 11).equals("Components.")){
            key = key.substring(11); 
        }
        try {
            return netlist.get(key).getConstructor(
                    this.getClass(),String.class).newInstance(this, key);
        } catch (Exception ex) {
            Logger.getLogger(Netlist.class.getName()).log(Level.SEVERE, null, ex);
        } return null;
    }

    /** @see java.util.HashMap#keySet() */
    public Set<String> keySet() {
        return netlist.keySet();
    }
    
    /** @see java.util.HashMap#keySet() */
    public boolean containsKey(String key) {
        return netlist.containsKey(key);
    }    
}