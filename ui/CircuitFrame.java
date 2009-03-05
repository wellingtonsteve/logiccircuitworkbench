 package ui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyVetoException;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import ui.command.FileSaveCommand;
import ui.error.ErrorHandler;

/**
 * A CircuitFrame contains a CircuitPanel on which the visual circuit is actually
 * drawn. The circuit frame is responsible for scrolling, closing operations and
 * the naming of unnamed circuits.
 * 
 * @author matt
 */
public class CircuitFrame extends JInternalFrame{

    private CircuitPanel circuitPanel;
    private JScrollPane jScrollPane2 = new JScrollPane();
    private Editor editor;
    private int untitledIndex;
    private JFrame propertiesDialog;
    
    public CircuitFrame(final Editor editor, int untitledIndex, boolean isSubCircuit){        
        this.editor = editor;
        this.untitledIndex = untitledIndex;
        this.circuitPanel = new CircuitPanel(this, isSubCircuit);
        this.propertiesDialog = new CircuitProperties(circuitPanel.getProperties());
        this.propertiesDialog.setLocationRelativeTo(this);
        this.propertiesDialog.setVisible(false);
        
        // Set frame properties
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setFrameIcon(null);
        setVisible(true);        
        setTitle("Untitled"+untitledIndex+".xml");
        
        // Set the layout and add the circuit panel
        org.jdesktop.layout.GroupLayout circuitPanelLayout = new org.jdesktop.layout.GroupLayout(circuitPanel);
        circuitPanel.setLayout(null);
        Dimension maxSize = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
        circuitPanelLayout.setHorizontalGroup(
            circuitPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, maxSize.width, Short.MAX_VALUE)
        );
        circuitPanelLayout.setVerticalGroup(
            circuitPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, maxSize.height, Short.MAX_VALUE)
        );
        jScrollPane2.setViewportView(circuitPanel);

        org.jdesktop.layout.GroupLayout circuitFrameLayout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(circuitFrameLayout);
        circuitFrameLayout.setHorizontalGroup(
            circuitFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane2, 0, 785, Short.MAX_VALUE)
        );
        circuitFrameLayout.setVerticalGroup(
            circuitFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane2, 0, 585, Short.MAX_VALUE)
        );

        // Show save dialog when frame is closing?
        // Also tell the editor when a different frame is activated.
        addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) {
                if(isDirty()){
                    int ans = JOptionPane.showConfirmDialog(editor, 
                        "Do you want to save changes to \""+getTitle()+"\" before closing it?");
                    try {
                        if(ans == JOptionPane.YES_OPTION){
                           circuitPanel.doCommand(new FileSaveCommand());
                           dispose();
                        } else if(ans == JOptionPane.NO_OPTION){
                           dispose();
                        } else if(ans == JOptionPane.CANCEL_OPTION){
                            setClosed(false);
                        }
                    } catch (PropertyVetoException ex) {
                        ErrorHandler.newError("Circuit Close Error",
                                "An error occured whilst trying to close the circuit. \n" +
                                "Please see the system output below.", ex);
                    }
                } else {
                    dispose();
                }              
            }
            public void internalFrameActivated(InternalFrameEvent e) {
                editor.setActiveCircuit(circuitPanel);
            }            
        });
        
        pack();
    }
    /**
     * @return The circuit panel that this frame contains
     */
    public CircuitPanel getCircuitPanel(){
        return circuitPanel;
    }
    
    /**
     * Return the number associated with this file is it is Untitled. Untitled 
     * frames have names of the format "UntitledX", where X is a unique number
     * assigned to this frame by the editor upon creation.
     * 
     * @return The untitled number of this frame.
     */
    public int getUntitledIndex() {
        return untitledIndex;
    }

    /**
     * @return The editor that created this frame
     */
    public Editor getEditor() {
        return editor;
    }
    
    /**
     * This frame (circuit) is dirty if since the last save (or creation) no new
     * commands/actions have been performed on it.
     * 
     * @return Has this circuit been saved since the last change?
     */
    public boolean isDirty(){
        return circuitPanel.getCommandHistory().isDirty();
    }
    
    public void openCircuitPreferencesDialog(){
        propertiesDialog.setLocationRelativeTo(this);
        propertiesDialog.setVisible(true);
    }
    
    public void closeCircuitPreferencesDialog(){
        propertiesDialog.setVisible(false);
    }
}