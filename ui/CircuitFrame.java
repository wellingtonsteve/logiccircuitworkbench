/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 *
 * @author matt
 */
public class CircuitFrame extends JInternalFrame{

    private CircuitPanel circuitPanel;
    private JScrollPane jScrollPane2 = new JScrollPane();
    private Editor editor;
    private int untitledIndex;
    
    public CircuitFrame(final Editor editor, int untitledIndex){
        
        super();
        
        this.editor = editor;
        this.untitledIndex = untitledIndex;
        this.circuitPanel = new CircuitPanel();

        setClosable(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        setIconifiable(true);

        setMaximizable(true);

        setResizable(true);

        setFrameIcon(null);

        setPreferredSize(new java.awt.Dimension(600, 450));

        setVisible(true);
        
        setTitle("Untitled"+untitledIndex);

        circuitPanel.setParentFrame(this);
        circuitPanel.setMinimumSize(new java.awt.Dimension(1000, 800));
        circuitPanel.setPreferredSize(new java.awt.Dimension(1000, 800));

        org.jdesktop.layout.GroupLayout circuitPanelLayout = new org.jdesktop.layout.GroupLayout(circuitPanel);
        circuitPanel.setLayout(circuitPanelLayout);
        circuitPanelLayout.setHorizontalGroup(
            circuitPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1000, Short.MAX_VALUE)
        );
        circuitPanelLayout.setVerticalGroup(
            circuitPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 800, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(circuitPanel);

        org.jdesktop.layout.GroupLayout circuitFrameLayout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(circuitFrameLayout);
        circuitFrameLayout.setHorizontalGroup(
            circuitFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
        );
        circuitFrameLayout.setVerticalGroup(
            circuitFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
        );
        
        addInternalFrameListener(new InternalFrameListener(){

            public void internalFrameOpened(InternalFrameEvent e) {
                
            }

            public void internalFrameClosing(InternalFrameEvent e) {
                
            }

            public void internalFrameClosed(InternalFrameEvent e) {
                
            }

            public void internalFrameIconified(InternalFrameEvent e) {
                
            }

            public void internalFrameDeiconified(InternalFrameEvent e) {
                
            }

            public void internalFrameActivated(InternalFrameEvent e) {
                if(!isSelected()){ editor.setActiveCircuit(circuitPanel);}
            }

            public void internalFrameDeactivated(InternalFrameEvent e) {
                
            }
            
        });
        
        setBounds(180, 0, 780, 600);

    }
    
    public CircuitPanel getCircuitPanel(){
        return circuitPanel;
    }
    
    public int getUntitledIndex() {
        return untitledIndex;
    }

    public Editor getEditor() {
        return editor;
    }
}
