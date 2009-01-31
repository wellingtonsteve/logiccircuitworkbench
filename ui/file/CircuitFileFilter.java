/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.file;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * This class represents a file filter for use by the file chooser. It limits
 * the files displayed only to those with an "xml" file extension.
 */
public class CircuitFileFilter extends FileFilter {
        /**
         * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
         */
        public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                        return true;
                }

                String extension = getExtension(pathname);
                if (extension != null && extension.equals(ui.UIConstants.FILE_EXTENSION.substring(1))) {
                        return true;
                } else {
                        return false;
                }
        }

        /**
         * @see javax.swing.filechooser.FileFilter#getDescription()
         */
        public String getDescription() {
                return "Circuit File (*."+ui.UIConstants.FILE_EXTENSION.substring(1)+")";
        }

        /**
         * @param f
         * @return the extension of the specified File
         */
        public String getExtension(File f) {
                String ext = null;
                String s = f.getName();
                int i = s.lastIndexOf('.');

                if (i > 0 && i < s.length() - 1) {
                        ext = s.substring(i + 1).toLowerCase();
                }
                return ext;
        }
}

