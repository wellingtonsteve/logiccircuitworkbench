/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.command;

import ui.Editor;
import ui.tools.SelectableComponent;

/**
 *
 * @author matt
 */
public class AddComponentCommand extends Command {
    private SelectableComponent sc;

    public AddComponentCommand(SelectableComponent sc){
        this.sc = sc;
    }
    
    @Override
    protected void perform(Editor editor) {
        if(activeCircuit != null){
            activeCircuit.addComponent(sc);
            canUndo = true;
        }
    }
    
    @Override
    protected void undoEffect(Editor editor) {
        canUndo = false;
        activeCircuit.removeComponent(sc);
    }

    @Override
    public String toString() {
        return "Add Component to Circuit";
    }
    
    

}
