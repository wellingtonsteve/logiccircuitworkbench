package ui.file;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/** This class represents a file filter for use by the file chooser. It limits
 * the files displayed only to those with an "xml" file extension.*/
public class CircuitFileFilter extends FileFilter {
    public boolean accept(File pathname) {
        if (pathname.isDirectory()) {return true;}
        String extension = getExtension(pathname);
        return (extension != null && extension.equals(ui.UIConstants.FILE_EXTENSION.substring(1)));
    }

    public String getDescription() {
        return "Circuit File (*."+ui.UIConstants.FILE_EXTENSION.substring(1)+")";
    }
    
    private String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}