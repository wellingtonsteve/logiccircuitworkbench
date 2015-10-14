package netlist.properties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author Matt
 */
public abstract class ButtonAttribute extends Attribute{
    
    public ButtonAttribute(String name){
        super(name, "");     
    }

    @Override
    protected void setJComponent() {
        JButton button = new JButton("Open");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonClickAction(e);
            }
        });
        jcomponent = button;
    }
    
    protected abstract void buttonClickAction(ActionEvent e); 
}
