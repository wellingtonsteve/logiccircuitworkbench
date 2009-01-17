package ui;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import ui.command.EditLabelCommand;
import ui.error.ErrorHandler;
import netlist.standard.Input;
import netlist.standard.LED;
import netlist.standard.Wire;
import ui.tools.SelectableComponent;

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
    private JComboBox ledColours = new JComboBox();
    private JCheckBox sourceIsOn = new JCheckBox();
    private JLabel typeLabel = new JLabel();
    private JLabel titleLabel = new JLabel();
    private JLabel blankLabel = new JLabel();
    private SelectableComponent sc = null;
    private Editor editor;
    private String titleNew, titleOld;
    private JLabel ledColoursLabel = new JLabel();
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
                editor.getActiveCircuit().getCommandHistory().doCommand(new EditLabelCommand(sc, labelTextbox.getText()));
            }
            
        });
        labelTextbox.addFocusListener(new FocusListener(){

            public void focusGained(FocusEvent e) {}

            public void focusLost(FocusEvent e) {
                editor.getActiveCircuit().getCommandHistory().doCommand(new EditLabelCommand(sc, labelTextbox.getText()));
            }
            
        });
                        
        labelLabel.setText(bundle.getString("OptionsPanel.labelLabel.text"));
        blankLabel.setText("");

        ledColours.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Yellow","Red","Green" }));
        ledColours.addItemListener(new ItemListener(){

            public void itemStateChanged(ItemEvent e) {
                if(sc instanceof LED){
                    ((LED) sc).setValue(true);
                    ((LED) sc).setColour((String) ledColours.getSelectedItem());
                    Preview.setComponent(sc);
                    repaint();
                }
            }
            
        });
        ledColoursLabel.setText(bundle.getString("OptionsPanel.ledColoursLabel.text"));
        
        sourceIsOn.addItemListener(new ItemListener(){

            public void itemStateChanged(ItemEvent e) {
                if(sc instanceof Input){
                    ((Input) sc).setIsOn((boolean) sourceIsOn.isSelected());
                    Preview.setComponent(sc);
                    repaint();
                }               
            }
            
        });
        sourceIsOn.setText(bundle.getString("OptionsPanel.sourceIsOn.text"));
        
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
     * Get a copy of the component which this options panel displays.
     * 
     * @return
     */
    public SelectableComponent getSelectableComponent(){
        return sc.copy();        
    }
    
    /**
     * Set the component which this panel shows. This component has already been
     * created and will normally come from a selection in the circuit workarea.
     * 
     * @param sc
     */
    public void setComponent(SelectableComponent sc){
        this.sc = sc;
        
        // Update the values in different parts of the form
        setVisible(true);
        sc.setRotation(rotation);
        titleLabel.setText(titleOld);
        typeLabel.setText(sc.getName());
        labelTextbox.setText(sc.getLabel());
        labelLabel.setEnabled(true);
        labelTextbox.setEnabled(true);
        
        // Display component specific layout options
        if(sc instanceof LED){
            ledColours.setSelectedItem(((LED) sc).getColour());
        }
        if(sc instanceof Input){
            sourceIsOn.setSelected(((Input) sc).isOn());
        }
        if(sc instanceof Wire){
            labelLabel.setEnabled(false);
            labelTextbox.setEnabled(false);
        }
        
        // Cascade changes to the preview window itself
        Preview.setComponent(sc);
        setLayoutManager();
        this.repaint();
        editor.getActiveCircuit().repaint();
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
            sc.setRotation(rotation);
            Preview.setComponent(sc);
            Preview.repaint();
            
            // Make the changes to the active circuit also
            editor.getActiveCircuit().removeUnFixedComponents();
            editor.getActiveCircuit().addComponent(sc);
        }
        
    }
    
    /**
     * Get the current rotation of the component displayed in the panel.
     * 
     * @return
     */
    public Double getComponentRotation(){
        return rotation;
    }
    
    /**
     * Get the current colour of the LED colours drop down box.
     * 
     * @return
     */
    public String getLEDColour(){
        return (String) ledColours.getSelectedItem();
    }
    
    /**
     * Get the state of the Input Source.
     * 
     * @return
     */
    public boolean getInputSourceState(){
        return sourceIsOn.isSelected();
    }
    
    /**
     * Layout the Panel. Dependant upon the current component.
     */
    public void setLayoutManager(){      
        org.jdesktop.layout.GroupLayout OptionsLayout = new org.jdesktop.layout.GroupLayout(this);
        
        setLayout(OptionsLayout);
        
        OptionsLayout.setHorizontalGroup(
            OptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, OptionsLayout.createSequentialGroup()
                .add(OptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, OptionsLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(Preview, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, OptionsLayout.createSequentialGroup()
                        .add(typeLabel))
                    .add(OptionsLayout.createSequentialGroup()
                        .add((sc instanceof LED)?ledColoursLabel:blankLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add((sc instanceof LED)?ledColours:blankLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,130))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, OptionsLayout.createSequentialGroup()
                        .add((sc instanceof Input)?sourceIsOn:blankLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, OptionsLayout.createSequentialGroup()
                        .add(titleLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
                    .add(OptionsLayout.createSequentialGroup()
                        .add(labelLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(labelTextbox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 130)))
                     .addContainerGap())
        );
        
        OptionsLayout.setVerticalGroup(
            OptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(OptionsLayout.createSequentialGroup()
                .add(titleLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(typeLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(OptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(labelLabel)
                    .add(labelTextbox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 20))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)      
                .add(OptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add((sc instanceof LED)?ledColoursLabel:blankLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add((sc instanceof LED)?ledColours:blankLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add((sc instanceof Input)?sourceIsOn:blankLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(Preview, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }
      
    
}
