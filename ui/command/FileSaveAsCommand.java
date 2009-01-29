package ui.command;

import ui.file.XMLFileFilter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import ui.Editor;

/**
 *
 * @author matt
 */
public class FileSaveAsCommand extends Command {
    
    public FileSaveAsCommand(CommandHistory cmdHist) {
        super(cmdHist);
    }
    
    @Override
    protected void perform(Editor editor) {
        String filename;                                    
        JFileChooser c = new JFileChooser();
        FileFilter xmlFilter = new XMLFileFilter();        
        c.setFileFilter(xmlFilter);
        c.setDialogType(JFileChooser.SAVE_DIALOG);
        int rVal = c.showSaveDialog(editor);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            filename = c.getSelectedFile().getAbsolutePath();
            activeCircuit.saveAs(filename);
            parentHistory.stageChange("message", getName() + ": " + filename);
        }
    }

    @Override
    public String getName() {
        return "Saved Circuit File As";
    }

}
