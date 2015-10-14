package netlist.properties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.transform.sax.TransformerHandler;
import sim.SimItem;
import ui.components.ComponentEdge;
import ui.components.SelectableComponent;
import ui.error.ErrorHandler;

/** @author Matt */
public class Properties implements Cloneable{
    private String key;
    private HashMap<String, Attribute> attributes = 
            new LinkedHashMap<String, Attribute>();
    private HashMap<String, PinPosition> inputPins = 
            new HashMap<String, PinPosition>();
    private HashMap<String, PinPosition> outputPins = 
            new HashMap<String, PinPosition>();
    private static HashMap<String, BufferedImage> images = 
            new HashMap<String, BufferedImage>();
    private Class<? extends SimItem> simItem;
    private Class<? extends SelectableComponent> selectableComponent;
    private JPanel attrPanel = new JPanel();
    
    public Properties(String key){
        this.key = key;
        createAttributesPanel();
    }

    /** Register a listener to be notified of changes to attributes here*/
    public void addAttributesListener(AttributeListener al) {
        for(Attribute a: attributes.values()){
            a.addAttributeListener(al);
        }
    }

    /** Remove a listener */
    public void removeAttributesListener(AttributeListener al) {
        for(Attribute a: attributes.values()){
            a.removeAttributeListener(al);
        }
    }
    
    /** Specify the class which is used to create a visual component */
    protected void setVisualComponentClass(Class<? extends SelectableComponent> sc){
        this.selectableComponent = sc;
    }
    
    /** Specify the class which is used to create a logical component */
    protected void setLogicalComponentClass(Class<? extends SimItem> simItem){
        this.simItem = simItem;
    }
    
    public Class<? extends SelectableComponent> getVisualComponentClass() {
        return selectableComponent;
    }

    public Class<? extends SimItem> getLogicalComponentClass() {
        return simItem;
    }
    
    /** Specify the class which is used to create a visual component */
    protected void addAttribute(Attribute attr){
        attributes.put(attr.getName(), attr);
        createAttributesPanel();
    }
    
    /** Return the panel for use in the editor to change attributes */
    public JPanel getAttributesPanel(){
        return attrPanel;        
    }    
    
    /** Return an attribute by it's key */
    public Attribute getAttribute(String name){
        return attributes.get(name);        
    }
    
    /** Is there an attribute with a key here? */
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }
    
    /** Add an image for use in a visual component */
    protected void addImage(String name, String imageFilename){
        try {
            if(!images.containsKey(key + "." + name)){
                BufferedImage image = ImageIO.read(
                        getClass().getResource(imageFilename));
                images.put(key + "." + name, image);
            }
        } catch (Exception e) {
            try{
                BufferedImage image = ImageIO.read(new File(imageFilename));
                images.put(name, image);
            } catch (Exception ex) {
                ErrorHandler.newError(new ui.error.Error("Initialisation Error", 
"Could not load image: \n" + imageFilename + ".\n\nComponent not loaded.", ex));    
            }                        
        }          
    }
    
    public BufferedImage getImage(String name){
        if(images.containsKey(name)){
            return images.get(name);
        } else if (images.containsKey(key + "." + name)){
            return images.get(key + "." + name);
        } else {
            return null;
        }
    }
    
    /** Link a pin name with its location */
    protected void addInputPin(String name, ComponentEdge edge, int n){
        inputPins.put(name, new PinPosition(edge, n));
    }
    
    public Map<String, PinPosition> getInputPins(){
        return inputPins;
    }
    
    /** Link a pin name with its location */
    protected void addOutputPin(String name, ComponentEdge edge, int n){
        outputPins.put(name, new PinPosition(edge, n));
    }
    
    public Map<String, PinPosition> getOutputPins(){
        return outputPins;
    }
    
    public void createAttributesPanel() {
        attrPanel = new JPanel();
        attrPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        int i = 0;
        for (Attribute a : attributes.values()) {
            c.anchor = GridBagConstraints.WEST;
            c.gridx = 0;
            c.gridy = i;
            attrPanel.add(new JLabel(a.getName()), c);

            c.weightx = 0.5;
            c.fill = GridBagConstraints.BOTH;
            c.anchor = GridBagConstraints.EAST;
            c.gridx = 1;
            c.gridy = i;
            attrPanel.add(a.getJComponent(), c);
            i++;
        }
    }
    
    /** Return the unique name of this properties object */
    public String getKeyName(){
        return key;
    }
    
    /** Visitor method for writing a circuit to disk. */
    public void createXML(TransformerHandler hd){
        for(Attribute attr: attributes.values()){
            attr.createXML(hd);
        }
    }
 }