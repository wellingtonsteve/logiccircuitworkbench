package netlist.properties;

import javax.swing.JComboBox;

/**
 *
 * @author Matt
 */
public class SelectionAttribute extends Attribute{
    
    public SelectionAttribute(String name, String[] options){
        super(name, options);
    }

    @Override
    protected void setJComponent() {
        JComboBox cb = new JComboBox((Object[])getValue());        
        jcomponent = cb;
    }
}