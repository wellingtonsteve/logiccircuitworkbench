package netlist.properties;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import sim.SimItem;
import ui.components.SelectableComponent;
import ui.error.ErrorHandler;

/**
 *
 * @author Matt
 */
public class Properties implements Cloneable{
    private String key;
    private HashMap<String, Attribute> attributes = new HashMap<String, Attribute>();
    private HashMap<String, Point> inputPins = new HashMap<String, Point>();
    private HashMap<String, Point> outputPins = new HashMap<String, Point>();
    private HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
    private Class<? extends SimItem> simItem;
    private Class<? extends SelectableComponent> selectableComponent;
    
    public Properties(String key){
        this.key = key;
    }
    
    protected void setVisualComponentClass(Class<? extends SelectableComponent> sc){
        this.selectableComponent = sc;
    }
    
    protected void setLogicalComponentClass(Class<? extends SimItem> simItem){
        this.simItem = simItem;
    }
    
    public Class<? extends SelectableComponent> getVisualComponentClass() {
        return selectableComponent;
    }

    public Class<? extends SimItem> getLogicalComponentClass() {
        return simItem;
    }
    
    protected void addAttribute(Attribute attr){
        attributes.put(attr.getName(), attr);
    }
    
    public JPanel getAttributesPanel(){
        JPanel attrPanel = new JPanel();        
        attrPanel.setLayout(new BoxLayout(attrPanel, BoxLayout.PAGE_AXIS));
        
        for(Attribute a: attributes.values()){
            JPanel subPanel = new JPanel();        
            subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.LINE_AXIS));
            subPanel.add(new JLabel(a.getName()+"   "));
            subPanel.add(a.getJComponent());   
            subPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            attrPanel.add(subPanel);
        }
        
        return attrPanel;        
    }    
    
    public Attribute getAttribute(String name){
        return attributes.get(name);        
    }
    
    protected void addImage(String name, String imageFilename){
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

    protected void addInputPin(String name, Point p){
        inputPins.put(name, p);
    }
    
    public Map<String, Point> getInputPins(){
        return inputPins;
    }
    
    protected void addOutputPin(String name, Point p){
        outputPins.put(name, p);
    }
    
    public Map<String, Point> getOutputPins(){
        return outputPins;
    }
 }