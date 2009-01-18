package ui.command;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import javax.swing.JComponent;
import ui.Editor;
import ui.UIConstants;

/**
 *
 * @author matt
 */
public class CommandHistory {

    private Editor parentEditor;
    private Stack<Command> undostack = new Stack<Command>();
    private Stack<Command> redostack = new Stack<Command>();
    private List<JComponent> undolisteners = new LinkedList<JComponent>();
    private List<JComponent> redolisteners = new LinkedList<JComponent>();
    private boolean isDirty = false;
    
    public CommandHistory(Editor parentEditor){
        this.parentEditor = parentEditor;
    }
    
    /**
     * Execute a new command and if it can be undone, push it onto the undo stack
     * for later.
     * 
     * @param cmd   The #Command to be performed
     */
    public void doCommand(Command cmd){
        cmd.execute(parentEditor);
        if(cmd.canUndo()){ // Is this an undoable command?
            undostack.push(cmd);
            if(undostack.size() == 1){
                for(JComponent c: undolisteners){
                    c.setEnabled(canUndo());
                }
            }
        }
        
        // The state changes when we do new actions so redos are not guarenteed to be safe
        redostack.clear();
        for(JComponent c: redolisteners){
            c.setEnabled(canRedo());
        }        
        
        setIsDirty(true);
    }

    public boolean isDirty() {
        return isDirty;
    }
    
    public void setIsDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }
    
    /**
     * Undo the command at the top of the undo stack.
     */
    public void undo(){
        if(canUndo() && undostack.peek().canUndo()){
            Command cmd = undostack.pop();
            cmd.undo(parentEditor);
            redostack.push(cmd);
            if(undostack.empty()){
                for(JComponent c: undolisteners){
                    c.setEnabled(canUndo());
                }
            }
            if(redostack.size() == 1){
                for(JComponent c: redolisteners){
                    c.setEnabled(canRedo());
                }
            }
        } else {
            UIConstants.beep();
        }
    }
    
    /**
     * Redo the command at the top of the redo stack.
     */
    public void redo(){
        if(canRedo()){
            Command cmd = redostack.pop();
            cmd.execute(parentEditor);
            undostack.push(cmd);
            if(undostack.size() == 1){
                for(JComponent c: undolisteners){
                    c.setEnabled(canUndo());
                }
            }
            if(redostack.empty()){
                for(JComponent c: redolisteners){
                    c.setEnabled(canRedo());
                }
            }
        } else {
            UIConstants.beep();
        }
    }
    
    public boolean canUndo(){
        return !undostack.empty();
    }
    
    public boolean canRedo(){
        return !redostack.empty();
    }
    
    public void clearHistory(){
        undostack.clear();
        redostack.clear();
    }
    
    /**
     * Add a #JComponent which can call an undo operation. Undo Listeners are
     * disabled when there are no actions to undo.
     * 
     * @param undolistener
     */
    public void addUndoEmptyListener(JComponent undolistener){
        undolisteners.add(undolistener);
    }
    
    /**
     * Add a #JComponent which can call a redo operation. Redo Listeners are
     * disabled when there are no actions to redo.
     * 
     * @param undolistener
     */
    public void addRedoEmptyListener(JComponent redolistener){
        redolisteners.add(redolistener);
    }
}

