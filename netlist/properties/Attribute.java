package netlist.properties;

import javax.swing.JComponent;

/**
 *
 * @author Matt
 */
public abstract class Attribute {
    private String name;
    private Object value;
    protected JComponent jcomponent;
    
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
    
    public boolean validate(Object value){
        return true;
    }
   
}
