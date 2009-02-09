package ui.command;

import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import ui.Editor;
import ui.file.FileLoader;
import ui.file.CircuitFileFilter;
import ui.components.SelectableComponent;

/**
 *
 * @author matt
 */
public class FileOpenCommand extends Command {

    @Override
    protected void perform(Editor editor) {
        String filename;                                    
        JFileChooser c = new JFileChooser();
        FileFilter xmlFilter = new CircuitFileFilter();        
        c.setFileFilter(xmlFilter);
        c.setDialogType(JFileChooser.OPEN_DIALOG);
        int rVal = c.showOpenDialog(editor);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            
            filename = c.getSelectedFile().getAbsolutePath();
            FileLoader cfh = new FileLoader(editor);
            
            activeCircuit = editor.createBlankCircuit();    
            if(cfh.loadFile(filename)){
                List<SelectableComponent> fileComponents = cfh.getStack();
                activeCircuit.addComponentList(fileComponents);
                activeCircuit.setFilename(filename);
                editor.refreshWindowsMenu();
                activeCircuit.getParentFrame().setTitle(filename);
                parentHistory.stageChange("message", getName() + ": " + filename);
            } else {
                // Close bad circuit
                ((JDesktopPane) activeCircuit.getParentFrame().getParent()).remove(activeCircuit.getParentFrame());
            }         
        }
    }

    @Override
    public String getName() {
        return "Opened Circuit File";
    }

}
