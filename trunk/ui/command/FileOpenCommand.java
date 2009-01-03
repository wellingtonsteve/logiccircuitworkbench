package ui.command;

import javax.swing.JFileChooser;
import ui.Editor;
import ui.file.CircuitFileHandler;

/**
 *
 * @author matt
 */
public class FileOpenCommand extends Command {

    @Override
    protected void perform(Editor editor) {
        String filename;                                    
        JFileChooser c = new JFileChooser();
        c.setDialogType(JFileChooser.OPEN_DIALOG);
        int rVal = c.showOpenDialog(editor);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            filename = c.getSelectedFile().getAbsolutePath();
            CircuitFileHandler cfh = new CircuitFileHandler();
            activeCircuit.addComponentList(cfh.loadFile(filename));
        }
    }

    @Override
    public String toString() {
        return "Open Circuit File";
    }

}
