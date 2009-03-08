package ui.command;

import ui.Editor;
import ui.components.SelectableComponent;
import ui.components.standard.log.ViewerFrame;
import ui.components.standard.PinLogger;

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
            activeCircuit.removeUnfixedComponents();
            if(!activeCircuit.containsComponent(sc)){
                activeCircuit.addComponent(sc);
            } else {
                activeCircuit.bringComponentToFront(sc);
            }
            if(sc.isFixed() && sc instanceof PinLogger){
                 ViewerFrame loggerWindow = activeCircuit.getLoggerWindow();
                loggerWindow.addPinLogger((PinLogger) sc);
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
    public String getName() {
        return "Fix Component to Circuit";
    }
    

}
