package netlist.properties;

import java.util.LinkedList;
import javax.swing.JComponent;

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
        if(validate(val)){
            this.value = val;
            for(AttributeListener al: attributeListeners){
                al.attributeValueChanged(this, value);
            }
        }
    }
       
}
