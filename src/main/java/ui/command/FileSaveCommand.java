package ui.command;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import ui.Editor;
import ui.file.CircuitFileFilter;

/**
 *
 * @author matt
 */
public class FileSaveCommand extends Command {
    
    @Override
    protected void perform(Editor editor) {
        String filename = activeCircuit.getFilename();    
        // Has a save been performed yet?
        if(filename.substring(0, 8).equals("Untitled") || filename == null || filename.equals("")){
                    
            JFileChooser c = new JFileChooser();
            FileFilter xmlFilter = new CircuitFileFilter();        
            c.setFileFilter(xmlFilter);
            c.setDialogType(JFileChooser.SAVE_DIALOG);
            c.setSelectedFile(new java.io.File(filename));
            int rVal = c.showSaveDialog(editor);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                filename = c.getSelectedFile().getAbsolutePath();
                // Forgotten Extension?
                if(!ui.UIConstants.FILE_EXTENSION.equals(filename.substring(filename.length()-4, filename.length()))){
                    filename = filename + ui.UIConstants.FILE_EXTENSION;
                }
                activeCircuit.saveAs(filename);
                parentHistory.stageChange(CommandStage.Message, getName() + ": " + filename);
            }
        // Update the saved file
        } else {
            activeCircuit.saveAs(filename);
            parentHistory.stageChange(CommandStage.Message, getName() + ": " + filename);
        }
    }

    @Override
    public String getName() {
        return "Saved Circuit File: filename";
    }
}
