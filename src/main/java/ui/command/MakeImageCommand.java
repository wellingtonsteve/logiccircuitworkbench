package ui.command;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import ui.Editor;
import ui.UIConstants;
import ui.file.JPGFileFilter;

/**
 *
 * @author matt
 */
public class MakeImageCommand extends Command {

    @Override
    protected void perform(Editor editor) {
        String filename;                                    
        JFileChooser c = new JFileChooser();        
        FileFilter jpgFilter = new JPGFileFilter();        
        c.setFileFilter(jpgFilter);
        c.setDialogType(JFileChooser.SAVE_DIALOG);
        int rVal = c.showSaveDialog(editor);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            filename = c.getSelectedFile().getAbsolutePath();
            boolean drawDots = UIConstants.DRAW_GRID_DOTS;
            UIConstants.DRAW_GRID_DOTS = false;
            activeCircuit.removeUnfixedComponents();
            activeCircuit.createImage(filename);
            UIConstants.DRAW_GRID_DOTS = drawDots;
        }
    }

    @Override
    public String getName() {
        return "Make Image";
    }


}
