package ui.command;

import java.util.LinkedList;
import ui.ClipboardType;
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
            selection.addAll(activeCircuit.getActiveComponents());
            activeCircuit.deleteActiveComponents();    
            editor.addSetToClipboard(selection, ClipboardType.Cut);
            //canUndo = true;
        }
    }

    @Override
    public String toString() {
        return "Cut";
    }

}
