package ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import netlist.properties.ButtonAttribute;
import netlist.properties.Properties;
import netlist.properties.PropertiesOwner;
import netlist.properties.SpinnerAttribute;
import netlist.properties.TextAttribute;
import sim.Simulator;
import sim.SimulatorState;
import sim.componentLibrary.Circuit;
import ui.command.Command;
import ui.file.FileCreator;
import ui.grid.Grid;
import ui.components.*;
import ui.components.SelectableComponent.Pin;
import ui.command.CommandHistory;
import ui.command.CreateComponentCommand;
import ui.command.SelectionTranslateCommand;
import ui.command.SubcircuitOpenCommand.SubcircuitComponent;
import ui.components.standard.PinLogger;
import ui.error.ErrorHandler;
import ui.components.standard.log.ViewerFrame;
import ui.file.JPGFileFilter;

/**
 * This panel represents the visual workarea on which the circuit is drawn. And responds to
 * mouse actions by the user. The circuit panel also keeps a history of commands and 
 * retains lists of the components that are drawn here.
 * 
 * @author Matt
 */
public class CircuitPanel extends javax.swing.JPanel implements sim.SimulatorStateListener, PropertiesOwner {

    // Component Variables
    private Point lastDragPoint = Wire.DEFAULT_ORIGIN();
    private Point dragStartPoint = Wire.DEFAULT_ORIGIN();
    private Point currentPoint = Wire.DEFAULT_ORIGIN();
    private Point previousCurrentPoint = Wire.DEFAULT_ORIGIN();    
    private LinkedList<SelectableComponent> drawnComponents = new LinkedList<SelectableComponent>();
    private LinkedList<SelectableComponent> activeComponents = new LinkedList<SelectableComponent>();     
    private SelectableComponent temporaryComponent; // Used for reference to a selection from list of drawn components
    private SelectableComponent highlightedComponent; // The currently highlighted (SelectionState.HOVER) component    
    
    // State Variables
    private String currentTool = "Select";  
    private boolean nowDragingComponent = false;
    private boolean multipleSelection = false;
    
    // Selection Box variables
    private int selX;
    private int selY;
    private int selWidth;
    private int selHeight;
    
    // IO and Editor variables
    private String filename;
    private CircuitFrame parentFrame;
    private Editor editor;
    private final Grid grid;
    private CommandHistory cmdHist;
    private Simulator simulator;
    private Circuit logicalCircuit;
    private ViewerFrame loggerWindow;
    private SimulatorState simulatorState = SimulatorState.STOPPED;
    private Properties properties;
    private boolean isSubcircuit;
    private CircuitPanel subcircuitParent;
    private int simulationRate;

    public CircuitPanel(CircuitFrame parentFrame, boolean isSubCircuit){
        addMouseMotionListener(new CircuitPanelMouseMotionAdapter());
        addMouseListener(new CircuitPanelMouseAdapter());
        this.logicalCircuit = new Circuit();
        this.loggerWindow = new ViewerFrame(this);
        this.loggerWindow.setVisible(false);
        this.simulator = new Simulator(logicalCircuit);
        this.simulator.addStateListener(this);
        this.simulator.addStateListener(loggerWindow.getSimStateListener());
        this.parentFrame = parentFrame;
        this.editor = ((CircuitFrame) getParentFrame()).getEditor();
        this.cmdHist = new CommandHistory(editor);
        this.grid = new Grid(this);
        this.simulationRate = simulator.getSimulatorSpeed();
        this.isSubcircuit = isSubCircuit;
        createDefaultProperties();        
        if(isSubcircuit){
            setBackground(UIConstants.SUBCIRCUIT_BACKGROUND_COLOUR);
        } else {
            setBackground(UIConstants.CIRCUIT_BACKGROUND_COLOUR);
        }
        setPreferredSize(new Dimension(java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize()));
        setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
    }

    /**
     * Remove any components that are still floating in the workarea and have not been fixed.
     */
    public void removeUnfixedComponents() {
        loggerWindow.clearPinLoggers();
        LinkedList<SelectableComponent> fixedComponents = new LinkedList<SelectableComponent>();
        for(SelectableComponent sc: drawnComponents){
            if(sc.isFixed() && sc.getParent().equals(this)){
                if(sc instanceof PinLogger){
                    loggerWindow.addPinLogger((PinLogger)sc);
                }                
                fixedComponents.push(sc);
            } else {
                grid.removeComponent(sc);
                logicalCircuit.removeSimItem(sc.getLogicalComponent());
            }
        }
        drawnComponents.clear();
        drawnComponents.addAll(fixedComponents);
        repaint();
    }
    
    /**
     * Change the z-order of the component and bring it to the front.
     * @param sc The component to change.
     */
    public void bringComponentToFront(SelectableComponent sc) {
        if(drawnComponents.remove(sc)){
            drawnComponents.add(sc);
        }
    }

    /**
     * Unselect active selection of components. Also tell the editor so that it can 
     * disable any actions that require an active selection.
     */
    public void resetActiveComponents() {
        for (SelectableComponent sc : activeComponents) {
            sc.resetDefaultState();
        }
        activeComponents.clear();
        editor.getClipboard().setHasSelection(false);
    }
    
    /**
     * Make every component in this circuit active. Also tell the editor so that it can 
     * enable any actions that require an active selection.
     */
    public void selectAllComponents() {
        activeComponents.clear();
        for (SelectableComponent sc : drawnComponents) {
            sc.setSelectionState(SelectionState.ACTIVE);
            activeComponents.add(sc);
        }
        editor.getClipboard().setHasSelection(true);
        repaint();
    }
    
    /** @return Does this circuit has any components selected? */
    public boolean hasActiveSelection(){
        for(SelectableComponent sc: drawnComponents){
            if(sc.getSelectionState().equals(SelectionState.ACTIVE)){
                return true;
            }
        }        
        return false;
    }
    
    /** Delete the current selection from the circuit. */
        public void deleteActiveComponents(){        
        for(SelectableComponent sc: activeComponents){
            grid.removeComponent(sc);
            logicalCircuit.removeSimItem(sc.getLogicalComponent());
        }        
        drawnComponents.removeAll(activeComponents);        
        activeComponents.clear();
        editor.getClipboard().setHasSelection(false);
        repaint();
    }
    
    /** @return The current selection of active components */
    public LinkedList<SelectableComponent> getActiveComponents(){
        return activeComponents;
    }
    
    /** @return The current component which has the mouse hovering over it */
    private SelectableComponent getHighlightedComponent() {
        return highlightedComponent;
    }

    /** @param highlightedComponent The new component to be highlighted */ 
    private void setHighlightedComponent(SelectableComponent higlightedComponent) {
        if(highlightedComponent!=null){
            this.highlightedComponent.revertHoverState();
        }
        this.highlightedComponent = higlightedComponent;
        this.highlightedComponent.setHoverState();
    }
        
    /**
     * Clear everything associated with this circuit.
     */
    public void resetCircuit(){
        for(SelectableComponent sc:drawnComponents){
            if(sc instanceof PinLogger){
                ((PinLogger) sc).clear();
            }
        }
        drawnComponents.clear();
        activeComponents.clear();
        highlightedComponent = null;
        temporaryComponent = null;
        nowDragingComponent = false;
        multipleSelection = false;
        grid.clear();
        logicalCircuit.clear();
        editor.getClipboard().setHasSelection(false);
        repaint();      
    }    
    
    /** Add a list of components to this circuit */
    public void addComponentList(Collection<SelectableComponent> list){
        drawnComponents.addAll(list);
        previousCurrentPoint = SelectableComponent.DEFAULT_ORIGIN();
        activeComponents.clear();
        for(SelectableComponent sc: list){
            if(!sc.isFixed()){
                activeComponents.add(sc);
                nowDragingComponent = true;
                lastDragPoint = currentPoint;
            }
            repaint(sc.getBoundingBox());
        }
        //if(nowDragingComponent){dragActiveSelection(null, true, false);}            
        editor.getClipboard().setHasSelection(!activeComponents.isEmpty());
    }
     
    /** Add a single component to this circuit */
    public void addComponent(SelectableComponent sc) {
        drawnComponents.push(sc);
        previousCurrentPoint = SelectableComponent.DEFAULT_ORIGIN();
        setCurrentTool(sc.getKeyName());
        repaint(sc.getBoundingBox());
    }
    
    /** @return Has the specified component been drawn on this circuit */
    public boolean containsComponent(SelectableComponent sc){
        return drawnComponents.contains(sc);
    }
    
    /** Remove the specified component from this circuit */
    public void removeComponent(SelectableComponent sc) {
        loggerWindow.removePinLogger(sc);
        drawnComponents.remove(sc);
        grid.removeComponent(sc);
        logicalCircuit.removeSimItem(sc.getLogicalComponent());
    }
    
    /** 
     * Calculate the Bounds of the Selection box based on mouse co-ordinates at
     * certain mouse dragging events.
     */
    private void setSelectionBox() {
        selX = dragStartPoint.x;
        selY = dragStartPoint.y;
        selWidth = currentPoint.x - dragStartPoint.x;
        selHeight = currentPoint.y - dragStartPoint.y;

        if (selWidth < 0) {
            selX = selX + selWidth;
            selWidth = selWidth * -1;
        }
        if (selHeight < 0) {
            selY = selY + selHeight;
            selHeight = selHeight * -1;
        }
    }
    
    /**
     * @return The connection point grid associated with this Circuit.
     * @see ui.grid.Grid
     */
    public Grid getGrid() {
        return grid;
    }  
        
    /**
     * @return The command history of actions in this panel
     * @see ui.command.CommandHistory;
     */
    public CommandHistory getCommandHistory() {
        return cmdHist;
    }

    /** Convienience method to execute a command */
    public void doCommand(Command cmd) {
        cmdHist.doCommand(cmd);
    }

    /** The current tool causes distinctions in the mouse actions */
    public String getCurrentTool() {
        return currentTool;
    }
    
    /** @see #getCurrentTool() */
    public void setCurrentTool(String tool) {
        this.currentTool = tool;
    }

    /** The Frame containing any logger graphs **/
    public ViewerFrame getLoggerWindow() {
        return loggerWindow;
    }

    /** The logical represenation of the circuit */
    public Circuit getLogicalCircuit() {
        return logicalCircuit;
    }

    /** Does this panel contain a sub circuit? */
    public boolean isSubcircuit() {
        return isSubcircuit;
    }
    
    /** Set the parent which contains this circuit as a sub circuit */
    public void setSubcircuitParent(CircuitPanel parent) {
        this.subcircuitParent = parent;
    }
    
    /** Update any subcircuit that this panel has to reflect changes made to the source */
    private void updateSubcircuits(CircuitPanel subcircuitPanel) {
        for(SelectableComponent sc: drawnComponents){
            if(sc instanceof SubcircuitComponent 
                    && sc.getKeyName().equals(subcircuitPanel.getFilename())){
                ((SubcircuitComponent) sc).updateSource(subcircuitPanel);
            }
        }
        repaint();
    }    
        
    /** @return The logical simulator, responsible for changes in time, state and tiggering changes */
    public Simulator getSimulator() {
        return simulator;
    }

    /** @return The current state of the logical simulator. */
    public SimulatorState getSimulatorState() {
        return simulatorState;
    }
    
    /** {@inheritDoc } */
    @Override
    public void SimulationTimeChanged(long time) {     
        if(time % Math.pow(10, simulationRate -2 ) == 0  || simulationRate < 2){// Don't change too quickly!
            ErrorHandler.changeStatus("message", "Simulator Time: " + ((double) (time / (double) 1000000000)) + "×10\u2079ns");
        }
    }

    /** {@inheritDoc } */
    @Override
    public void SimulatorStateChanged(SimulatorState state) {
        this.simulatorState = state;
        if (getSimulatorState().equals(simulatorState.STOPPED)) {
            setBackground(UIConstants.CIRCUIT_BACKGROUND_COLOUR);
        } else {
            if(isSubcircuit){
                setBackground(UIConstants.SUBCIRCUIT_BACKGROUND_COLOUR);
            } else {
                setBackground(UIConstants.CIRCUIT_PLAYING_BACKGROUND_COLOUR);
            }
        }
    }
    
    /** {@inheritDoc } */
    @Override
    public void SimulationRateChanged(int rate){
       this.simulationRate = rate;
    }    

    /** Returns true if and only if this circuit is currently active in the editor. */
    protected boolean isActiveCircuit() {
        return editor.getActiveCircuit().equals(CircuitPanel.this);
    }

    /** @return the JFrame within which this panel is contained in the workarea*/
    public CircuitFrame getParentFrame() {
        return parentFrame;
    }
    
    /** @return Create a filename if we are untitled, otherwise return the filename */
    public String getFilename() {
        return (filename == null) ? "Untitled" + getParentFrame().getUntitledIndex() + UIConstants.FILE_EXTENSION : filename;
    }
    
    /** Convienience method to change the filename of this panel, it's parent frame 
     * and the title of the editor */
    public void setFilename(String filename) {
        this.filename = filename;
        this.getParentFrame().setTitle(filename);
        editor.setTitle("Logic Circuit Workbench - " + filename);
    }
    
    /** Save this circuit to disk, required to access hidden component list */
    public void saveAs(String filename) {
        setFilename(filename);
        FileCreator.write(this, drawnComponents);
        CircuitFrame frame = getParentFrame();
        frame.setTitle(filename);
        cmdHist.setIsDirty(false);
        
        if(isSubcircuit()){
            subcircuitParent.updateSubcircuits(this);
        }
    }
        
    /** {@inheritDoc} 
     * Also print to the correct area as determined by the double buffering test.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (editor.drawDirect()) {
            drawCircuit(g);
        } else {
            Graphics offscreenGraphics = editor.getOffscreenGraphics();
            Image offscreenImage = editor.getOffscreenImage();
            offscreenGraphics.setColor(getBackground());
            offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());
            offscreenGraphics.setColor(getForeground());

            // Paint to the offscreen image
            drawCircuit(offscreenGraphics);
            // Copy the offscreen image to the screen
            g.drawImage(offscreenImage, 0, 0, this);
        }
    }  
    
    /**
     * TODO: javadoc
     */
    public void repaintDirtyAreas() {
        Rectangle currentArea = new Rectangle(currentPoint);
        Rectangle previousArea = new Rectangle();
        int dx = previousCurrentPoint.x - currentPoint.x;
        int dy = previousCurrentPoint.y - currentPoint.y;

        currentArea.add(previousCurrentPoint);
        for (SelectableComponent sc : drawnComponents) {
            if (!sc.isFixed() || sc.equals(highlightedComponent) || activeComponents.contains(sc)) {
                currentArea.add(sc.getBoundingBox());
            }
        }

        previousArea = currentArea.getBounds();
        previousArea.translate(dx, dy);
        currentArea.add(previousArea);
        currentArea.grow(10, 10);

        repaint(currentArea);
    }
    
    /** Paint areas within the clip rectangle onto the specified graphics context */
    public void drawCircuit(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (g2.getClip() == null) {
            g2.setClip(0, 0, getWidth(), getHeight());
        }
        
        // Draw the grid dots
        if (UIConstants.DRAW_GRID_DOTS) {
            Point start = Grid.snapToGrid(new Point(g2.getClipBounds().x, g2.getClipBounds().y));
            g2.setColor(UIConstants.GRID_DOT_COLOUR);
            for (int i = start.x; i < g2.getClipBounds().getMaxX(); i += UIConstants.GRID_DOT_SPACING) {
                for (int j = start.y; j < g2.getClipBounds().getMaxY(); j += UIConstants.GRID_DOT_SPACING) {
                    g2.fillRect(i, j, 1, 1);
                }
            }
        }
        
        // Draw the components
        for (SelectableComponent sc : drawnComponents) {
            if (sc.getBoundingBox().intersects(g2.getClipBounds())) {
                //g2.translate(-sc.getCentre().x, -sc.getCentre().y);
                sc.draw(g2);
                if (UIConstants.SHOW_INVALID_AREA_BOXES) {
                    g2.draw(sc.getInvalidArea());
                }
                if (UIConstants.SHOW_BOUNDING_BOXES) {
                    g2.draw(sc.getBoundingBox());
                }
                //g2.translate(sc.getCentre().x, sc.getCentre().y);                
            }
        }

        // Draw Connection Points
        grid.draw(g2);

        // Draw Selection Box
        if (multipleSelection) {
            g2.setColor(UIConstants.SELECTION_BOX_COLOUR);
            g2.setStroke(UIConstants.SELECTION_BOX_STROKE);
            g2.drawRect(selX, selY, selWidth, selHeight);
        }
    }
    
    /** Draw the circuit to a buffered image and write to the filename specified. */
    public void createImage(String filename) {
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        paint(bi.getGraphics());
        try {
            ImageIO.write(bi, "png", new File(filename + ".png"));
        } catch (IOException ex) {
            ErrorHandler.newError("Image Creation Error", "Please refer to the system output below.", ex);
        }
    }

    /** Create a standard set of properties for this circuit if it was to be used as a subcircuit */
    private void createDefaultProperties() {
        this.properties = new Properties(getFilename()){
            {
                // Default logical description
                setLogicalComponentClass(logicalCircuit.getClass());      
                
                // Circuit Attributes
                addAttribute(new TextAttribute("Title", ""));
                addAttribute(new ButtonAttribute("Description") {
                    @Override
                    protected void buttonClickAction(ActionEvent e) {
                        JOptionPane.showInputDialog(jcomponent, getValue(), "Description", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                addAttribute(new ButtonAttribute("Subcircuit Image") {
                    @Override
                    protected void buttonClickAction(ActionEvent e) {
                        JFileChooser c = new JFileChooser();
                        FileFilter jpgFilter = new JPGFileFilter();        
                        c.setFileFilter(jpgFilter);
                        c.setDialogType(JFileChooser.SAVE_DIALOG);
                        c.setSelectedFile(new java.io.File(filename));
                        int rVal = c.showOpenDialog(jcomponent);
                        if (rVal == JFileChooser.APPROVE_OPTION) {
                            changeValue(c.getSelectedFile().getAbsolutePath());
                        }
                    }
                });
                addAttribute(new SpinnerAttribute("Subcircuit Width", 40, 10, Short.MAX_VALUE, UIConstants.GRID_DOT_SPACING));
                addAttribute(new SpinnerAttribute("Subcircuit Height", 40, 10, Short.MAX_VALUE, UIConstants.GRID_DOT_SPACING));
                
                // Subcircuit Image?
                if(!getAttribute("Subcircuit Image").getValue().equals("")){
                    File imagefile = new File((String)getAttribute("Subcircuit Image").getValue());
                    if(imagefile.exists()){
                        addImage(getFilename()+".default", (String)properties.getAttribute("Subcircuit Image").getValue());
                    } 
                }
            }          
        };
    }
    
    /** Refresh the subcircuit properties of this panel to reflect any changes to the circuit contents. */
    public void updateProperties(){
         Properties newproperties = new Properties(getFilename()){
             {
                // Default logical description
                setLogicalComponentClass(properties.getLogicalComponentClass());
                
                // Circuit Attributes
                addAttribute(properties.getAttribute("Title"));
                addAttribute(properties.getAttribute("Description"));
                addAttribute(properties.getAttribute("Subcircuit Image"));
                addAttribute(properties.getAttribute("Subcircuit Width"));
                addAttribute(properties.getAttribute("Subcircuit Height"));
                
                // Subcircuit Image?
                if(!getAttribute("Subcircuit Image").getValue().equals("")){
                    File imagefile = new File((String)getAttribute("Subcircuit Image").getValue());
                    if(imagefile.exists()){
                        addImage(getFilename()+".default", (String)properties.getAttribute("Subcircuit Image").getValue());
                    } else {
                        ErrorHandler.newError(new ui.error.Error("Initialisation Error",
                         "Could not find image with key : \n" + (String)getAttribute("Subcircuit Image").getValue())); 
                    }
                }
                
                // Create external pins
                int i = 0, o = 0;
                for(SelectableComponent sc: drawnComponents){                    
                    // Input Pins
                    if(sc.isFixed() 
                            && sc.getLogicalComponent() instanceof sim.componentLibrary.standard.Input){
                        int pos = (Integer) sc.getProperties().getAttribute("External Position").getValue();
                        String edge = (String) sc.getProperties().getAttribute("External Edge").getValue();
                        String label;
                        if(sc.hasLabel()){
                            label = sc.getLabel();
                        } else {
                            label = "Input " + i;
                            sc.setLabel(label);
                            i++;
                        }
                        addInputPin(label, ComponentEdge.convertStringToEdge(edge), pos);                        
                    // Output Pins    
                    } else if(sc.isFixed() 
                            && sc.getLogicalComponent() instanceof sim.componentLibrary.standard.Output){
                        int pos = (Integer) sc.getProperties().getAttribute("External Position").getValue();
                        String edge = (String) sc.getProperties().getAttribute("External Edge").getValue();
                        String label;
                        if(sc.hasLabel()){
                            label = sc.getLabel();
                        } else {
                            label = "Output " + o;
                            sc.setLabel(label);
                            o++;
                        }
                        addOutputPin(label, ComponentEdge.convertStringToEdge(edge), pos);       
                    }              
                }
             }
         };
         this.properties = newproperties;
    }
        
    @Override
    public Properties getProperties(){
        return properties;
    }
    
    @Override
    public void setProperties(Properties properties){
        this.properties = properties;
    }
    
    @Override
    public String getKeyName(){
        return "Circuit Panel";
    }

     /** This inner class handles all MouseListener events */
    private class CircuitPanelMouseAdapter extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (isActiveCircuit() && !currentTool.equals("Wire")) {
                // Area we clicking empty space?
                boolean clickingEmptySpace = true;
                temporaryComponent = null;
                for (SelectableComponent sc : drawnComponents) {
                    if (sc.isFixed() && sc.containsPoint(lastDragPoint)) {
                        clickingEmptySpace = false;
                        temporaryComponent = sc;
                        break;
                    }
                }

                if (clickingEmptySpace) {                    
                    resetActiveComponents();

                    // Fix floating selection
                    if (!drawnComponents.isEmpty() && !drawnComponents.peek().isFixed()) {
                        SelectableComponent top = drawnComponents.peek();
                        editor.fixComponent(top);                        
                        if (!currentTool.equals("Select")) {
                            // Add another new component                            
                            CreateComponentCommand ccc = new CreateComponentCommand(
                                    CircuitPanel.this,
                                    currentTool,
                                    editor.getComponentRotation(),
                                    SelectableComponent.DEFAULT_ORIGIN());
                            cmdHist.doCommand(ccc);
                            ((VisualComponent)ccc.getComponent()).addLogicalComponentToCircuit();

                            // To redraw the new component at workarea origin
                            previousCurrentPoint = SelectableComponent.DEFAULT_ORIGIN();
                        }
                    }
                } else {
                    // Single Click
                    if(e.getClickCount() != 2){
                        // Activate selected component
                        temporaryComponent.mouseClicked(e);
                        if(simulatorState.equals(SimulatorState.STOPPED)){
                            resetActiveComponents();
                            activeComponents.add(temporaryComponent);
                            editor.getClipboard().setHasSelection(true);                            
                        }                        
                        repaint(temporaryComponent.getBoundingBox());

                        // Update the current selection options panel
                        editor.setComponent(temporaryComponent);
                    // Double Click
                    } else if(e.getClickCount() == 2 
                            && temporaryComponent instanceof SubcircuitComponent){
                        ((SubcircuitComponent) temporaryComponent).openEditor();
                    }
                }
            } 
        }

        public void mousePressed(MouseEvent e) {
            if (isActiveCircuit()) {
                dragStartPoint = Grid.snapToGrid(e.getPoint());
                lastDragPoint = Grid.snapToGrid(e.getPoint());
            }
        }

        public void mouseReleased(MouseEvent e) {            
            if (isActiveCircuit()) {
                currentPoint = Grid.snapToGrid(e.getPoint());

                // Drop draged components
                if (nowDragingComponent) {
                    dragActiveSelection(e,false,true);
                // Activate all components within the selection box
                } else if (multipleSelection) {
                    setSelectionBox();
                    Rectangle selBox = new Rectangle(selX, selY, selWidth, selHeight);

                    for (SelectableComponent sc : activeComponents) {
                        sc.resetDefaultState();
                    }
                    activeComponents.clear();
                    for (SelectableComponent sc : drawnComponents) {
                        if (sc.isFixed() && sc.containedIn(selBox)) {
                            sc.mouseReleased(e);
                            activeComponents.add(sc);
                        }
                    }
                    editor.getClipboard().setHasSelection(!activeComponents.isEmpty());
                    
                    // Update the current selection options panel
                    if (activeComponents.size() == 1) {
                        editor.setComponent(activeComponents.get(0));
                    }
                    multipleSelection = false;
                    
                } else if (currentTool.equals("Wire") && !drawnComponents.isEmpty()) {
                    Wire w = (Wire) drawnComponents.peek();
                    
                    // Start drawing the new wire
                    if(w.getOrigin().equals(Wire.DEFAULT_ORIGIN()) && grid.isConnectionPoint(currentPoint)){
                        w.setStartPoint(currentPoint);
                    // We have chosen the start point again, remove the wire
                    } else if (w.getOrigin().equals(currentPoint)) {
                        drawnComponents.pop();
                        drawnComponents.push(new Wire(CircuitPanel.this));
                    } else if (!w.getOrigin().equals(Wire.DEFAULT_ORIGIN())) {
                        // Should we continue to draw the wire?
                        //      Only if we have not released on a connection point
                        if (!grid.isConnectionPoint(currentPoint)  || 
                                (grid.isConnectionPoint(currentPoint) && grid.getConnectionPoint(currentPoint).hasParent(w))) {
                            w.addWaypoint(currentPoint);
                        } else if (grid.getConnectionPoint(currentPoint).canConnect(w.getLogicalWire())) {
                            w.setEndPoint(currentPoint);
                            // Remove zero-length wires created by wire optimisation
                            if(w.getOrigin().equals(w.getEndPoint())){
                                drawnComponents.pop();
                            } else {
                                w.translate(0, 0, true);
                            }
                            drawnComponents.push(new Wire(CircuitPanel.this));
                        }
                    }
                    // Highlight connection point?
                    if (grid.isConnectionPoint(currentPoint) 
                            && grid.getConnectionPoint(currentPoint).noOfConnections() > 1) {
                        grid.setActivePoint(currentPoint, true);
                    }
                } 
                repaint();
            }
        }
    }
    
    /** This inner class handles all Mouse Motion events */
    public class CircuitPanelMouseMotionAdapter extends MouseMotionAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            if(isActiveCircuit()){
                currentPoint = Grid.snapToGrid(e.getPoint());
                
                if(!nowDragingComponent && !currentTool.equals("Wire")){                

                    // Moving a new non-fixed component around
                    if(!drawnComponents.isEmpty() 
                            && !drawnComponents.peek().isFixed() 
                            && !nowDragingComponent){
                        SelectableComponent sc = drawnComponents.peek();
                        int dx = currentPoint.x-sc.getOrigin().x-sc.getCentre().x;
                        int dy = currentPoint.y-sc.getOrigin().y-sc.getCentre().y;
                        boolean canMove = grid.canTranslateComponent(sc,dx,dy);
                        
                        // Move the component
                        if(canMove){                     
                            sc.translate(dx, dy, false);
                            sc.mouseMoved(e);                            
                        }

                        // Activate any connection points that overlap pins on the current non-fixed component
                        for(Pin local: sc.getPins()){
                            Point p = local.getGlobalLocation();
                            if(grid.isConnectionPoint(p)){
                                grid.setActivePoint(p,true);
                            }
                        }                        
                        repaintDirtyAreas();
                        if(canMove){ previousCurrentPoint = currentPoint;}

                    // Hover highlights    
                    } else if (currentTool.equals("Select")){       
                        // Reset the last component to be hovered over
                        temporaryComponent = null;
                        if(getHighlightedComponent()!=null){
                            getHighlightedComponent().revertHoverState();
                            repaint(getHighlightedComponent().getBoundingBox());
                        }
                        // Determine which component the mouse lies in
                        for(SelectableComponent sc: drawnComponents){
                            if(sc.containsPoint(currentPoint)){
                                temporaryComponent = sc;
                                break;
                            }                            
                        }  
                        // Pass the "Highlight Token"
                        if(temporaryComponent!=null){
                            setHighlightedComponent(temporaryComponent);
                            temporaryComponent.mouseMoved(e);
                            repaint(temporaryComponent.getBoundingBox());
                        }                        
                    }                                 
                } else if(currentTool.equals("Wire") 
                        && !drawnComponents.isEmpty()
                        && drawnComponents.peek() instanceof Wire){

                    Wire w = (Wire) drawnComponents.peek();
                    Rectangle dirtyArea = w.getBoundingBox();
                    w.setEndPoint(currentPoint);
                    if(grid.isConnectionPoint(currentPoint)){
                        grid.setActivePoint(currentPoint, true);
                    }

                    dirtyArea.add(w.getBoundingBox());
                    dirtyArea.add(currentPoint);
                    repaint(dirtyArea);
                }
            } 
        }                   

        @Override
        public void mouseDragged(MouseEvent e) {
            if(isActiveCircuit()){
                currentPoint = Grid.snapToGrid(e.getPoint());
                if(currentTool.equals("Select")){
                    // Continue Drag
                    if(nowDragingComponent){
                        dragActiveSelection(e,false,false);
                    }  else {
                        // Area we dragging from a fixed component?
                        boolean clickingEmptySpace = true;
                        temporaryComponent = null;
                        if(!multipleSelection){
                            for(SelectableComponent sc: drawnComponents){
                                if(sc.isFixed() && sc.containsPoint(dragStartPoint)){
                                    clickingEmptySpace = false;
                                    temporaryComponent = sc;
                                    break;
                                } 
                            }  
                        }                            
                        // Create a selection box or Start dragging selection
                        if(clickingEmptySpace){
                            multipleSelection = true;                            
                            setSelectionBox();
                            repaint();
                        } else {
                            nowDragingComponent = true;
                            if(!activeComponents.contains(temporaryComponent)){ 
                                resetActiveComponents();
                                activeComponents.add(temporaryComponent); 
                                editor.getClipboard().setHasSelection(true);
                            } else {
                                // Move active dragged components to the top of the stack
                                drawnComponents.removeAll(activeComponents);
                                drawnComponents.addAll(activeComponents);
                            }                            
                            // Start drag
                            dragActiveSelection(e, true, false);
                       }
                    }                    
                } else if (currentTool.equals("Wire") && !drawnComponents.isEmpty()){
                    Wire w = (Wire) drawnComponents.peek();
                    Rectangle dirtyArea = w.getBoundingBox();
                    
                    // Start drawing the new wire
                    if(w.getOrigin().equals(SelectableComponent.DEFAULT_ORIGIN())
                            && grid.isConnectionPoint(dragStartPoint)){
                        w.setStartPoint(dragStartPoint);                           
                    }
                    w.setEndPoint(currentPoint); 
                    
                    // Highlight connection point?
                    if(grid.isConnectionPoint(currentPoint)
                            && grid.getConnectionPoint(currentPoint).noOfConnections() > 1){
                        grid.setActivePoint(currentPoint, true);
                    }                    
                    
                    dirtyArea.add(w.getBoundingBox());
                    dirtyArea.add(currentPoint);
                    repaint(dirtyArea);
                    lastDragPoint = currentPoint;
                }
            }       
        }
    }    
    
    /**
     * Translate the active selection according to start and end points of a 
     * drag by the mouse.
     * 
     * @param e The event associated with the drag
     * @param start Are we just starting the drag?
     * @param finish Should we finish the drag
     */
    private void dragActiveSelection(MouseEvent e, boolean start, boolean finish) {
        // We don't want to translate a wire unless we are moving it with some other pieces.
        if(activeComponents.size() == 1 && activeComponents.get(0) instanceof Wire && e != null){            
            if(finish){
                activeComponents.get(0).mouseDraggedDropped(e);
                nowDragingComponent = false;
            } else {
                activeComponents.get(0).mouseDragged(e);     
            }
            repaintDirtyAreas();
            lastDragPoint = currentPoint;
            previousCurrentPoint = lastDragPoint;
        // Translate selection
        } else {
            Point anchor = null;
            if(temporaryComponent!=null){
                anchor = temporaryComponent.getOrigin().getLocation();
                anchor.translate(temporaryComponent.getCentre().x, temporaryComponent.getCentre().y);
            }
            // Check whether all the moves can be made
            boolean canMoveAll = true;
            for (SelectableComponent sc : activeComponents) {
                if(anchor==null){ 
                    anchor = sc.getOrigin().getLocation(); 
                    anchor.translate(sc.getCentre().x, sc.getCentre().y);
                }
                int dx = currentPoint.x - anchor.x;
                int dy = currentPoint.y - anchor.y;
                canMoveAll &= grid.canTranslateComponent(sc, dx, dy);
            }
            if (canMoveAll) {
                SelectionTranslateCommand stc = null;
                if(finish && !start){ stc = new SelectionTranslateCommand(); }
                // Perform appropriate actions to the start, middle of end of a translation
                for (SelectableComponent sc : activeComponents) {
                    int dx = currentPoint.x - anchor.x;
                    int dy = currentPoint.y - anchor.y;
                    // Start
                    if(start && !finish){
                        sc.translate(currentPoint.x - lastDragPoint.x, currentPoint.y - lastDragPoint.y, false);
                        sc.mouseDragged(e);
                        nowDragingComponent = true;
                    // End    
                    } else if(finish && !start){
                        sc.translate(0, 0, true);
                        stc.translate(sc, dx, dy);
                        nowDragingComponent = false;
                    // Invalid
                    } else if(start && finish){
                        ErrorHandler.newError("Drag Error", 
                                "You cannot start and finish a drag at the same time");
                    // Middle
                    } else {
                        sc.translate(dx, dy, false);
                        sc.mouseDragged(e);
                        nowDragingComponent = true;
                    }
                }
                // Save the translation to the command history
                if(finish && !start){
                    cmdHist.doCommand(stc);
                    resetActiveComponents();
                }
                repaintDirtyAreas();
                lastDragPoint = currentPoint;
                previousCurrentPoint = currentPoint;
            // Catch finish translations if the move was invalid
            } else if(finish && !start) {
                SelectionTranslateCommand stc = new SelectionTranslateCommand();
                for (SelectableComponent sc : activeComponents) {
                    stc.translate(sc, 0, 0);
                    nowDragingComponent = false;
                }
                cmdHist.doCommand(stc);
                resetActiveComponents();
            }
        }
    }
}
