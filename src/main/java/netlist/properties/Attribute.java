package netlist.properties;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/** An attribute is a mutable property of a component. Essentially the attribute
 * class is data class with various get and set methods plus an JComponent for 
 * use in the user interface. The final method creates xml data which is used
 * when saving the circuit to disk.  *
 * @author Matt
 */
public abstract class Attribute implements Cloneable {
    private String name;
    private Object value;
    protected JComponent jcomponent;
    protected LinkedList<AttributeListener> attributeListeners = 
            new LinkedList<AttributeListener>();
    private Object oldValue;
    
    public Attribute(String name, Object value){
        this.name = name;
        this.value = value;
        this.oldValue = value;
        setJComponent();
    }
    
    public String getName(){
        return name;
    }
    
    public Object getValue(){
        return value;
    }
    
    public Object getPreviousValue(){
        return oldValue;
    }

    protected abstract void setJComponent();
    
    public JComponent getJComponent() {
        return jcomponent;
    }
    
    public boolean validate(Object val){
        return true;
    }
    
    public void addAttributeListener(AttributeListener al){
        attributeListeners.add(al);
    }
    
    public void removeAttributeListener(AttributeListener al){
        attributeListeners.remove(al);
    }
    
    public void setValue(Object val){
        this.oldValue = value;
        this.value = val;        
    }
     
    public void changeValue(Object val){
        if(validate(val)){
            setValue(val);
            for(AttributeListener al: attributeListeners){
                al.attributeValueChanged(this, value);
            }
        }
    }

    void createXML(TransformerHandler hd) {
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "", "name", "CDATA", getName());
            atts.addAttribute("", "", "value", "CDATA",
                    (value instanceof String)?(String) value:value.toString());
            hd.startElement("", "", "attr", atts);
            hd.endElement("", "", "attr");
        } catch (SAXException ex) {
            Logger.getLogger(Attribute.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
}