package ui.command;

import java.awt.Point;
import ui.Editor;
import ui.tools.SelectableComponent;

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
        }
    }
    
    @Override
    public String toString() {
        return "Fix Component to Circuit";
    }
    

}
