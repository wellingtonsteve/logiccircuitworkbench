package ui.command;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
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
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private final JLabel statusAnimationLabel;
    private final JLabel statusMessageLabel;
    private final JProgressBar progressBar;

    public CommandHistory(Editor parentEditor) {
        this.parentEditor = parentEditor;
        this.statusAnimationLabel = parentEditor.getStatusAnimationLabel();
        this.statusMessageLabel = parentEditor.getStatusMessageLabel();
        this.progressBar = parentEditor.getProgressBar();
        
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("ui/Bundle"); // NOI18N
        // status bar initialization - message timeout, idle icon and busy animation, etc
        int messageTimeout = Integer.parseInt(bundle.getString("StatusBar.messageTimeout"));
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = Integer.parseInt(bundle.getString("StatusBar.busyAnimationRate"));
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = new javax.swing.ImageIcon(getClass().getResource(bundle.getString("StatusBar.busyIcons[" + i + "]")));
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = new javax.swing.ImageIcon(getClass().getResource(bundle.getString("StatusBar.idleIcon")));
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);
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
            setIsDirty(true);
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
    
    /**
     * This command history is dirty if an undoable command has been done since
     * the last save.
     * 
     * @return
     */
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
            cmd.activeCircuit.repaint();
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
            cmd.activeCircuit.repaint();
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
    
    void stageChange(String stage, Object value) {
        if ("started".equals(stage)) {
            if (!busyIconTimer.isRunning()) {
                statusAnimationLabel.setIcon(busyIcons[0]);
                busyIconIndex = 0;
                busyIconTimer.start();
            }
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);
        } else if ("done".equals(stage)) {
            busyIconTimer.stop();
            statusAnimationLabel.setIcon(idleIcon);
            progressBar.setVisible(false);
            progressBar.setValue(0);
        } else if ("message".equals(stage)) {
            String text = (String)value;
            statusMessageLabel.setText((text == null) ? "" : text);
            messageTimer.restart();
        } else if ("progress".equals(stage)) {
            int i = (Integer)value;
            progressBar.setVisible(true);
            progressBar.setIndeterminate(false);
            progressBar.setValue(i);
        }
    }
}

