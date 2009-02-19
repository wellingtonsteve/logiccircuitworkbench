package netlist;

import java.awt.image.BufferedImage;
import java.util.Set;
import sim.SimItem;
import ui.components.SelectableComponent;

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
public interface Netlist {
    
    /**
     * @param key
     * @return the class associated with the <code>key</code>
     */
    public Class<? extends SelectableComponent> getDrawableClass(String key);
    
    /**
     * @param key
     * @return the class associated with the <code>key</code>
     */
    public BufferedImage getImage(String key);
  
    /**
     * @param key
     * @return the class associated with the <code>key</code>
     */
    public Class<? extends SimItem> getLogicClass(String key);
    
    /**
     * @see java.util.Set
     */
    public Set<String> keySet();
    
    /**
     * @see java.util.Set
     */
    public boolean containsDrawableKey(String key);
    
    /**
     * @see java.util.Set
     */
    public boolean containsLogicKey(String key);
    
}
