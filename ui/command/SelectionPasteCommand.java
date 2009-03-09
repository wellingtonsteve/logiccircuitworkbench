package ui.command;

import java.awt.Point;
import java.util.LinkedList;
import ui.Editor;
import ui.command.SubcircuitOpenCommand.SubcircuitComponent;
import ui.components.SelectableComponent;
import ui.components.VisualComponent;

/**
 *
 * @author matt
 */
public class SelectionPasteCommand extends Command {
    private LinkedList<SelectableComponent> pasted = new LinkedList<SelectableComponent>();
    
    @Override
    protected void perform(Editor editor) {
        activeCircuit.removeUnfixedComponents();
        activeCircuit.resetActiveComponents();
        pasted = (LinkedList<SelectableComponent>) editor.getClipboard().getLastClipboardItem();        
        int dx = activeCircuit.getMousePosition().x - pasted.getFirst().getOrigin().x;
        int dy = activeCircuit.getMousePosition().y - pasted.getFirst().getOrigin().y;
        for(SelectableComponent sc: pasted){              
            if(!sc.getKeyName().equals("Wire") && !(sc instanceof SubcircuitComponent)){ 
                // Get a new properties object from the netlist
                sc.setProperties(editor.getNetlistWithKey(sc.getKeyName()).getProperties(sc.getKeyName()));
                sc.setParent(activeCircuit);
                sc.translate(dx, dy, false);
                sc.addListeners();
            } else if (sc instanceof SubcircuitComponent){
                //Create a new sub circuit
                Point oldOrigin = sc.getOrigin().getLocation();
                CreateComponentCommand ccc = new CreateComponentCommand(
                        activeCircuit,
                        sc.getKeyName(),
                         editor.getComponentRotation(),
                        SelectableComponent.DEFAULT_ORIGIN());
                ccc.execute(editor);
                ((VisualComponent)ccc.getComponent()).addLogicalComponentToCircuit();
                sc = ccc.getComponent();
                sc.moveTo(oldOrigin, false);
                sc.translate(dx, dy, false);
                sc.addListeners();
            } else {
                sc.setParent(activeCircuit);
                sc.translate(dx, dy, false);
                sc.addListeners(); 
            }
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
