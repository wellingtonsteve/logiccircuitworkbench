/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
    private SelectableComponent old;
    private Point endPoint;

    public FixComponentCommand(SelectableComponent old, SelectableComponent sc, Point endPoint){
        this.sc = sc;
        this.old = old;
        this.endPoint = endPoint;
    }
    
    @Override
    protected void perform(Editor editor) {
        if(activeCircuit != null){
            //activeCircuit.removeComponent(old);
            old.moveTo(endPoint, true);
            //activeCircuit.addComponent(sc);
            canUndo = true;
        }
    }
    
    @Override
    protected void undoEffect(Editor editor) {
        if(activeCircuit != null){
            activeCircuit.removeComponent(sc);
            sc.moveTo(endPoint, false);
            activeCircuit.addComponent(old);
            canUndo = false;
        }
    }

    @Override
    public String toString() {
        return "Fix Component to Circuit";
    }
    

}
