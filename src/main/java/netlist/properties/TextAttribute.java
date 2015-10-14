package netlist.properties;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;

/** @author Matt */
public class TextAttribute extends Attribute{
    private String defaultValue;
    
    public TextAttribute(String name, String defaultValue){
        super(name, defaultValue);
        this.defaultValue = defaultValue;
        setJComponent();
    }
    
    @Override
    protected void setJComponent() {
        final JTextField tf = new JTextField(defaultValue);
        tf.setText((String) getValue());
        tf.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                changeValue(tf.getText());
            }
        });      
        tf.addFocusListener(new FocusAdapter(){
            @Override
            public void focusLost(FocusEvent e) {
                changeValue(tf.getText());
            }            
        });
        tf.setPreferredSize(new Dimension(50, 25));
        jcomponent = tf;
    }
    
     @Override
    public void setValue(Object val) {
        if(validate(val)){
            ((JTextField) jcomponent).setText((String) val);
            super.setValue(val);
        }      
    }
}
