package ui.command;

import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import ui.Editor;
import ui.components.SelectableComponent;
import ui.components.standard.PinLogger;

/**
 *
 * @author matt
 */
public class SelectionDeleteCommand extends Command {
    private List<SelectableComponent> selection = new LinkedList<SelectableComponent>();
    
    protected void perform(Editor editor) {
        if(activeCircuit.hasActiveSelection()){
            int ans = JOptionPane.showConfirmDialog(editor,"Are you sure that you want to delete the selected component(s)?");

            if(ans == JOptionPane.YES_OPTION){     
                selection.addAll(activeCircuit.getActiveComponents());
                activeCircuit.deleteActiveComponents();  
                for(SelectableComponent sc: selection){
                    if(sc instanceof PinLogger){
                        activeCircuit.getLoggerWindow().removePinLogger(sc);
                    }
                }  
                canUndo = true;
            }  
        }
    }
        
    @Override
    protected void undoEffect(Editor editor) {
        activeCircuit.addComponentList(selection);
        selection.clear();
        canUndo = false;
    }

    @Override
    public String getName() {
        return "Delete " + selection.size() + "item(s)";
   }

}
