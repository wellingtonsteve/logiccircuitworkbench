package ui.command;

import ui.CircuitPanel;
import ui.Editor;

/**
 *
 * @author matt
 */
public abstract class Command {
    private CircuitPanel circuit;
    
    /** Carry out the command (for the first time) */
    public final void execute(Editor editor) {
       circuit = editor.getActiveCircuit();
       perform(editor);
    }

    
    protected abstract void perform(Editor editor);
    
    /** After perform, test if command really made a change */
    public boolean canUndo() { return false; }
    
    /** Undo the command */
    public final void undo(Editor editor) {
        undoEffect(editor);
        editor.setActiveCircuit(circuit);

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

}
