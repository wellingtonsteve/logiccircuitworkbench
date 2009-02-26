package ui.command;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import ui.CircuitPanel;
import ui.Editor;
import ui.file.FileLoader;
import ui.file.CircuitFileFilter;

/**
 *
 * @author matt
 */
public class SubcircuitOpenCommand extends Command {

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
            CircuitPanel loadingCircuit = activeCircuit;
            activeCircuit = editor.createBlankCircuit();    
            if(cfh.loadFile(filename)){
                activeCircuit.setFilename(filename);
                editor.refreshWindowsMenu();
                activeCircuit.getParentFrame().setTitle(filename);
                
                loadingCircuit.addComponent(activeCircuit.asSelectableComponent(loadingCircuit));                            
                activeCircuit.getParentFrame().setVisible(false);
                editor.setActiveCircuit(loadingCircuit);                

            } else {
                // Close bad circuit
                ((JDesktopPane) activeCircuit.getParentFrame().getParent()).remove(activeCircuit.getParentFrame());
            }         
        }
    }

    @Override
    public String getName() {
        return "Load Sub-circuit";
    }

}
