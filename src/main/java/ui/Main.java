package ui;

import javax.swing.JProgressBar;
import javax.swing.UIManager;
import ui.error.ErrorHandler;

/**
 * This class properly initialises all the components of the application. Setting
 * the look and feel, opening the splash screen, loading netlists, registering error
 * reporters, testing double buffering and opening the editor with a blank circuit
 * 
 * @author matt
 */
public class Main {

    public static void main(String[] args){
       try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Slider.paintValue", Boolean.FALSE);
            
            JProgressBar loadingBar = new JProgressBar();
            loadingBar.setString("Loading...");
            loadingBar.setStringPainted(true);
            loadingBar.setMaximum(100);    
            
            SplashDialog splash = new SplashDialog(loadingBar);
            splash.setVisible(true);               

            Editor editor = new Editor();   
            ErrorHandler.addErrorListener(editor);

            loadingBar.setValue(30);

            loadingBar.setString("Loading Standard Netlist...");
            editor.addNetlist(new netlist.Standard());
            loadingBar.setValue(40);

            loadingBar.setString("Loading Logic Gates Netlist...");
            editor.addNetlist(new netlist.LogicGates());
            loadingBar.setValue(50);

            loadingBar.setString("Loading Flip-Flops Netlist...");
            editor.addNetlist(new netlist.FlipFlops());
            loadingBar.setValue(60);

            loadingBar.setString("Loading Latches Netlist...");
            editor.addNetlist(new netlist.Latches());
            loadingBar.setValue(70);
            
            loadingBar.setString("Creating Blank Circuit...");
            editor.createBlankCircuit(false);
            loadingBar.setValue(85);

            if (UIConstants.DO_OFFSCREEN_DRAWING_TEST) {
                loadingBar.setString("Speed Drawing Test...");
                editor.doAutoDetect();
                loadingBar.setString("Speed Drawing Test: " + ((editor.drawDirect())?"Drawing Directly":"Drawing Offscreen"));
                loadingBar.setValue(100);
            }
            
            splash.setVisible(false);      
            editor.setVisible(true);   
                     
        } 
        catch (Exception e) {          
            // Report unexpected errors to user
            ErrorHandler.newError("Unknown Error","An unexpected error has occured, please see the system error below.", e);            
        }       
    }
}
