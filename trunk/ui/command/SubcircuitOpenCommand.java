package ui.command;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import netlist.properties.Properties;
import sim.SimItem;
import ui.CircuitPanel;
import ui.Editor;
import ui.UIConstants;
import ui.components.SelectableComponent;
import ui.components.SelectionState;
import ui.components.VisualComponent;
import ui.file.FileLoader;
import ui.file.CircuitFileFilter;

/**
 *
 * @author matt
 */
public class SubcircuitOpenCommand extends Command {

    @Override
    protected void perform(Editor editor) {
        // Choose the file
        String filename;                                    
        JFileChooser c = new JFileChooser();
        FileFilter xmlFilter = new CircuitFileFilter();        
        c.setApproveButtonText("Insert");
        c.setDialogTitle("Insert Subcomponent");
        c.setFileFilter(xmlFilter);
        c.setDialogType(JFileChooser.OPEN_DIALOG);
        int rVal = c.showOpenDialog(editor);
        if (rVal == JFileChooser.APPROVE_OPTION) {            
            // Create the subcircuit
            filename = c.getSelectedFile().getAbsolutePath();
            createSubcircuitComponent(editor, filename);
        }
    }

    @Override
    public String getName() {
        return "Load Sub-circuit";
    }

    private SelectableComponent createSubcircuitComponent(Editor editor, String filename) {
        FileLoader cfh = new FileLoader(editor);
        CircuitPanel loadingCircuit = activeCircuit;
        activeCircuit = editor.createBlankCircuit(true);
        if (cfh.loadFile(filename)) {
            activeCircuit.setFilename(filename);

            // Get the things we need from the circuit panel
            activeCircuit.updateProperties();
            Properties properties = activeCircuit.getProperties();
            SimItem logicalCircuit = activeCircuit.getLogicalCircuit();
            logicalCircuit.setProperties(properties);
            activeCircuit.selectAllComponents();

            // Create a new component
            VisualComponent subcircuitComponent = new SubcircuitComponent(
                    loadingCircuit, new Point(0, 0), logicalCircuit, properties);
            loadingCircuit.addComponent(subcircuitComponent);
            subcircuitComponent.addLogicalComponentToCircuit();

            activeCircuit.getParentFrame().doDefaultCloseAction();
            activeCircuit.getParentFrame().dispose();
            editor.setActiveCircuit(loadingCircuit);
            loadingCircuit.repaint();
            editor.setComponent(subcircuitComponent);
            
            return subcircuitComponent;
        } else {
            // Close bad circuit
            ((JDesktopPane) activeCircuit.getParentFrame().getParent()).remove(activeCircuit.getParentFrame());
        }
        return null;
    }

    public class SubcircuitComponent extends VisualComponent {

        public SubcircuitComponent(CircuitPanel parent, Point point, SimItem logicalComponent, Properties properties) {
            super(parent, point, logicalComponent, properties);
        }
        private int width = 0;
        private int height = 0;
        private String filename = activeCircuit.getFilename();

        @Override
        protected void setBoundingBox() {
            boundingBox = new java.awt.Rectangle(getOrigin().x - getCentre().x,
                    getOrigin().y - getCentre().y, getWidth(), getHeight());
            boundingBox = rotate(boundingBox);
        }

        @Override
        public int getWidth() {
            if (width == 0) {
                int maxX = 0;
                for (Point p : properties.getInputPins().values()) {
                    if (p.x > maxX) {
                        maxX = p.x;
                    }
                }
                for (Point p : properties.getOutputPins().values()) {
                    if (p.x > maxX) {
                        maxX = p.x;
                    }
                }
                width = Math.max(maxX, 30);
            }
            return width;
        }

        @Override
        public int getHeight() {
            if (height == 0) {
                int maxY = 0;
                for (Point p : properties.getInputPins().values()) {
                    if (p.y > maxY) {
                        maxY = p.y;
                    }
                }
                for (Point p : properties.getOutputPins().values()) {
                    if (p.y > maxY) {
                        maxY = p.y;
                    }
                }
                height = Math.max(maxY+5, 30);
            }
            return height;
        }

        @Override
        protected void setInvalidAreas() {
            invalidArea = new java.awt.Rectangle(getOrigin().x - getCentre().x + 14, 
                    getOrigin().y - getCentre().y - 1, getWidth() - 25 + 2, getHeight() +2);
            invalidArea = rotate(invalidArea);
        }

        @Override
        public Point getCentre() {
            return new Point(20, 20);
        }

        @Override
        public void draw(Graphics2D g) {
            if (hasLabel()) {
                g.setColor(UIConstants.LABEL_TEXT_COLOUR);
                g.drawString(getLabel(), getOrigin().x, getOrigin().y - 2);
            }

            g.rotate(rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
            g.translate(getOrigin().x, getOrigin().y);
            g.setStroke(new BasicStroke(1.0f));

            if (properties.getImage("default") != null) {
                g.drawImage(properties.getImage("default"), 0, 0, null);
            } else {
                if (getSelectionState().equals(SelectionState.ACTIVE)) {
                    g.setColor(UIConstants.ACTIVE_COMPONENT_COLOUR);
                    g.drawRect(15, 0, getWidth() - 25, getHeight());
                } else if (getSelectionState().equals(SelectionState.HOVER)) {
                    g.setColor(UIConstants.CIRCUIT_BACKGROUND_COLOUR);
                    g.fillRect(15, 0, getWidth() - 25, getHeight());
                    g.setColor(UIConstants.HOVER_COMPONENT_COLOUR);
                    g.drawRect(15, 0, getWidth() - 25, getHeight());
                } else {
                    g.setColor(UIConstants.CIRCUIT_BACKGROUND_COLOUR);
                    g.fillRect(15, 0, getWidth() - 25, getHeight());
                    g.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);
                    g.drawRect(15, 0, getWidth() - 25, getHeight());
                }
            }

            g.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);
            //String name = (String) properties.getAttribute("Title").getValue();
            //g.drawString(name, 10, 10);

            for (Pin p : localPins) {
                if (p.getJoinable() instanceof sim.joinable.InputPin) {
                    String label = ((sim.joinable.InputPin)p.getJoinable()).getName();
                    g.drawString(label, p.x + (2 * UIConstants.GRID_DOT_SPACING)+1, p.y+3);
                    g.drawLine(p.x, p.y, p.x + (2 * UIConstants.GRID_DOT_SPACING), p.y);
                } else if (p.getJoinable() instanceof sim.joinable.OutputPin) {
                    String label = ((sim.joinable.OutputPin)p.getJoinable()).getName();
                    g.drawString(label, p.x - (2 * UIConstants.GRID_DOT_SPACING)-8, p.y+3);
                    g.drawLine(p.x, p.y, p.x - (2 * UIConstants.GRID_DOT_SPACING), p.y);
                }
            }

            g.translate(-getOrigin().x, -getOrigin().y);
            g.rotate(-rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
        }

        @Override
        protected void setDefaultImage() {
            defaultBi = null;
        }

        @Override
        protected void setSelectedImage() {
            selectedBi = null;
        }

        @Override
        protected void setActiveImage() {
            activeBi = null;
        }
        
        public void openEditor(){
            Editor editor = getParent().getParentFrame().getEditor();
            FileLoader cfh = new FileLoader(editor);
            CircuitPanel subCircuit = editor.createBlankCircuit(true);    
            if(cfh.loadFile(filename)){
                subCircuit.setFilename(filename);
                subCircuit.setSubcircuitParent(parent);
            } else {
                // Close bad circuit
                ((JDesktopPane) activeCircuit.getParentFrame().getParent()).remove(activeCircuit.getParentFrame());
            }         
        }
        
        public void updateSource(CircuitPanel cp){
            SelectableComponent sc = createSubcircuitComponent(getParent().getParentFrame().getEditor(), cp.getFilename());
            setProperties(sc.getProperties());
            parent.getLogicalCircuit().removeSimItem(logicalComponent);
            this.logicalComponent = sc.getLogicalComponent();
            addLogicalComponentToCircuit();
            height = 0;
            width = 0;
            unsetGlobalPins();
            setLocalPins();
            setGlobalPins();
        }
    }

}