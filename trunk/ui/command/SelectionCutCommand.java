package ui.command;

import java.util.LinkedList;
import ui.Editor;
import ui.components.SelectableComponent;
import ui.components.standard.PinLogger;

/** Remove the current selection from the workarea of the active circuit and add 
 * the selection to the clipboard.   @author matt */
public class SelectionCutCommand extends Command {
    private LinkedList<SelectableComponent> selection = new LinkedList<SelectableComponent>();

    @Override
    protected void perform(Editor editor) {
         if(activeCircuit.hasActiveSelection()){
            activeCircuit.removeUnfixedComponents();
            selection.addAll(activeCircuit.getActiveComponents());
            activeCircuit.deleteActiveComponents();    
            for(SelectableComponent sc: selection){
                if(sc instanceof PinLogger){
                    activeCircuit.getLoggerWindow().removePinLogger(sc);
                }
            }  
            editor.getClipboard().cut(selection);
            canUndo = true;
        }
    }

    @Override
    protected void undoEffect(Editor editor) {
        activeCircuit.addComponentList(editor.getClipboard().paste());
        canUndo = false;
    }
    
    @Override
    public String getName() {
        return "Cut";
    }
}
