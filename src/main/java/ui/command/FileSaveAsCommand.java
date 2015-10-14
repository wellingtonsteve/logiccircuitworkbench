package ui.command;

import ui.file.CircuitFileFilter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import ui.Editor;

/**
 *
 * @author matt
 */
public class FileSaveAsCommand extends Command {
    
    @Override
    protected void perform(Editor editor) {
        String filename = activeCircuit.getFilename(); ;                                    
        JFileChooser c = new JFileChooser();
        FileFilter xmlFilter = new CircuitFileFilter();        
        c.setFileFilter(xmlFilter);
        c.setSelectedFile(new java.io.File(filename));
        c.setDialogType(JFileChooser.SAVE_DIALOG);
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
    }

    @Override
    public String getName() {
        return "Saved Circuit File As";
    }

}
