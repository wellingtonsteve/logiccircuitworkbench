/*
 * To change this template, choose Tools | Templates
 * and open the template in the Options.
 */

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
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import ui.command.EditLabelCommand;
import ui.error.ErrorHandler;
import netlist.standard.Input;
import netlist.standard.LED;
import netlist.standard.Wire;
import ui.tools.SelectableComponent;

/**
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
    private String componentName;
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
                editor.getCommandHistory().doCommand(new EditLabelCommand(sc, labelTextbox.getText()));
            }
            
        });
        labelTextbox.addFocusListener(new FocusListener(){

            public void focusGained(FocusEvent e) {}

            public void focusLost(FocusEvent e) {
                editor.getCommandHistory().doCommand(new EditLabelCommand(sc, labelTextbox.getText()));
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

    public JTextField getLabelTextbox() {
        return labelTextbox;
    }
    
    public void setComponent(SelectableComponent sc){
        this.sc = sc;
        setVisible(true);
        sc.setRotation(rotation);
        titleLabel.setText(titleOld);
        typeLabel.setText(sc.getName());
        labelTextbox.setText(sc.getLabel());
        labelLabel.setEnabled(true);
        labelTextbox.setEnabled(true);
        
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
        
        Preview.setComponent(sc);
        setLayoutManager();
        this.repaint();
        editor.getActiveCircuit().repaint();
    }

    public SelectableComponent getSelectableComponent(){
        if(componentName!=null){
            setComponentByName(componentName);
            sc.setRotation(rotation);
            return sc;
        } else {
            ErrorHandler.newError(new ui.error.Error("Component Creation Error","Please select a component from the selection box."));
            return null;
        }
        
    }
    
    public void setComponentByName(String componentName){
        this.componentName = componentName;      
        
        if(componentName!=null && editor!=null){
            try {
                if(editor.isNetlistComponent(componentName)){
                    sc = editor.getNetlistComponent(componentName).getConstructor(CircuitPanel.class, Point.class).newInstance(editor.getActiveCircuit(), new Point(0,0));
                } else {
                    sc = editor.getDefaultNetlistComponent(componentName);
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(OptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(OptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(OptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(OptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(OptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(OptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        sc.setRotation(rotation);
        titleLabel.setText(titleNew);
        typeLabel.setText(sc.getName());
        ledColours.setVisible(sc instanceof LED);
        ledColoursLabel.setVisible(sc instanceof LED);
        sourceIsOn.setVisible(sc instanceof Input);
        labelLabel.setEnabled(true);
        labelTextbox.setEnabled(true);
               
        if(sc instanceof LED){
            ((LED) sc).setValue(true);
            ((LED) sc).setColour((String) ledColours.getSelectedItem());
        }
        if(sc instanceof Input){
            ((Input) sc).setIsOn((boolean) sourceIsOn.isSelected());
        }
        if(sc instanceof Wire){
            labelLabel.setEnabled(false);
            labelTextbox.setEnabled(false);
        }
        
        Preview.setComponent(sc);
        setLayoutManager();
    }

    public void setComponentRotation(double d) {
        this.rotation = d;
        if(sc != null){
            sc.setRotation(rotation);
            Preview.setComponent(sc);
            Preview.repaint();
            editor.getActiveCircuit().removeUnFixedComponents();
            editor.getActiveCircuit().addComponent(sc);
        }
        
    }
    
    private void setLayoutManager(){      
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
