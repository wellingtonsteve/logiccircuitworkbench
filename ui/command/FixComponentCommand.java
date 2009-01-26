package ui.command;

import ui.Editor;
import ui.components.SelectableComponent;

/**
 *
 * @author matt
 */
public class FixComponentCommand extends Command {
    
    private SelectableComponent sc;

    public FixComponentCommand(SelectableComponent sc){
        this.sc = sc;
    }
    
    @Override
    protected void perform(Editor editor) {
        if(activeCircuit != null){
            activeCircuit.removeUnFixedComponents();
            if(!activeCircuit.containsComponent(sc)){
                sc.setFresh();
                activeCircuit.addComponent(sc);
            }
            sc.translate(0, 0, true);
            canUndo = true;
        }
    }
    
    @Override
    protected void undoEffect(Editor editor) {
        canUndo = false;
        sc.getParent().removeComponent(sc);
    }
    
    @Override
    public String toString() {
        return "Fix Component to Circuit";
    }
    

}
