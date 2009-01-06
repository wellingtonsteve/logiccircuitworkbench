package ui.command;

import ui.CircuitPanel;
import ui.Editor;

/**
 *
 * @author matt
 */
public abstract class Command {
    protected CircuitPanel activeCircuit;
    protected boolean canUndo = false;
    
    public Command(){
    }
    
    /** Carry out the command (for the first time) */
    public final void execute(Editor editor) {
       if(!canUndo){
           activeCircuit = editor.getActiveCircuit();
           perform(editor);
           if(activeCircuit!=null){editor.setActiveCircuit(activeCircuit);}
       }       
    }
    
    protected abstract void perform(Editor editor);
    
    /** After perform, test if command really made a change */
    public boolean canUndo() { return canUndo; }
    
    /** Undo the command */
    public final void undo(Editor editor) {
        if(canUndo){
            editor.setActiveCircuit(activeCircuit);
            undoEffect(editor);
        }        
    }
    
    protected void undoEffect(Editor editor) {
        //TODO Error reporting
           throw new Error("Can’t undo");
    }
    
    /** Make an independent copy of an unperformed command */
    public Command copy() {
       try {
             return (Command) this.clone();
       }
       catch (CloneNotSupportedException e) {
                   //TODO Error reporting
             throw new Error("Can’t clone command");
       }
    }

    @Override
    public abstract String toString();
    
}
