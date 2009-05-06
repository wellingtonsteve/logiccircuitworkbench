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
import ui.error.ErrorHandler;
import ui.file.FileLoader;
import ui.file.CircuitFileFilter;

/**
 * New subcircuits are created by opening the file containing the subcircuit as if it 
 * were a normal open, collecting its properties and logical description and then 
 * creating a visual component from that data.
 * 
 * @author matt
 */
public class SubcircuitOpenCommand extends Command {

    @Override
    protected void perform(Editor editor) {
        // Choose the file, and  set attributes of dialog
        String filename;                                    
        JFileChooser c = new JFileChooser();
        FileFilter xmlFilter = new CircuitFileFilter();        
        c.setApproveButtonText("Insert");
        c.setDialogTitle("Insert Subcomponent");
        c.setFileFilter(xmlFilter);
        c.setDialogType(JFileChooser.OPEN_DIALOG);
        int rVal = c.showOpenDialog(editor);
        if (rVal == JFileChooser.APPROVE_OPTION) {                        
            filename = c.getSelectedFile().getAbsolutePath();
            if(!filename.equals(activeCircuit.getFilename())){
                // Create the subcircuit
                CircuitPanel loadingCircuit = activeCircuit;
                loadingCircuit.setCurrentTool(filename);
                SelectableComponent sc = createSubcircuitComponent(editor, filename, activeCircuit);
                loadingCircuit.addComponent(sc);
                loadingCircuit.repaint();
            // Do not allow self recursion
            } else {
                ErrorHandler.newError("Subcomponent Insertion Error", "Cannot add a subcomponent to itself.");
            }
        }
    }

    @Override
    public String getName() {
        return "Load Sub-circuit";
    }

    public SelectableComponent createSubcircuitComponent(Editor editor, String filename, CircuitPanel loadingCircuit) {
        FileLoader cfh = new FileLoader(editor);
        activeCircuit = editor.createBlankCircuit(true);
        if (cfh.loadFile(filename)) {
            activeCircuit.setFilename(filename);

            // Get the things we need from the circuit panel
            activeCircuit.updateProperties();
            Properties properties = activeCircuit.getProperties();
            SimItem logicalCircuit = activeCircuit.getLogicalCircuit();
            logicalCircuit.setProperties(properties);

            // Create a new component
            VisualComponent subcircuitComponent = new SubcircuitComponent(
                    loadingCircuit, SelectableComponent.getDefaultOrigin(), logicalCircuit, properties);
            subcircuitComponent.addLogicalComponentToCircuit();
            
            // Tidy up
            activeCircuit.getCommandHistory().clearHistory();
            activeCircuit.getParentFrame().doDefaultCloseAction();
            activeCircuit.getParentFrame().dispose();
            editor.refreshWindowsMenu();
            editor.setActiveCircuit(loadingCircuit);
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
        public int getWidth() {
            if (width == 0) {
                width = (Integer) properties.getAttribute("Subcircuit Width").getValue();
            }
            return width+30;
        }

        @Override
        public int getHeight() {
            if (height == 0) {
                height = (Integer) properties.getAttribute("Subcircuit Height").getValue();
            }
            return height+30;
        }

        @Override
        protected void setInvalidAreas() {
            invalidArea = new java.awt.Rectangle(getOrigin().x + 14, 
                    getOrigin().y+ 14, getWidth() - 28, getHeight() -28);
            invalidArea = rotate(invalidArea);
        }

        @Override
        public Point getCentre() {
            return new Point(35, 35);
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
                    g.drawRect(15, 15, getWidth() - 30, getHeight()-30);
                } else if (getSelectionState().equals(SelectionState.HOVER)) {
                    g.setColor(UIConstants.CIRCUIT_BACKGROUND_COLOUR);
                    g.fillRect(15, 15, getWidth() - 30, getHeight()-30);
                    g.setColor(UIConstants.HOVER_COMPONENT_COLOUR);
                    g.drawRect(15, 15, getWidth() - 30, getHeight()-30);
                } else {
                    g.setColor(UIConstants.CIRCUIT_BACKGROUND_COLOUR);
                    g.fillRect(15, 15, getWidth() - 30, getHeight()-30);
                    g.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);
                    g.drawRect(15, 15, getWidth() - 30, getHeight()-30);
                }
            }

            g.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);

            for (Pin p : localPins) {
                String label = new String();
                if (p.getJoinable() instanceof sim.joinable.InputPin) {
                    label = ((sim.joinable.InputPin)p.getJoinable()).getName();                    
                } else if (p.getJoinable() instanceof sim.joinable.OutputPin) {
                    label = ((sim.joinable.OutputPin)p.getJoinable()).getName();
                }
                switch(p.getEdge()){
                    case North:
                        g.drawString(label, p.x-3, p.y+ (4 * UIConstants.GRID_DOT_SPACING)+1);
                        g.drawLine(p.x, p.y, p.x, p.y+(2*UIConstants.GRID_DOT_SPACING));
                        break;
                    case South:
                        g.drawString(label, p.x-3, p.y-(2*UIConstants.GRID_DOT_SPACING)-2);
                        g.drawLine(p.x, p.y, p.x, p.y-(2*UIConstants.GRID_DOT_SPACING)); 
                        break;
                    case West:
                        g.drawString(label, p.x + (2 * UIConstants.GRID_DOT_SPACING)+1, p.y+3);
                        g.drawLine(p.x, p.y, p.x+(2*UIConstants.GRID_DOT_SPACING), p.y);
                        break;
                    case East:
                        int d = (label.length() + 1)* UIConstants.GRID_DOT_SPACING;
                        g.drawString(label, p.x -d-8, p.y+3);
                        g.drawLine(p.x, p.y, p.x-(2*UIConstants.GRID_DOT_SPACING), p.y);                        
                        break;
                }      
            }
            g.translate(-getOrigin().x, -getOrigin().y);
            g.rotate(-rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
        }
        
        public void openEditor(){
            Editor editor = getParent().getParentFrame().getEditor();
            FileLoader cfh = new FileLoader(editor);
            CircuitPanel subCircuit = editor.createBlankCircuit(true);    
            if(cfh.loadFile(filename)){
                subCircuit.setFilename(filename);
                subCircuit.setSubcircuitParent(parent);
                editor.refreshWindowsMenu();
                subCircuit.getParentFrame().setTitle(filename);
                subCircuit.getCommandHistory().clearHistory();
                subCircuit.setCurrentTool("Select");
            } else {
                // Close bad circuit
                ((JDesktopPane) subCircuit.getParentFrame().getParent()).remove(subCircuit.getParentFrame());
            }         
        }
        
        public void updateSource(CircuitPanel cp){
            SelectableComponent sc = createSubcircuitComponent(getParent().getParentFrame().getEditor(), cp.getFilename(), parent);
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
