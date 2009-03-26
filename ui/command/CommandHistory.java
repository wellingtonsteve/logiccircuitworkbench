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
    private CommandHistoryBrowser chb;

    public CommandHistory(Editor parentEditor) {
        this.parentEditor = parentEditor;
        this.statusAnimationLabel = parentEditor.getStatusAnimationLabel();
        this.statusMessageLabel = parentEditor.getStatusMessageLabel();
        this.progressBar = parentEditor.getProgressBar();
        if(UIConstants.SHOW_CLIPBOARD_BROWSERS){
            chb = new CommandHistoryBrowser();
            chb.setVisible(true);
        }        
        
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
            busyIcons[i] = new javax.swing.ImageIcon(getClass().getResource(bundle.getString("StatusBar.busyIcons["+i+"]")));
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
            ui.CircuitFrame frame = cmd.activeCircuit.getParentFrame();
            frame.setTitle(cmd.activeCircuit.getFilename() +  "*");
            // Renable Parts of the user interface
            if(undostack.size() == 1){
                for(JComponent c: undolisteners){
                    c.setEnabled(canUndo());
                }
            }
        }
              
        // The state changes when we do new actions, so redos are not guarenteed to be safe
        redostack.clear();
        // Tell Listeners
        for(JComponent c: redolisteners){
            c.setEnabled(canRedo());
        }              
        updateBrowser();
    }
    
    /**
     * This command history is dirty if an undoable command has been done since
     * the last save.
     * 
     * @return Is this command history dirty?
     */
    public boolean isDirty() {
        return isDirty;
    }
    
    /** Artifically dirty or clean this history.
     * @param isDirty The new dirty state of this history. */
    public void setIsDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }
    
    /** Undo the command at the top of the undo stack. */
    public void undo(){
        if(canUndo() && undostack.peek().canUndo()){
            Command cmd = undostack.pop();
            cmd.undo(parentEditor);
            redostack.push(cmd);
            // Tell listeners
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
            updateBrowser();
        } else {
            UIConstants.beep();
        }
    }
    
    /** Redo the command at the top of the redo stack. */
    public void redo(){
        if(canRedo()){
            Command cmd = redostack.pop();
            cmd.execute(parentEditor);
            undostack.push(cmd);
            // Tell listeners
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
            updateBrowser();
        } else {
            UIConstants.beep();
        }
    }
    
    /** Can an undo be done? */
    public boolean canUndo(){
        return !undostack.empty();
    }
    
    /** Can a redo be done? */
    public boolean canRedo(){
        return !redostack.empty();
    }
    
    /** Reset this command history */
    public void clearHistory(){
        undostack.clear();
        redostack.clear();
        isDirty = false;
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
     * @param redolistener
     */
    public void addRedoEmptyListener(JComponent redolistener){
        redolisteners.add(redolistener);
    }
    
    /** This method can be called by a command if it wants to provide feedback to
     * the user whilst it is preforming its action. 
     * 
     * @param stage The stage that an action has reached. 
     * @param value
     */
    public void stageChange(CommandStage stage, Object value) {
        switch(stage){
            case Started:
                if (!busyIconTimer.isRunning()) {
                    statusAnimationLabel.setIcon(busyIcons[0]);
                    busyIconIndex = 0;
                    busyIconTimer.start();
                }
                progressBar.setVisible(true);
                progressBar.setIndeterminate(true);
                break;
            case Done:
                busyIconTimer.stop();
                statusAnimationLabel.setIcon(idleIcon);
                progressBar.setVisible(false);
                progressBar.setValue(0);
                break;
            case Message:
                String text = (String)value;
                statusMessageLabel.setText((text == null) ? "" : text);
                messageTimer.restart();
                break;
            case Progress:
                int i = (Integer)value;
                progressBar.setVisible(true);
                progressBar.setIndeterminate(false);
                progressBar.setValue(i);
                break;
        }
    }

    private void updateBrowser() {
        if(UIConstants.SHOW_CLIPBOARD_BROWSERS){
            String undoString = new String();
            for (Command c : undostack) {
                undoString += c.getName() + "\n";
            }
            chb.getUndoTextArea().setText(undoString);

            String redoString = new String();
            for (Command c : redostack) {
                redoString += c.getName() + "\n";
            }
            chb.getRedoTextArea().setText(redoString);
        }
    }
}

