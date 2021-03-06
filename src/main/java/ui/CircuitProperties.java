package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import netlist.properties.Attribute;
import netlist.properties.AttributeListener;
import netlist.properties.Properties;

/**
 *
 * @author  matt
 */
public class CircuitProperties extends javax.swing.JFrame implements AttributeListener {
    private Properties properties;

    /** Creates new form CircuitProperties */
    public CircuitProperties(Properties properties) {
        this.properties = properties;
        initComponents();
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMM yyyy',' HH:mm");
        AuthorField.setText(System.getProperty("user.name")); 
        ModifiedField.setText(sdf.format(cal.getTime()));
        
        properties.getAttribute("Title").addAttributeListener(this);
        properties.getAttribute("Description").addAttributeListener(this);
        properties.getAttribute("Subcircuit Image").addAttributeListener(this);
        properties.getAttribute("Subcircuit Width").addAttributeListener(this);
        properties.getAttribute("Subcircuit Height").addAttributeListener(this);
        
        if(properties.getAttribute("Subcircuit Image").getValue() != null){
            FileChooser.setSelectedFile(new File((String) properties.getAttribute("Subcircuit Image").getValue()));
        }
        FileChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(FileChooser.getSelectedFile()!=null){
                    ImageField.setText(FileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        FileChooser = new javax.swing.JFileChooser();
        TitleField = new javax.swing.JTextField();
        TitleLabel = new javax.swing.JLabel();
        ImageLabel = new javax.swing.JLabel();
        ImageField = new javax.swing.JTextField();
        ImageDialogButton = new javax.swing.JButton();
        DescScrollPane = new javax.swing.JScrollPane();
        DescTextArea = new javax.swing.JTextArea();
        DescLabel = new javax.swing.JLabel();
        AuthorLabel = new javax.swing.JLabel();
        AuthorField = new javax.swing.JTextField();
        ModifiedField = new javax.swing.JTextField();
        ModifiedLabel = new javax.swing.JLabel();
        CancelButton = new javax.swing.JButton();
        OkButton = new javax.swing.JButton();
        Title = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        WidthSpinner = new javax.swing.JSpinner();
        WidthLabel = new javax.swing.JLabel();
        HeightSpinner = new javax.swing.JSpinner();
        Height = new javax.swing.JLabel();

        FileChooser.setName("FileChooser"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("ui/Bundle"); // NOI18N
        setTitle(bundle.getString("CircuitProperties.title")); // NOI18N
        setAlwaysOnTop(true);
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        TitleField.setName("TitleField"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 4);
        getContentPane().add(TitleField, gridBagConstraints);

        TitleLabel.setText(bundle.getString("CircuitProperties.TitleLabel.text")); // NOI18N
        TitleLabel.setName("TitleLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        getContentPane().add(TitleLabel, gridBagConstraints);

        ImageLabel.setText(bundle.getString("CircuitProperties.ImageLabel.text")); // NOI18N
        ImageLabel.setName("ImageLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        getContentPane().add(ImageLabel, gridBagConstraints);

        ImageField.setName("ImageField"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 3);
        getContentPane().add(ImageField, gridBagConstraints);

        ImageDialogButton.setText(bundle.getString("CircuitProperties.ImageDialogButton.text")); // NOI18N
        ImageDialogButton.setName("ImageDialogButton"); // NOI18N
        ImageDialogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImageDialogButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 3);
        getContentPane().add(ImageDialogButton, gridBagConstraints);

        DescScrollPane.setName("DescScrollPane"); // NOI18N
        DescScrollPane.setPreferredSize(new java.awt.Dimension(150, 60));

        DescTextArea.setColumns(20);
        DescTextArea.setLineWrap(true);
        DescTextArea.setRows(4);
        DescTextArea.setName("DescTextArea"); // NOI18N
        DescScrollPane.setViewportView(DescTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        getContentPane().add(DescScrollPane, gridBagConstraints);

        DescLabel.setText(bundle.getString("CircuitProperties.DescLabel.text")); // NOI18N
        DescLabel.setName("DescLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        getContentPane().add(DescLabel, gridBagConstraints);

        AuthorLabel.setText(bundle.getString("CircuitProperties.AuthorLabel.text")); // NOI18N
        AuthorLabel.setName("AuthorLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        getContentPane().add(AuthorLabel, gridBagConstraints);

        AuthorField.setEnabled(false);
        AuthorField.setName("AuthorField"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        getContentPane().add(AuthorField, gridBagConstraints);

        ModifiedField.setEnabled(false);
        ModifiedField.setName("ModifiedField"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        getContentPane().add(ModifiedField, gridBagConstraints);

        ModifiedLabel.setText(bundle.getString("CircuitProperties.ModifiedLabel.text")); // NOI18N
        ModifiedLabel.setName("ModifiedLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        getContentPane().add(ModifiedLabel, gridBagConstraints);

        CancelButton.setText(bundle.getString("CircuitProperties.CancelButton.text")); // NOI18N
        CancelButton.setName("CancelButton"); // NOI18N
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        getContentPane().add(CancelButton, gridBagConstraints);

        OkButton.setText(bundle.getString("CircuitProperties.OkButton.text")); // NOI18N
        OkButton.setName("OkButton"); // NOI18N
        OkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        getContentPane().add(OkButton, gridBagConstraints);

        Title.setFont(new java.awt.Font("DejaVu Sans", 0, 18));
        Title.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Title.setText(bundle.getString("CircuitProperties.Title.text")); // NOI18N
        Title.setName("Title"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        getContentPane().add(Title, gridBagConstraints);

        jSeparator1.setName("jSeparator1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        getContentPane().add(jSeparator1, gridBagConstraints);

        WidthSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(40), Integer.valueOf(10), null, Integer.valueOf(5)));
        WidthSpinner.setName("WidthSpinner"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        getContentPane().add(WidthSpinner, gridBagConstraints);

        WidthLabel.setText(bundle.getString("CircuitProperties.WidthLabel.text")); // NOI18N
        WidthLabel.setName("WidthLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        getContentPane().add(WidthLabel, gridBagConstraints);

        HeightSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(40), Integer.valueOf(10), null, Integer.valueOf(5)));
        HeightSpinner.setName("HeightSpinner"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        getContentPane().add(HeightSpinner, gridBagConstraints);

        Height.setText(bundle.getString("CircuitProperties.Height.text")); // NOI18N
        Height.setName("Height"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        getContentPane().add(Height, gridBagConstraints);

        pack();
    }//GEN-END:initComponents
    
private void OkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkButtonActionPerformed
    properties.getAttribute("Title").changeValue(TitleField.getText());
    properties.getAttribute("Description").changeValue(DescTextArea.getText());
    properties.getAttribute("Subcircuit Image").changeValue(ImageField.getText());
    properties.getAttribute("Subcircuit Width").changeValue(WidthSpinner.getValue());
    properties.getAttribute("Subcircuit Height").changeValue(HeightSpinner.getValue());
    setVisible(false);
}//GEN-LAST:event_OkButtonActionPerformed

private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
    setVisible(false);
}//GEN-LAST:event_CancelButtonActionPerformed

private void ImageDialogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImageDialogButtonActionPerformed
    FileChooser.showOpenDialog(this);
}//GEN-LAST:event_ImageDialogButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AuthorField;
    private javax.swing.JLabel AuthorLabel;
    private javax.swing.JButton CancelButton;
    private javax.swing.JLabel DescLabel;
    private javax.swing.JScrollPane DescScrollPane;
    private javax.swing.JTextArea DescTextArea;
    private javax.swing.JFileChooser FileChooser;
    private javax.swing.JLabel Height;
    private javax.swing.JSpinner HeightSpinner;
    private javax.swing.JButton ImageDialogButton;
    private javax.swing.JTextField ImageField;
    private javax.swing.JLabel ImageLabel;
    private javax.swing.JTextField ModifiedField;
    private javax.swing.JLabel ModifiedLabel;
    private javax.swing.JButton OkButton;
    private javax.swing.JLabel Title;
    private javax.swing.JTextField TitleField;
    private javax.swing.JLabel TitleLabel;
    private javax.swing.JLabel WidthLabel;
    private javax.swing.JSpinner WidthSpinner;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables

    public void attributeValueChanged(Attribute attr, Object value) {
        if(attr.getName().equals("Title")){
            TitleField.setText((String)value);
        } else if(attr.getName().equals("Description")){
            DescTextArea.setText((String)value);
        } else if(attr.getName().equals("Subcircuit Image")){
            ImageField.setText((String)value);
        } else if(attr.getName().equals("Subcircuit Width")){
            WidthSpinner.setValue((Integer) value);
        } else if(attr.getName().equals("Subcircuit Height")){
            HeightSpinner.setValue((Integer) value);
        }
    }
}
