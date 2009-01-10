package ui.command;

import ui.CircuitPanel;
import ui.Editor;

/**
 *
 * @author matt
 */
public abstract class Command implements Cloneable{
    protected CircuitPanel activeCircuit;
    protected boolean canUndo = false;
    
    
    /**
     * Carry out the command (if we are in the correct state to do so)
     * 
     * @param editor The #Editor executing this command
     */
    public final void execute(Editor editor) {
       if(!canUndo){
           activeCircuit = editor.getActiveCircuit();
           perform(editor);
           if(activeCircuit!=null){editor.setActiveCircuit(activeCircuit);}
       }       
    }
    
    
    /**
     * Perform the main action of this command. This method must be overwritten
     * in all classes which extend the Command class.
     * 
     * @param editor    The #Editor executing this command
     */
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
        ui.error.ErrorHandler.newError(new ui.error.Error("Cannot Undo Command",this.getClass().getSimpleName() +"\nThis command cannot be undone."));
    }
    
    /** Make an independent copy of an unperformed command */
    public Command copy() {
       try {
             return (Command) this.clone();
       }
       catch (CloneNotSupportedException e) {
             ui.error.ErrorHandler.newError(new ui.error.Error("Cannot Clone Command",this.getClass().getSimpleName() +"\nThis command cannot be copied."));
             return null;
       }
    }

    @Override
    public abstract String toString();
    
}
