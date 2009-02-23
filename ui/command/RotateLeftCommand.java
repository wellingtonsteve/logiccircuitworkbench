package ui.command;

import java.util.List;
import ui.Editor;
import ui.components.SelectableComponent;

/**
 *
 * @author matt
 */
public class RotateLeftCommand extends Command {
    private SelectableComponent item;
            
    @Override
    protected void perform(Editor editor) {
        if(activeCircuit.hasActiveSelection() 
                && activeCircuit.getActiveComponents().size() == 1){
            List<SelectableComponent> selection = activeCircuit.getActiveComponents();
            item = selection.get(0);

            item.setRotation(item.getRotation() - Math.PI/2, true);
            editor.setComponentRotation(item.getRotation());
            editor.repaintOptionsPanel();

            activeCircuit.repaint();
            
        } else if(editor.getSelectableComponent() != null 
                && !activeCircuit.getCurrentTool().equals("Wire") 
                && !activeCircuit.getCurrentTool().equals("Select")){
            editor.setComponentRotation(editor.getSelectableComponent().getRotation() - Math.PI/2);
            editor.repaintOptionsPanel();
        }
    }
    
    @Override
    protected void undoEffect(Editor editor) {
        item.setRotation(item.getRotation() + Math.PI/2, true);
        canUndo = false;
        editor.repaint();
    }

    @Override
    public String getName() {
        return "Rotate Left";
    } 
    
}
