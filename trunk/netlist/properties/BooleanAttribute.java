package netlist.properties;

import javax.swing.JCheckBox;

/**
 *
 * @author Matt
 */
public class BooleanAttribute extends Attribute{
    private boolean defaultValue;
    
    public BooleanAttribute(String name, boolean defaultValue){
        super(name, defaultValue);
        this.defaultValue = defaultValue;        
    }

    @Override
    protected void setJComponent() {
        JCheckBox cb = new JCheckBox();
        cb.setSelected(defaultValue);
        
        jcomponent = cb;
    }
}
