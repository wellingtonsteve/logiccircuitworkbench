package netlist.properties;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import sim.SimItem;
import ui.components.SelectableComponent;
import ui.error.ErrorHandler;

/**
 *
 * @author Matt
 */
public class Properties {
    private String key;
    private HashMap<String, Attribute> attributes = new HashMap<String, Attribute>();
    private HashMap<String, Point> inputPins = new HashMap<String, Point>();
    private HashMap<String, Point> outputPins = new HashMap<String, Point>();
    private HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
    private Class<? extends SimItem> simItem;
    private Class<? extends SelectableComponent> selectableComponent;
    
    public Properties(String key, Class<? extends SimItem> simItem){
        this.key = key;
        this.simItem = simItem;
    }
    
    public void setVisualComponentClass(Class<? extends SelectableComponent> sc){
        this.selectableComponent = sc;
    }
    
    public Class<? extends SelectableComponent> getVisualComponentClass() {
        return selectableComponent;
    }

    public Class<? extends SimItem> getLogicalComponentClass() {
        return simItem;
    }
    
    public void addAttribute(Attribute attr){
        attributes.put(attr.getName(), attr);
    }
    
    public JPanel getAttributesPanel(){
        JPanel attrPanel = new JPanel();
        attrPanel.setLayout(new java.awt.GridLayout(0,2));
        
        for(Attribute a: attributes.values()){
            attrPanel.add(new JLabel(a.getName()));
            attrPanel.add(a.getJComponent());            
        }        
        return attrPanel;        
    }    
    
    public Attribute getAttribute(String name){
        return attributes.get(name);        
    }
    
    public void addImage(String name, String imageFilename){
        try {
            BufferedImage image = ImageIO.read(getClass().getResource(imageFilename));
            images.put(name, image);
        } catch (Exception ex) {
            ErrorHandler.newError(new ui.error.Error("Initialisation Error", "Could not load image: \n" + imageFilename + ".\n\nComponent not loaded.", ex));    
        }          
    }
    
    public BufferedImage getImage(String name){
        return images.get(name);
    }

    public void addInputPin(String name, Point p){
        inputPins.put(name, p);
    }
    
    public Map<String, Point> getInputPins(){
        return inputPins;
    }
    
    public void addOutputPin(String name, Point p){
        outputPins.put(name, p);
    }
    
    public Map<String, Point> getOutputPins(){
        return outputPins;
    }
}
