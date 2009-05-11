package ui.command;

import java.util.LinkedList;
import ui.Editor;
import ui.components.SelectableComponent;

/** Make a copy of the active selection on the circuit, clone each component and
 * add it to the clipboard @author matt */
public class SelectionCopyCommand extends Command {

    @Override
    protected void perform(Editor editor) {
         if(activeCircuit.hasActiveSelection()){
            activeCircuit.removeUnfixedComponents();
            LinkedList<SelectableComponent> selection 
                    = new LinkedList<SelectableComponent>();
            for(SelectableComponent sc: activeCircuit.getActiveComponents()){
                selection.add(sc.copy());
            }
            editor.getClipboard().copy(selection);
            canUndo = true;
        }
    }

    @Override
    protected void undoEffect(Editor editor) {
        editor.getClipboard().removeLastClipboardItem();
        canUndo = false;
    }

    @Override
    public String getName() {
        return "Copy";
    }
}
