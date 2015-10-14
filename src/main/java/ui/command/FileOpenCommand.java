package ui.command;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import ui.Editor;
import ui.file.FileLoader;
import ui.file.CircuitFileFilter;

/** This command opens a File Open dialog for the user to choose a circuit to 
 * open.  @author matt */
public class FileOpenCommand extends Command {

    @Override
    protected void perform(Editor editor) {
        String filename; 
        // Set up the dialog
        JFileChooser c = new JFileChooser();
        FileFilter xmlFilter = new CircuitFileFilter();        
        c.setFileFilter(xmlFilter);
        c.setDialogType(JFileChooser.OPEN_DIALOG);
        int rVal = c.showOpenDialog(editor);
        // Check that yes/ok was chosen
        if (rVal == JFileChooser.APPROVE_OPTION) {
            
            // Get the path of the file which was chosen 
            filename = c.getSelectedFile().getAbsolutePath();
            // Create a file loader object
            FileLoader cfh = new FileLoader(editor);            
            activeCircuit = editor.createBlankCircuit(false);    
            
            // Parse the file
            activeCircuit.setFilename(filename);
            if(cfh.loadFile(filename)){                
                editor.refreshWindowsMenu();
                activeCircuit.getParentFrame().setTitle(filename);
                activeCircuit.getCommandHistory().clearHistory();
                parentHistory.stageChange(CommandStage.Message, getName() +
                        ": " + filename);
            } else {
                // Close bad circuit
                ((JDesktopPane) activeCircuit.getParentFrame().getParent()).
                        remove(activeCircuit.getParentFrame());
            }         
        }
    }

    @Override
    public String getName() {
        return "Opened Circuit File";
    }

}
