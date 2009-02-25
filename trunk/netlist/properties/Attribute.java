package netlist.properties;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 *
 * @author Matt
 */
public abstract class Attribute implements Cloneable {
    private String name;
    private Object value;
    protected JComponent jcomponent;
    protected LinkedList<AttributeListener> attributeListeners = new LinkedList<AttributeListener>();
    
    public Attribute(String name, Object value){
        this.name = name;
        this.value = value;
        setJComponent();
    }
    
    public String getName(){
        return name;
    }
    
    public Object getValue(){
        return value;
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
        changeValue(val);
    }
     
    public void changeValue(Object val){
        if(validate(val)){
            this.value = val;
            for(AttributeListener al: attributeListeners){
                al.attributeValueChanged(this, value);
            }
        }
    }

    void createXML(TransformerHandler hd) {
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "", "name", "CDATA", getName());
            atts.addAttribute("", "", "value", "CDATA", (value instanceof String)?(String) value:value.toString());
            hd.startElement("", "", "attr", atts);
            hd.endElement("", "", "attr");
        } catch (SAXException ex) {
            Logger.getLogger(Attribute.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
