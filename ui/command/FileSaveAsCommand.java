package ui.command;

import java.io.File;
import java.io.FileFilter;
import javax.swing.JFileChooser;
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
        FileFilter xmlFilter = new FileFilter(){

            public boolean accept(File pathname) {
                String ext = getExtension(pathname);
                return ext.equals("xml");
            }         

            private String getExtension(File f) {
                String ext = null;
                String s = f.getName();
                int i = s.lastIndexOf('.');

                if (i > 0 &&  i < s.length() - 1) {
                    ext = s.substring(i+1).toLowerCase();
                }
                return ext;
            }

            public String getDescription() {
                return "XML Files";
            }

        };
        
        //c.addChoosableFileFilter(xmlFilter);
        c.setDialogType(JFileChooser.SAVE_DIALOG);
        int rVal = c.showSaveDialog(editor);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            filename = c.getSelectedFile().getAbsolutePath();
            activeCircuit.saveAs(filename);
        }
    }

    @Override
    public String toString() {
        return "Open Circuit File";
    }

}
