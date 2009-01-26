package ui;

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
           
            SplashDialog splash = new SplashDialog();
            splash.setVisible(true);

            JProgressBar loadingBar = splash.getProgressBar();
            loadingBar.setString("Loading...");
            loadingBar.setStringPainted(true);
            loadingBar.setMaximum(100);    

            Editor editor = new Editor();   
            ErrorHandler.addErrorListener(editor);

            loadingBar.setValue(30);

            loadingBar.setString("Loading Standard Netlist...");
            editor.addNetlist(new netlist.Standard());
            loadingBar.setValue(40);

            loadingBar.setString("Loading Logic Gates Netlist...");
            editor.addNetlist(new netlist.LogicGates());
            loadingBar.setValue(50);

            loadingBar.setString("Creating Blank Circuit...");
            editor.createBlankCircuit();
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
        catch (Exception e) {
           // TODO: Remove after debugging 
            e.printStackTrace();
            
            // Report unexpected errors to user
            ErrorHandler.newError("Unknown Error","An unexpected error has occured, please see the system error below.", e);
            
        }       
                        
 

    }

}
