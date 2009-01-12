package ui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import ui.error.ErrorHandler;

/**
 *
 * @author matt
 */
public class Main {

    public static void main(String[] args){
        try {
           UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
           //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
           //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
           //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } 
        catch (Exception e) {
           e.printStackTrace();
        }       
                        
        SplashDialog splash = new SplashDialog();
        splash.setVisible(true);
        
        JProgressBar loadingBar = splash.getProgressBar();
        loadingBar.setString("Loading...");
        loadingBar.setStringPainted(true);
        loadingBar.setMaximum(100);    
              
        Editor editor = new Editor();         

        loadingBar.setValue(30);

        loadingBar.setString("Loading Standard Netlist...");
        editor.addNetlist(new ui.netlist.standard.Standard());
        loadingBar.setValue(40);

        loadingBar.setString("Loading Logic Gates Netlist...");
        editor.addNetlist(new ui.netlist.logicgates.LogicGates());
        loadingBar.setValue(50);

        loadingBar.setString("Creating Blank Circuit...");
        ErrorHandler.addErrorListener(editor);
        editor.newCircuit();
        loadingBar.setValue(80);

        if (UIConstants.DO_OFFSCREEN_DRAWING_TEST) {
            loadingBar.setString("Testing Offscreen Drawing...");
            editor.doAutoDetect();
            loadingBar.setString("Testing Offscreen Drawing: " + ((editor.drawDirect())?"Drawing Directly":"Drawing Offscreen"));
            loadingBar.setValue(100);
        }

        editor.setVisible(true); 
        splash.setVisible(false);    

    }

}
