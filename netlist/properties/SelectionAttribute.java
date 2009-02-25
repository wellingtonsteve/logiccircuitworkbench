package netlist.properties;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/**
 *
 * @author Matt
 */
public class SelectionAttribute extends Attribute{
    
    public SelectionAttribute(String name, String[] options){
        super(name, options);
        setValue(options[0]);
    }

    @Override
    protected void setJComponent() {
        final JComboBox cb = new JComboBox((Object[])getValue());        
        cb.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                changeValue(cb.getSelectedItem());
            }
        });
        cb.setMaximumSize(new Dimension(100, 25));
        jcomponent = cb;
    }
    
    @Override
    public void setValue(Object val) {
        if(validate(val)){
            ((JComboBox) jcomponent).setSelectedItem(val);
        }
        super.setValue(val);
    }
    
}