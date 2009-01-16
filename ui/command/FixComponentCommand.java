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
    private Point endPoint;

    public FixComponentCommand(SelectableComponent sc, Point endPoint){
        this.sc = sc;
        this.endPoint = endPoint;
    }
    
    @Override
    protected void perform(Editor editor) {
        if(activeCircuit != null){
            sc.moveTo(endPoint, true);
            sc.setLabel(editor.getOptionsPanel().getCurrentLabel());
            editor.repaint();
            editor.getOptionsPanel().repaint();
            //canUndo = true;
        }
    }
    
//    @Override
//    protected void undoEffect(Editor editor) {
//        if(activeCircuit != null){
//            sc.moveTo(endPoint, false);
//            editor.repaint();
//            editor.getOptionsPanel().repaint();
//            canUndo = false;
//        }
//    }

    @Override
    public String toString() {
        return "Fix Component to Circuit";
    }
    

}
