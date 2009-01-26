package ui.command;

import java.util.Collection;
import java.util.LinkedList;
import ui.CircuitPanel;
import ui.Editor;
import ui.components.SelectableComponent;

/**
 *
 * @author matt
 */
public class SelectionPasteCommand extends Command {
    private Collection<SelectableComponent> pasted = new LinkedList<SelectableComponent>();
    private CircuitPanel sourcePanel = null;
    
    @Override
    protected void perform(Editor editor) {
        activeCircuit.removeUnFixedComponents();
        pasted = editor.getClipboard().getLastClipboardItem();
        for(SelectableComponent sc: pasted){
            sourcePanel = sc.getParent();
            sc.setParent(activeCircuit);
            sc.translate(0, 0, false);        
        }
        activeCircuit.addComponentList(pasted);
    }
    
    @Override
    protected void undoEffect(Editor editor) {
        for(SelectableComponent sc: pasted){
            sc.setParent(sourcePanel);
            sc.translate(0, 0, true);
            activeCircuit.removeComponent(sc);
        }
        pasted.clear();
        canUndo = false;
    }

    @Override
    public String toString() {
        return "Paste";
    }

}
