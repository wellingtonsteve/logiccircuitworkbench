package ui.command;

import java.util.List;
import ui.Editor;
import ui.tools.SelectableComponent;

/**
 *
 * @author matt
 */
public class RotateRightCommand extends Command {
    private SelectableComponent item;
    
    @Override
    protected void perform(Editor editor) {
        if(activeCircuit.hasActiveSelection()){
            List<SelectableComponent> selection = activeCircuit.getActiveComponents();
            item = selection.get(0);
                       
            item.setRotation(item.getRotation() + Math.PI/2);
            
            canUndo = true;
            editor.repaint();
        } else if(editor.getOptionsPanel().getSelectableComponent() != null 
                && !activeCircuit.getCurrentTool().equals("Standard.Wire") 
                && !activeCircuit.getCurrentTool().equals("Select")){
            editor.getOptionsPanel().setComponentRotation(editor.getOptionsPanel().getSelectableComponent().getRotation() + Math.PI/2);
            editor.getOptionsPanel().repaint();
        }
    }
    
    @Override
    protected void undoEffect(Editor editor) {
        item.setRotation(item.getRotation() - Math.PI/2);
        canUndo = false;
        editor.repaint();
    }

    @Override
    public String toString() {
        return "Rotate Right";
    } 
    
}
