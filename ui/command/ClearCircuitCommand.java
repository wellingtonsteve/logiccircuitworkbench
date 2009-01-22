/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.command;

import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import ui.CircuitPanel;
import ui.Editor;
import ui.components.SelectableComponent;

/**
 *
 * @author matt
 */
public class ClearCircuitCommand extends Command {   
    private List<SelectableComponent> backup = new LinkedList<SelectableComponent>();
    
    @Override
    protected void perform(Editor editor) {
        int ans = JOptionPane.showConfirmDialog(editor, 
            "Are you sure that you want to clear this circuit?");
        if(ans == JOptionPane.YES_OPTION){
            activeCircuit.selectAllComponents();
            backup.addAll(activeCircuit.getActiveComponents());
            activeCircuit.resetCircuit();
            canUndo = true;
            editor.repaint();
        }
    }
    
    @Override
    protected void undoEffect(Editor editor) {
        activeCircuit.addComponentList(backup);
        backup.clear();
        canUndo = false;
        editor.repaint();
    }

    @Override
    public String toString() {
        return "Clear Circuit";
    } 
    
}
