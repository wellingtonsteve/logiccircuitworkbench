package netlist.properties;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        final JTextField tf = new JTextField(defaultValue);
        tf.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(tf.getText());
            }
        });
        tf.setMaximumSize(new Dimension(tf.getMaximumSize().width, 25));
        jcomponent = tf;
    }
}
