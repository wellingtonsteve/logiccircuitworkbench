/*
 * TestJFrameForm.java
 *
 * Created on 07 October 2008, 19:02
 */

package ui;

import ui.tools.UITool;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Event;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 *
 * @author  Matt
 */
public class FrameMain2 extends javax.swing.JFrame {
    
    /** Creates new form TestJFrameForm */
    public FrameMain2() {
    initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator2 = new javax.swing.JSeparator();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        DesktopPane = new javax.swing.JDesktopPane();
        jInternalFrame4 = new javax.swing.JInternalFrame();
        jPanel4 = new javax.swing.JPanel();
        Selection = new javax.swing.JButton();
        Wire = new javax.swing.JButton();
        AndGate = new javax.swing.JButton();
        circuitFrame = new javax.swing.JInternalFrame();
        circuitToolbar = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        delete_selected = new javax.swing.JButton();
        circuitPanel = new CircuitPanel();
        jSeparator1 = new javax.swing.JSeparator();
        statusPanel = new javax.swing.JPanel();
        infoLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(750, 510));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));

        jToolBar1.setRollover(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(750, 23));
        jToolBar1.setMinimumSize(new java.awt.Dimension(750, 23));
        jToolBar1.setPreferredSize(new java.awt.Dimension(750, 23));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("ui/Bundle"); // NOI18N
        jButton1.setText(bundle.getString("TestJFrameForm.jButton1.text_1")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHideActionText(true);
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        getContentPane().add(jToolBar1);

        jPanel1.setAutoscrolls(true);
        jPanel1.setMinimumSize(new java.awt.Dimension(750, 500));
        jPanel1.setPreferredSize(new java.awt.Dimension(750, 500));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        DesktopPane.setAutoscrolls(true);
        DesktopPane.setMinimumSize(new java.awt.Dimension(600, 400));

        jInternalFrame4.setIconifiable(true);
        jInternalFrame4.setResizable(true);
        jInternalFrame4.setTitle(bundle.getString("TestJFrameForm.jInternalFrame4.title")); // NOI18N
        jInternalFrame4.setVisible(true);

        jPanel4.setLayout(new java.awt.GridLayout(1, 3));

        Selection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/buttons/sml_select.png"))); // NOI18N
        Selection.setText(bundle.getString("TestJFrameForm.Selection.text")); // NOI18N
        Selection.setMargin(new java.awt.Insets(2, 2, 2, 2));
        Selection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SelectionMouseClicked(evt);
            }
        });
        Selection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectionActionPerformed(evt);
            }
        });
        jPanel4.add(Selection);

        Wire.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/buttons/sml_wire.png"))); // NOI18N
        Wire.setText(bundle.getString("TestJFrameForm.Wire.text")); // NOI18N
        Wire.setToolTipText(bundle.getString("TestJFrameForm.Wire.toolTipText")); // NOI18N
        Wire.setMargin(new java.awt.Insets(2, 2, 2, 2));
        Wire.setOpaque(false);
        Wire.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                WireMouseClicked(evt);
            }
        });
        jPanel4.add(Wire);

        AndGate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/buttons/sml_andgate.png"))); // NOI18N
        AndGate.setText(bundle.getString("TestJFrameForm.AndGate.text")); // NOI18N
        AndGate.setMargin(new java.awt.Insets(2, 2, 2, 2));
        AndGate.setOpaque(false);
        AndGate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AndGateMouseClicked(evt);
            }
        });
        jPanel4.add(AndGate);

        org.jdesktop.layout.GroupLayout jInternalFrame4Layout = new org.jdesktop.layout.GroupLayout(jInternalFrame4.getContentPane());
        jInternalFrame4.getContentPane().setLayout(jInternalFrame4Layout);
        jInternalFrame4Layout.setHorizontalGroup(
            jInternalFrame4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 120, Short.MAX_VALUE)
            .add(jInternalFrame4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jInternalFrame4Layout.createSequentialGroup()
                    .add(0, 0, Short.MAX_VALUE)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 0, Short.MAX_VALUE)))
            .add(jInternalFrame4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(0, 120, Short.MAX_VALUE))
            .add(jInternalFrame4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(0, 120, Short.MAX_VALUE))
            .add(jInternalFrame4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(0, 120, Short.MAX_VALUE))
        );
        jInternalFrame4Layout.setVerticalGroup(
            jInternalFrame4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 115, Short.MAX_VALUE)
            .add(jInternalFrame4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jInternalFrame4Layout.createSequentialGroup()
                    .add(0, 37, Short.MAX_VALUE)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 37, Short.MAX_VALUE)))
            .add(jInternalFrame4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(0, 115, Short.MAX_VALUE))
            .add(jInternalFrame4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(0, 115, Short.MAX_VALUE))
            .add(jInternalFrame4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(0, 115, Short.MAX_VALUE))
        );

        jInternalFrame4.setBounds(0, 0, 130, 150);
        DesktopPane.add(jInternalFrame4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        circuitFrame.setClosable(true);
        circuitFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        circuitFrame.setIconifiable(true);
        circuitFrame.setMaximizable(true);
        circuitFrame.setResizable(true);
        circuitFrame.setTitle("Breadboard");
        circuitFrame.setPreferredSize(new java.awt.Dimension(600, 450));
        circuitFrame.setVisible(true);
        circuitFrame.getContentPane().setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        circuitToolbar.setRollover(true);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/buttons/circuit_toolbar/clear_circuit.png"))); // NOI18N
        jButton2.setText(bundle.getString("TestJFrameForm.jButton2.text")); // NOI18N
        jButton2.setToolTipText(bundle.getString("TestJFrameForm.jButton2.toolTipText")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        circuitToolbar.add(jButton2);

        delete_selected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/buttons/circuit_toolbar/delete_selected.png"))); // NOI18N
        delete_selected.setText(bundle.getString("TestJFrameForm.delete_selected.text")); // NOI18N
        delete_selected.setToolTipText(bundle.getString("TestJFrameForm.delete_selected.toolTipText")); // NOI18N
        delete_selected.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        delete_selected.setMargin(new java.awt.Insets(1, 1, 1, 1));
        delete_selected.setMaximumSize(new java.awt.Dimension(24, 24));
        delete_selected.setMinimumSize(new java.awt.Dimension(24, 24));
        delete_selected.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        delete_selected.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                delete_selectedMouseClicked(evt);
            }
        });
        circuitToolbar.add(delete_selected);

        circuitFrame.getContentPane().add(circuitToolbar);

        circuitPanel.setPreferredSize(new java.awt.Dimension(700, 400));

        org.jdesktop.layout.GroupLayout circuitPanelLayout = new org.jdesktop.layout.GroupLayout(circuitPanel);
        circuitPanel.setLayout(circuitPanelLayout);
        circuitPanelLayout.setHorizontalGroup(
            circuitPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 700, Short.MAX_VALUE)
        );
        circuitPanelLayout.setVerticalGroup(
            circuitPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );

        circuitFrame.getContentPane().add(circuitPanel);

        jSeparator1.setPreferredSize(new java.awt.Dimension(750, 2));
        circuitFrame.getContentPane().add(jSeparator1);

        infoLabel.setText(bundle.getString("TestJFrameForm.infoLabel.text_1")); // NOI18N

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(infoLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 268, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(infoLabel)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        circuitFrame.getContentPane().add(statusPanel);

        circuitFrame.setBounds(130, 0, 540, 460);
        DesktopPane.add(circuitFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jPanel1.add(DesktopPane);

        getContentPane().add(jPanel1);

        jMenu1.setText(bundle.getString("TestJFrameForm.jMenu1.text_1")); // NOI18N
        jMenuBar1.add(jMenu1);

        jMenuBar1.add(getFileMenu());
        jMenuBar1.add(getEditMenu());
        jMenuBar1.add(getHelpMenu());

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    
        /**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getSaveMenuItem());
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new JMenu();
			editMenu.setText("Edit");
			editMenu.add(getCutMenuItem());
			editMenu.add(getCopyMenuItem());
			editMenu.add(getPasteMenuItem());
		}
		return editMenu;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new JMenuItem();
			exitMenuItem.setText("Exit");
			exitMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return exitMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About");
			aboutMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDialog aboutDialog = getAboutDialog();
					aboutDialog.pack();
					Point loc = jMenuBar1.getLocation();
					loc.translate(20, 20);
					aboutDialog.setLocation(loc);
					aboutDialog.setVisible(true);
				}
			});
		}
		return aboutMenuItem;
	}

	/**
	 * This method initializes aboutDialog	
	 * 	
	 * @return javax.swing.JDialog
	 */
	private JDialog getAboutDialog() {
		if (aboutDialog == null) {
			aboutDialog = new JDialog(this, true);
			aboutDialog.setTitle("About");
			aboutDialog.setContentPane(getAboutContentPane());
		}
		return aboutDialog;
	}

	/**
	 * This method initializes aboutContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAboutContentPane() {
		if (aboutContentPane == null) {
			aboutContentPane = new JPanel();
			aboutContentPane.setLayout(new BorderLayout());
			aboutContentPane.add(getAboutVersionLabel(), BorderLayout.CENTER);
		}
		return aboutContentPane;
	}

	/**
	 * This method initializes aboutVersionLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getAboutVersionLabel() {
		if (aboutVersionLabel == null) {
			aboutVersionLabel = new JLabel();
			aboutVersionLabel.setText("Version 1.0");
			aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return aboutVersionLabel;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getCutMenuItem() {
		if (cutMenuItem == null) {
			cutMenuItem = new JMenuItem();
			cutMenuItem.setText("Cut");
			cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
					Event.CTRL_MASK, true));
		}
		return cutMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getCopyMenuItem() {
		if (copyMenuItem == null) {
			copyMenuItem = new JMenuItem();
			copyMenuItem.setText("Copy");
			copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
					Event.CTRL_MASK, true));
		}
		return copyMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getPasteMenuItem() {
		if (pasteMenuItem == null) {
			pasteMenuItem = new JMenuItem();
			pasteMenuItem.setText("Paste");
			pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
					Event.CTRL_MASK, true));
		}
		return pasteMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getSaveMenuItem() {
		if (saveMenuItem == null) {
			saveMenuItem = new JMenuItem();
			saveMenuItem.setText("Save");
			saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
					Event.CTRL_MASK, true));
		}
		return saveMenuItem;
	}
    
private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jButton1ActionPerformed

private void SelectionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SelectionMouseClicked
    circuitFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    toggleToolboxButton(Selection);
    ((CircuitPanel) circuitPanel).selectTool(UITool.Select);
}//GEN-LAST:event_SelectionMouseClicked

private void AndGateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AndGateMouseClicked
    circuitFrame.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    ((CircuitPanel) circuitPanel).selectTool(UITool.AndGate2Input);
    toggleToolboxButton(AndGate);
}//GEN-LAST:event_AndGateMouseClicked

private void WireMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_WireMouseClicked
    circuitFrame.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    ((CircuitPanel) circuitPanel).selectTool(UITool.Wire);
    toggleToolboxButton(Wire);
}//GEN-LAST:event_WireMouseClicked

private void delete_selectedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_delete_selectedMouseClicked
    
    if(((CircuitPanel) circuitPanel).hasActiveSelection()){
        int ans = JOptionPane.showConfirmDialog(this,
                "Are you sure that you want to delete the selected component?");
    
        if(ans == JOptionPane.YES_OPTION){
            infoLabel.setText(((CircuitPanel) circuitPanel).deleteActiveComponents());
        }
    } 
        
        
}//GEN-LAST:event_delete_selectedMouseClicked

private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
    int ans = JOptionPane.showConfirmDialog(this, 
        "Are you sure that you want to clear this circuit?");
    
    if(ans == JOptionPane.YES_OPTION){
        infoLabel.setText(((CircuitPanel) circuitPanel).resetCircuit());
    }
}//GEN-LAST:event_jButton2MouseClicked

private void SelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectionActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_SelectionActionPerformed
    
private void toggleToolboxButton(JButton b){
    // Reset Selections
    Selection.setSelected(false);
    Wire.setSelected(false);
    AndGate.setSelected(false);    
    
    // Select this button
    b.setSelected(true);
}


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       // TODO case analysis for non-windows environments
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } 
        catch (Exception e) {
           e.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameMain2().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AndGate;
    private javax.swing.JDesktopPane DesktopPane;
    private javax.swing.JButton Selection;
    private javax.swing.JButton Wire;
    private javax.swing.JInternalFrame circuitFrame;
    private javax.swing.JPanel circuitPanel;
    private javax.swing.JToolBar circuitToolbar;
    private javax.swing.JButton delete_selected;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JInternalFrame jInternalFrame4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JMenu fileMenu = null;
    private javax.swing.JMenu editMenu = null;
    private javax.swing.JMenu helpMenu = null;
    private javax.swing.JMenuItem exitMenuItem = null;
    private javax.swing.JMenuItem aboutMenuItem = null;
    private javax.swing.JMenuItem cutMenuItem = null;
    private javax.swing.JMenuItem copyMenuItem = null;
    private javax.swing.JMenuItem pasteMenuItem = null;
    private javax.swing.JMenuItem saveMenuItem = null;
    private javax.swing.JDialog aboutDialog = null;
    private javax.swing.JPanel aboutContentPane = null;
    private javax.swing.JLabel aboutVersionLabel = null;
    
}
