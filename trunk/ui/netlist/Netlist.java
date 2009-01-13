package ui.netlist;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import ui.tools.SelectableComponent;

/**
 *
 * @author matt
 */
public abstract class Netlist {
    
    protected LinkedHashMap<String, Class<? extends SelectableComponent>> classMap = new  LinkedHashMap<String, Class<? extends SelectableComponent>>();
    protected LinkedHashMap<String, BufferedImage> imageMap = new  LinkedHashMap<String, BufferedImage>();
    
    public Netlist(){
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
        } catch (IOException ex) {
            Logger.getLogger(Netlist.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(image==null){System.out.println("no file loaded");
            
        }
        imageMap.put(key, image);
    }
    
    /**
     * Get the class associated with the <code>key</key>
     * @param key
     * @return
     */
    public Class<? extends SelectableComponent> getClass(String key){
        return classMap.get(key);
    }
    
    /**
     * Get the class associated with the <code>key</key>
     * @param key
     * @return
     */
    public BufferedImage getImage(String key){
        BufferedImage image = imageMap.get(key);
        return image.getSubimage(0, 0, image.getWidth(), image.getHeight());
    }
    
    /**
     * @see #java.util.Set
     */
    public Set<String> keySet(){
        return classMap.keySet();
    }
    
    /**
     * @see #java.util.Set
     */
    public boolean containsKey(String key){
        return classMap.containsKey(key);
    }
}
