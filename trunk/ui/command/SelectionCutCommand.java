package ui.command;

import java.util.LinkedList;
import ui.clipboard.ClipboardType;
import ui.Editor;
import ui.components.SelectableComponent;

/**
 *
 * @author matt
 */
public class SelectionCutCommand extends Command {
    private LinkedList<SelectableComponent> selection = new LinkedList<SelectableComponent>();

    @Override
    protected void perform(Editor editor) {
         if(activeCircuit.hasActiveSelection()){
            activeCircuit.removeUnFixedComponents();
            selection.addAll(activeCircuit.getActiveComponents());
            activeCircuit.deleteActiveComponents();    
            editor.getClipboard().addSetToClipboard(selection, ClipboardType.Cut);
            canUndo = true;
        }
    }

    @Override
    protected void undoEffect(Editor editor) {
        activeCircuit.addComponentList(selection);
        editor.getClipboard().removeLastClipboardItem();
        selection.clear();
        canUndo = false;
    }
    
    @Override
    public String toString() {
        return "Cut";
    }

}
