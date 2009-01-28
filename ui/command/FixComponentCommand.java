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
            sc.translate(0, 0, true);
            activeCircuit.removeUnFixedComponents();
            if(!activeCircuit.containsComponent(sc)){
                activeCircuit.addComponent(sc);
            }           
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
