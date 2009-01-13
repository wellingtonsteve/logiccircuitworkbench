/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.command;

import java.util.List;
import javax.swing.JOptionPane;
import ui.Editor;
import ui.Labeled;
import ui.tools.SelectableComponent;

/**
 *
 * @author matt
 */
public class AddLabelCommand extends Command {
    private String labelStr;
    private Labeled item;
    
    @Override
    protected void perform(Editor editor) {
        if(activeCircuit.hasActiveSelection()){
            List<SelectableComponent> selection = activeCircuit.getActiveComponents();
            item = selection.get(0);
            
            this.labelStr = JOptionPane.showInputDialog("Enter your label:",(item.hasLabel())?item.getLabel():"");
            
            item.setLabel(labelStr);
            canUndo = true;
            editor.repaint();
        }
    }
    
    @Override
    protected void undoEffect(Editor editor) {
        item.removeLabel();
        canUndo = false;
        editor.repaint();
    }

    @Override
    public String toString() {
        return "Add label \"" + labelStr + "\"";
    } 

}
