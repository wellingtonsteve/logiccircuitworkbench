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
        }
    }

    @Override
    public String toString() {
        return "Save Circuit File As";
    }

}
