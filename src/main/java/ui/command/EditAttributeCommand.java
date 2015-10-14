package ui.command;

import netlist.properties.Attribute;
import ui.Editor;

/**
 *
 * @author matt
 */
public class EditAttributeCommand extends Command {
    private Object newValue;
    private Object oldValue;
    private Attribute attr;

    public EditAttributeCommand(Attribute attr, Object oldValue, Object newValue) {
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.attr = attr;
    }
    
    @Override
    protected void perform(Editor editor) {
        attr.setValue(newValue);
        canUndo = true;
        activeCircuit.repaint();
    }
    
    @Override
    protected void undoEffect(Editor editor) {
        attr.setValue(oldValue);
        canUndo = false;
        activeCircuit.repaint();
    }

    @Override
    public String getName() {
        return "Change Attribute";
    } 

}
