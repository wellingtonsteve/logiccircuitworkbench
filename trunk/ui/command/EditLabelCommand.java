/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.command;

import ui.Editor;
import ui.components.Labeled;

/**
 *
 * @author matt
 */
public class EditLabelCommand extends Command {
    private String labelStr;
    private String oldLabel;
    private Labeled item;

    public EditLabelCommand(Labeled sc, String text) {
        this.item = sc;
        this.labelStr = text;
        this.oldLabel = item.getLabel();
    }
    
    @Override
    protected void perform(Editor editor) {
        if(activeCircuit.hasActiveSelection()){            
            item.setLabel(labelStr);
            canUndo = true;
            editor.repaint();
        }
    }
    
    @Override
    protected void undoEffect(Editor editor) {
        item.setLabel(oldLabel);
        canUndo = false;
        editor.repaint();
    }

    @Override
    public String toString() {
        return "Edit label \"" + labelStr + "\"";
    } 

}
