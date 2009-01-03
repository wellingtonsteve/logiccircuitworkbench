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
    
    public CommandHistory(Editor parentEditor){
        this.parentEditor = parentEditor;
    }
    
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
    }
    
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
    
    public void addUndoEmptyListener(JComponent undolistener){
        undolisteners.add(undolistener);
    }
    
    public void addRedoEmptyListener(JComponent redolistener){
        redolisteners.add(redolistener);
    }
}

