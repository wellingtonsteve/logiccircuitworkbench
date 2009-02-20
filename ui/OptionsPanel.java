package ui;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import ui.command.EditLabelCommand;
import ui.components.SelectableComponent;
import ui.error.ErrorHandler;

/**
 * This panel displays the preview of the currently selected component along with
 * any related options and a label text field, where the label of the current 
 * selection can be created or edited.
 * 
 * The current selection may be either a selection from the circuit editor workarea
 * or from the Component selection tree in the toolbox. The later displaying a 
 * preview of the component that would be added to the workarea.
 * 
 * @author matt
 */
public class OptionsPanel extends JPanel{
    private PreviewPanel Preview;
    private JTextField labelTextbox = new JTextField();
    private JLabel typeLabel = new JLabel();
    private JLabel titleLabel = new JLabel();
    private JPanel componentAttributes = new JPanel();
    private SelectableComponent sc = null;
    private Editor editor;
    private String titleNew, titleOld;
    private JLabel labelLabel = new JLabel();
    private double rotation = 0;

    public OptionsPanel(final Editor editor){
        this.editor = editor;
        
        Preview = new PreviewPanel(editor);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("ui/Bundle"); // NOI18N

        titleNew = bundle.getString("OptionsPanel.titleNew.text"); // NOI18N
        titleOld = bundle.getString("OptionsPanel.titleOld.text"); // NOI18N
        
        labelTextbox.setText(bundle.getString("OptionsPanel.jTextField1.text")); // NOI18N
        labelTextbox.setPreferredSize(new java.awt.Dimension(50, 20));
        labelTextbox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                editor.getActiveCircuit().doCommand(new EditLabelCommand(sc, labelTextbox.getText()));
            }            
        });
        labelTextbox.addFocusListener(new FocusAdapter(){
            public void focusLost(FocusEvent e) {
                editor.getActiveCircuit().doCommand(new EditLabelCommand(sc, labelTextbox.getText()));
            }            
        });
                        
//        labelLabel.setText(bundle.getString("OptionsPanel.labelLabel.text"));
//        blankLabel.setText("");
//
//        ledColours.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Yellow","Red","Green" }));
//        ledColours.addItemListener(new ItemListener(){
//            public void itemStateChanged(ItemEvent e) {
//                if(sc instanceof LED){
//                    ((LED) sc).setValue(true);
//                    ((LED) sc).setColour((String) ledColours.getSelectedItem());
//                    Preview.setComponent(sc);
//                    repaint();
//                }
//            }           
//        });
//        ledColoursLabel.setText(bundle.getString("OptionsPanel.ledColoursLabel.text"));
        
//        sourceIsOn.addItemListener(new ItemListener(){
//
//            public void itemStateChanged(ItemEvent e) {
//                if(sc instanceof Input){
//                    ((Input) sc).setIsOn((boolean) sourceIsOn.isSelected());
//                    Preview.setComponent(sc);
//                    repaint();
//                }               
//            }
//            
//        });
//        sourceIsOn.setText(bundle.getString("OptionsPanel.sourceIsOn.text"));
        
        typeLabel.setText(bundle.getString("OptionsPanel.jLabel2.text")); // NOI18N

        titleLabel.setForeground(new java.awt.Color(108, 108, 108));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText(titleNew);
         
        org.jdesktop.layout.GroupLayout PreviewLayout = new org.jdesktop.layout.GroupLayout(Preview);
        Preview.setLayout(PreviewLayout);
        PreviewLayout.setHorizontalGroup(
            PreviewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 148, Short.MAX_VALUE)
        );
        PreviewLayout.setVerticalGroup(
            PreviewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 88, Short.MAX_VALUE)
        );
        
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        add(titleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, -1, -1));
        add(typeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 25, -1, -1));
        add(labelLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 45, -1, -1));
        add(labelTextbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 45, -1, -1));
        add(componentAttributes, new org.netbeans.lib.awtextra.AbsoluteConstraints(5,80, -1, 100));
        add(Preview, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 180, -1, -1));        
    }

    /**
     * Reset the value of the Component Label Textbox
     */
    public void resetLabel(){
        labelTextbox.setText("");
    }
    
    /**
     * Return the current value of the Component Label Textbox
     * 
     * @return The String of labelTextbox
     */
    public String getCurrentLabel(){
        return labelTextbox.getText();
    }
    
    /**
     * @return a copy of the component which this options panel displays.
     */
    public SelectableComponent getSelectableComponent(){
        return (sc!=null)?sc.copy():null;        
    }
    
    /**
     * Set the component which this panel shows. This component has already been
     * created and will normally come from a selection in the circuit workarea.
     * 
     * @param sc
     */
    public void setComponent(SelectableComponent sc){
        // Protect options panel from badly created components
        if(sc != null){
            this.sc = sc;

            setVisible(true);
            titleLabel.setText((editor.getActiveCircuit().getActiveComponents().contains(sc))?titleOld:titleNew);
            typeLabel.setText(sc.getName());
            labelTextbox.setText(sc.getLabel());
            labelLabel.setEnabled(true);
            labelTextbox.setEnabled(true);
            //componentAttributes = sc.getOptionsPanel();
            //componentAttributes.repaint();
            revalidate();
            // Cascade changes to the preview window itself
            Preview.setComponent(sc);
            this.repaint();
            editor.getActiveCircuit().repaint();
        } else {
            ErrorHandler.newError("Options Panel Error",
                    "The component that you are trying to create or select is malformed.");
        }
    }

    /**
     * Rotate the component that is displayed in this options panel, also force
     * repaints as necessary. Changes to the component here are reflected in the 
     * unfixed component in the workarea.
     * 
     * @param d
     */
    public void setComponentRotation(double d) {
        this.rotation = d;
        if(sc != null){
            sc.setRotation(rotation, false);
            Preview.setComponent(sc);
            Preview.repaint();
        }        
    }
    
    /**
     * @return the current rotation of the component displayed in the panel.
     */
    public Double getComponentRotation(){
        return rotation;
    }
}
