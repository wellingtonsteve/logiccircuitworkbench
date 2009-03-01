package netlist.properties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.transform.sax.TransformerHandler;
import sim.SimItem;
import ui.components.SelectableComponent;
import ui.error.ErrorHandler;

/**
 *
 * @author Matt
 */
public class Properties implements Cloneable{
    private String key;
    private HashMap<String, Attribute> attributes = new LinkedHashMap<String, Attribute>();
    private HashMap<String, Point> inputPins = new HashMap<String, Point>();
    private HashMap<String, Point> outputPins = new HashMap<String, Point>();
    private HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
    private Class<? extends SimItem> simItem;
    private Class<? extends SelectableComponent> selectableComponent;
    private JPanel attrPanel = new JPanel();
    
    public Properties(String key){
        this.key = key;
        createAttributesPanel();
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
        createAttributesPanel();
    }
    
    public JPanel getAttributesPanel(){
        return attrPanel;        
    }    
    
    public Attribute getAttribute(String name){
        return attributes.get(name);        
    }
    
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }
    
    protected void addImage(String name, String imageFilename){
        try {
            BufferedImage image = ImageIO.read(getClass().getResource(imageFilename));
            images.put(name, image);
        } catch (Exception e) {
            try{
                BufferedImage image = ImageIO.read(new File(imageFilename));
                images.put(name, image);
            } catch (Exception ex) {
                ErrorHandler.newError(new ui.error.Error("Initialisation Error", "Could not load image: \n" + imageFilename + ".\n\nComponent not loaded.", ex));    
            }                        
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

    public void createAttributesPanel() {
        attrPanel = new JPanel();
        attrPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        int i = 0;
        for (Attribute a : attributes.values()) {
            c.anchor = GridBagConstraints.WEST;
            c.gridx = 0;
            c.gridy = i;
            attrPanel.add(new JLabel(a.getName() + "   "), c);

            c.weightx = 0.5;
            c.fill = GridBagConstraints.BOTH;
            c.anchor = GridBagConstraints.EAST;
            c.gridx = 1;
            c.gridy = i;
            attrPanel.add(a.getJComponent(), c);
            i++;
        }
    }
    
    public String getKeyName(){
        return key;
    }
    
    public void createXML(TransformerHandler hd){
        for(Attribute attr: attributes.values()){
            attr.createXML(hd);
        }
    }
 }