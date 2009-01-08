/*
 * To change this template, choose Tools | Templates
 * and open the template in the Options.
 */

package ui;

import java.awt.event.ItemEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Point;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import ui.tools.Input;
import ui.tools.LED;
import ui.tools.SelectableComponent;

/**
 *
 * @author matt
 */
public class OptionsPanel extends JPanel{
    private PreviewPanel Preview;
    private JTextField labelText = new JTextField();
    private JComboBox ledColours = new JComboBox();
    private JCheckBox sourceIsOn = new JCheckBox();
    private JLabel typeLabel = new JLabel();
    private JLabel titleLabel = new JLabel();
    private JLabel blankLabel = new JLabel();
    private SelectableComponent sc = null;
    private Editor editor;
    private String titleNew, titleOld;

    public OptionsPanel(Editor editor){
        this.editor = editor;
        
        Preview = new PreviewPanel(editor);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("ui/Bundle"); // NOI18N

        titleNew = bundle.getString("OptionsPanel.titleNew.text"); // NOI18N
        titleOld = bundle.getString("OptionsPanel.titleOld.text"); // NOI18N
        
        labelText.setText(bundle.getString("OptionsPanel.jTextField1.text")); // NOI18N
        blankLabel.setText("");

        ledColours.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Yellow","Red","Green" }));
        ledColours.addItemListener(new ItemListener(){

            public void itemStateChanged(ItemEvent e) {
                if(sc instanceof LED){
                    ((LED) sc).setValue(true);
                    ((LED) sc).setColour((String) ledColours.getSelectedItem());
                    Preview.setComponent(sc);
                    Preview.repaint();
                }
            }
            
        });
        
        sourceIsOn.addItemListener(new ItemListener(){

            public void itemStateChanged(ItemEvent e) {
                if(sc instanceof Input){
                    ((Input) sc).setIsOn((boolean) sourceIsOn.isSelected());
                    Preview.setComponent(sc);
                    Preview.repaint();
                }               
            }
            
        });

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
    
    public void setComponent(SelectableComponent sc){
        this.sc = sc;
        titleLabel.setText(titleOld);
        typeLabel.setText(sc.getName());
        labelText.setText(sc.getLabel());
    }

    public SelectableComponent getSelectableComponent(){
        return sc.copy();
    }
    
    public void setComponentByName(String componentName){
        
        titleLabel.setText(titleNew);
        
        if(componentName!=null && editor!=null){
            try {
                Class<SelectableComponent> clazz = editor.getNetlistComponent(componentName);
                sc = clazz.getConstructor(Point.class).newInstance(new Point(0,0));
                //sc = (SelectableComponent) editor.getNetlistComponent(componentName).getConstructor(Point.class).newInstance(new Point(0,0));
            } catch (InstantiationException ex) {
                Logger.getLogger(CircuitPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(CircuitPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(CircuitPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(CircuitPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(CircuitPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(CircuitPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        typeLabel.setText(sc.getName());
        ledColours.setVisible(sc instanceof LED);
        sourceIsOn.setVisible(sc instanceof Input);
               
        if(sc instanceof LED){
            ((LED) sc).setValue(true);
            ((LED) sc).setColour((String) ledColours.getSelectedItem());
        }
        if(sc instanceof Input){
            ((Input) sc).setIsOn((boolean) sourceIsOn.isSelected());
        }
        
        Preview.setComponent(sc.copy());
        setLayoutManager();
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
                    .add(org.jdesktop.layout.GroupLayout.LEADING, OptionsLayout.createSequentialGroup()
                        .add((sc instanceof LED)?ledColours:blankLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, OptionsLayout.createSequentialGroup()
                        .add((sc instanceof Input)?sourceIsOn:blankLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, OptionsLayout.createSequentialGroup()
                        .add(titleLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))

                        .add(org.jdesktop.layout.GroupLayout.LEADING, labelText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 143, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        OptionsLayout.setVerticalGroup(
            OptionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(OptionsLayout.createSequentialGroup()
                .add(titleLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(typeLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(labelText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)      
                .add((sc instanceof LED)?ledColours:blankLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add((sc instanceof Input)?sourceIsOn:blankLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(Preview, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }
      
    
}
