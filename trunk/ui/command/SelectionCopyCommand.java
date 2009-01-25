package ui.command;

import java.util.LinkedList;
import ui.Editor;
import ui.clipboard.ClipboardType;
import ui.components.SelectableComponent;

/**
 *
 * @author matt
 */
public class SelectionCopyCommand extends Command {

    @Override
    protected void perform(Editor editor) {
         if(activeCircuit.hasActiveSelection()){
            activeCircuit.removeUnFixedComponents();
            LinkedList<SelectableComponent> selection = new LinkedList<SelectableComponent>();
            selection.addAll(activeCircuit.getActiveComponents());
            editor.getClipboard().addSetToClipboard(selection, ClipboardType.Copy);
            canUndo = true;
        }
    }

    @Override
    protected void undoEffect(Editor editor) {
        editor.getClipboard().removeLastClipboardItem();
        canUndo = false;
    }

    @Override
    public String toString() {
        return "Copy";
    }

}
