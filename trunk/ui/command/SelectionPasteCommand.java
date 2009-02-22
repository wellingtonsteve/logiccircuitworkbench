package ui.command;

import java.util.LinkedList;
import ui.Editor;
import ui.components.SelectableComponent;

/**
 *
 * @author matt
 */
public class SelectionPasteCommand extends Command {
    private LinkedList<SelectableComponent> pasted = new LinkedList<SelectableComponent>();
    
    @Override
    protected void perform(Editor editor) {
        activeCircuit.removeUnfixedComponents();
        pasted = (LinkedList<SelectableComponent>) editor.getClipboard().getLastClipboardItem();        
        int dx = activeCircuit.getMousePosition().x - pasted.getFirst().getOrigin().x;
        int dy = activeCircuit.getMousePosition().y - pasted.getFirst().getOrigin().y;
        for(SelectableComponent sc: pasted){
            sc.setParent(activeCircuit);  
            sc.translate(dx, dy, false);  
            sc.addPinListeners();
        }
        activeCircuit.addComponentList(pasted);
    }
    
    @Override
    protected void undoEffect(Editor editor) {
        for(SelectableComponent sc: pasted){
            activeCircuit.removeComponent(sc);
        }
        pasted.clear();
        canUndo = false;
    }

    @Override
    public String getName() {
        return "Paste";
    }

}
