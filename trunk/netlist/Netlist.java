package netlist;

import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.imageio.ImageIO;
import sim.SimItem;
import ui.error.ErrorHandler;
import ui.tools.SelectableComponent;

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
public abstract class Netlist {
    
    protected LinkedHashMap<String, Class<? extends SelectableComponent>> classMap = new  LinkedHashMap<String, Class<? extends SelectableComponent>>();
    protected LinkedHashMap<String, BufferedImage> imageMap = new  LinkedHashMap<String, BufferedImage>();
    protected LinkedHashMap<String, Class<? extends SimItem>> logicMap = new LinkedHashMap<String, Class<? extends SimItem>>();    
    
    public Netlist(){
        setLogicMappings();
        setClassMappings();
        setImageMappings();
    }

    /**
     * Populate the mappings upon intialisation
     */
    protected abstract void setClassMappings();
    
    /**
     * Populate the image mappings upon intialisation
     */
    protected abstract void setImageMappings();
    
    /**
     * Populate the mappings upon intialisation for the Simulation part
     */
    protected abstract void setLogicMappings();
    
    /**
     * Create a mapping from <code>key</code> to <code>value</code>.
     * 
     * @param key   The name as it will appear in the component tree
     * @param value     The canonical name of the class file
     */
    public void putClass(String key, Class<? extends SelectableComponent> value){
        classMap.put(key, value);
    }
      
     /**
     * Create a mapping from <code>key</code> to <code>value</code>.
     * 
     * @param key   The name as it will appear in the component tree
     * @param value     The file name of the image to be associated with the key
     */
    public void putImage(String key, String value){
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResource(value));
            imageMap.put(key, image);
        } catch (Exception ex) {
            key = key.replaceAll(".default", "");
            key = key.replaceAll(".active", "");
            key = key.replaceAll(".selected", "");
 
            System.out.println(key);
            classMap.remove(key);
            logicMap.remove(key);
            ErrorHandler.newError(new ui.error.Error("Initialisation Error", "Could not load image: \n" + value + ".\n\nComponent not loaded.", ex));    
        }        
        
    }
    
    /**
     * Create a mapping from <code>key</code> to <code>value</code>.
     * 
     * @param key   The name as it will appear in the component tree
     * @param value     The canonical name of the logic class file
     */
    public void putLogicClass(String key, Class<? extends SimItem> value){
        logicMap.put(key, value);
    }
    
    /**
     * Get the class associated with the <code>key</code>
     * @param key
     * @return
     */
    public Class<? extends SelectableComponent> getClass(String key){
        return classMap.get(key);
    }
    
    /**
     * Get the class associated with the <code>key</code>
     * @param key
     * @return
     */
    public BufferedImage getImage(String key){
        if(imageMap.containsKey(key)){
            BufferedImage image = imageMap.get(key);
            return image.getSubimage(0, 0, image.getWidth(), image.getHeight());
        } else {
            return null;
        }
        
    }
  
    /**
     * Get the class associated with the <code>key</code>
     * @param key
     * @return
     */
    public Class<? extends SimItem> getLogicClass(String key){
        return logicMap.get(key);
    }
    
    /**
     * @see #java.util.Set
     */
    public Set<String> keySet(){
        return logicMap.keySet();
    }
    
    /**
     * @see #java.util.Set
     */
    public boolean containsKey(String key){
        return classMap.containsKey(key);
    }
    
    /**
     * @see #java.util.Set
     */
    public boolean containsLogicKey(String key){
        return logicMap.containsKey(key);
    }
}
