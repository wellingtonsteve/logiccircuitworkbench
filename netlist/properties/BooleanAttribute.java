package netlist.properties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        final JCheckBox cb = new JCheckBox();
        cb.setSelected(defaultValue);
        cb.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(cb.isSelected());
            }
        });
        jcomponent = cb;
    }
}
