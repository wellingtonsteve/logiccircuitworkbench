package ui.command;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import ui.Editor;
import ui.file.CircuitFileHandler;
import ui.file.XMLFileFilter;

/**
 *
 * @author matt
 */
public class FileOpenCommand extends Command {

    @Override
    protected void perform(Editor editor) {
        String filename;                                    
        JFileChooser c = new JFileChooser();
        FileFilter xmlFilter = new XMLFileFilter();        
        c.setFileFilter(xmlFilter);
        c.setDialogType(JFileChooser.OPEN_DIALOG);
        int rVal = c.showOpenDialog(editor);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            activeCircuit = editor.newCircuit();
            filename = c.getSelectedFile().getAbsolutePath();
            CircuitFileHandler cfh = new CircuitFileHandler(editor);
            activeCircuit.addComponentList(cfh.loadFile(filename));
            activeCircuit.setFilename(filename);
            editor.refreshWindowsMenu();
            activeCircuit.getParentFrame().setTitle(filename);
        }
    }

    @Override
    public String toString() {
        return "Open Circuit File";
    }

}
