package netlist.properties;

import javax.swing.JComboBox;

/**
 *
 * @author Matt
 */
public class SelectionAttribute extends Attribute{
    private String[] options;
    
    public SelectionAttribute(String name, String[] options){
        super(name, options[0]);
        this.options = options;
    }

    @Override
    protected void setJComponent() {
        JComboBox cb = new JComboBox(options);        
        jcomponent = cb;
    }
}
