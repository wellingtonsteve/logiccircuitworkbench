package netlist;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import netlist.properties.Properties;

/**
 * A netlist class is a module for adding new components to the editor. Each component
 * is identified by a key which corresponds to the structure that it takes in the 
 * component tree. The component tree is created dynamically from all loaded netlists 
 * with different levels delimited by dots in the key string. A complete netlist 
 * contains mappings to Class files which extend the SelectableComponent class 
 * and also mappings to Class files which extend the SimItem class. 
 * 
 * The SelectableComponent mappings map the keys to descriptions of the HCI for 
 * component, i.e. how the component is drawn, what happens when it is clicked etc.
 * SelectableComponent's may also require images which illustrate common representations
 * of component in a circuit diagram. Mappings for these images are also defined
 * in the netlist inorder to minimise Disk I/Os. All Selectable Components with 
 * images retrieve thier images straight from the netlist (all images are loaded
 * automatically upon initialisation) rather from the disk when needed.
 * 
 * The SimItem mappings describe the behaviour of the component in terms of its 
 * inputs and outputs, i.e. the logical description.
 * 
 * 
 * @author matt
 */
public class Netlist{
    
    protected HashMap<String, Class<? extends Properties>> netlist = new HashMap<String, Class<? extends Properties>>();

    public Properties getProperties(String key){
        //Remove "Components." from begining
        if(key.length() > 11 && key.subSequence(0, 11).equals("Components.")){
            key = key.substring(11); 
        }
        try {
            return netlist.get(key).getConstructor(this.getClass(),String.class).newInstance(this, key);
        } catch (Exception ex) {
            Logger.getLogger(Netlist.class.getName()).log(Level.SEVERE, null, ex);
        } return null;
    }
    
    
//    /**
//     * @param key
//     * @return the class associated with the <code>key</code>
//     */
//    public Class<? extends SelectableComponent> getDrawableClass(String key) {
//        return netlist.get(key).getVisualComponentClass();
//    }
//
//    /**
//     * @param key
//     * @return the class associated with the <code>key</code>
//     */
//    public BufferedImage getImage(String key, String name) {
//        return netlist.get(key).getImage(name);
//    }

//    /**
//     * @param key
//     * @return the class associated with the <code>key</code>
//     */
//    public Class<? extends SimItem> getLogicClass(String key) {
//        return netlist.get(key).getLogicalComponentClass();
//    }

    /**
     * @see java.util.Set
     */
    public Set<String> keySet() {
        return netlist.keySet();
    }

//    /**
//     * TODO: javadoc
//     */
//    public boolean containsDrawableKey(String key) {
//        return netlist.containsKey(key) && netlist.get(key).getVisualComponentClass() != null;
//    }
    
    /**
     * @see java.util.Set
     */
    public boolean containsLogicKey(String key) {
        return netlist.containsKey(key);
    }
    
}
