package netlist;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Set;
import netlist.properties.Properties;
import sim.SimItem;
import ui.components.SelectableComponent;

/**
 *
 * @author Matt
 */
public class NetlistProperties implements Netlist{
    
    protected HashMap<String, Properties> netlist = new HashMap<String, Properties>();

    public Class<? extends SelectableComponent> getDrawableClass(String key) {
        return netlist.get(key).getVisualComponentClass();
    }

    public BufferedImage getImage(String key, String name) {
        return netlist.get(key).getImage(name);
    }

    public Class<? extends SimItem> getLogicClass(String key) {
        return netlist.get(key).getLogicalComponentClass();
    }

    public Set<String> keySet() {
        return netlist.keySet();
    }

    public boolean containsDrawableKey(String key) {
        return netlist.get(key).getVisualComponentClass() != null;
    }

    public boolean containsLogicKey(String key) {
        return netlist.containsKey(key);
    }

    public BufferedImage getImage(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
