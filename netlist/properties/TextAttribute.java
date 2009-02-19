package netlist.properties;

import javax.swing.JTextField;

/**
 *
 * @author Matt
 */
public class TextAttribute extends Attribute{
    private String defaultValue;
    
    public TextAttribute(String name, String defaultValue){
        super(name, defaultValue);
        this.defaultValue = defaultValue;
    }
    
    @Override
    protected void setJComponent() {
        JTextField tf = new JTextField(defaultValue);
        jcomponent = tf;
    }
}
