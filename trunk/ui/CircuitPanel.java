package ui;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import sim.Simulator;
import sim.componentLibrary.Circuit;
import ui.file.FileCreator;
import ui.grid.Grid;
import ui.components.*;
import ui.components.SelectableComponent.Pin;
import ui.command.CommandHistory;
import ui.command.CreateComponentCommand;
import ui.command.SelectionTranslateCommand;
import ui.error.ErrorHandler;
import ui.components.standard.log.PinLogger;
import ui.components.standard.log.ViewerWindow;

/**
 * This panel represents the actual visible circuit on which the circuit is drawn.
 * 
 * @author Matt
 */
public class CircuitPanel extends JPanel {

    private Point currentPoint = new Point(0,0), dragStartPoint = new Point(0,0), endPoint;
    private LinkedList<SelectableComponent> drawnComponents = new LinkedList<SelectableComponent>();
    private LinkedList<SelectableComponent> activeComponents = new LinkedList<SelectableComponent>();  
    private String currentTool = "Select";
    private SelectableComponent temporaryComponent; // Used for reference to a selection from list of drawn components
    private SelectableComponent highlightedComponent; // The currently highlighted (SelectionState.HOVER) component      
    private boolean nowDragingComponent = false;
    private boolean multipleSelection = false;
    private int selX;
    private int selY;
    private int selWidth;
    private int selHeight;
    private String filename;
    private CircuitFrame parentFrame;
    private Editor editor;
    private final Grid grid = new Grid();
    private Point previousEndPoint = new Point(0,0);
    private CommandHistory cmdHist;
    private Simulator simulator;
    private Circuit logicalCircuit;
    private LinkedList<PinLogger> OutputLoggers = new LinkedList<PinLogger>();
    private ViewerWindow loggerWindow;

    public CircuitPanel(CircuitFrame parentFrame){
        addMouseMotionListener(new CircuitPanelMouseMotionAdapter());
        addMouseListener(new CircuitPanelMouseAdapter());
        this.logicalCircuit = new Circuit();
        this.simulator = new Simulator(logicalCircuit);
        this.loggerWindow = new ViewerWindow(this);
        this.loggerWindow.setVisible(false);
        this.parentFrame = parentFrame;
        this.editor = ((CircuitFrame) getParentFrame()).getEditor();
        this.cmdHist = new CommandHistory(editor);
    }

    /**
     * Remove any components that are still floating in the workarea and have not been fixed.
     */
    public void removeUnFixedComponents() {
        LinkedList<SelectableComponent> fixedComponents = new LinkedList<SelectableComponent>();
        for(SelectableComponent sc: drawnComponents){
            if(sc.isFixed() && sc.getParent().equals(this)){
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
            sc.mouseClicked(null);
            activeComponents.add(sc);
        }
        editor.getClipboard().setHasSelection(true);
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
     * Calculate the Bounds of the Selection box based on mouse co-ordinates at
     * certain mouse dragging events.
     */
    private void setSelectionBox() {
        selX = dragStartPoint.x;
        selY = dragStartPoint.y;
        selWidth = endPoint.x - dragStartPoint.x;
        selHeight = endPoint.y - dragStartPoint.y;

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
     * Speedy method to repaint only those areas that have been changed since the 
     * last movement on the workarea.
     */
    public void repaintDirtyAreas() {              
        int dirtyX = Math.min(endPoint.x, previousEndPoint.x);
        int dirtyY = Math.min(endPoint.y, previousEndPoint.y);
        int dirtyMaxX = Math.max(endPoint.x, previousEndPoint.x);
        int dirtyMaxY = Math.max(endPoint.y, previousEndPoint.y);
      
        // Include range of current selection (i.e. non-fixed components)
        for(SelectableComponent sc: drawnComponents){
            if(!sc.isFixed() 
                    || sc.equals(highlightedComponent) 
                    || activeComponents.contains(sc)){
                if(dirtyX - sc.getCentre().x - 10 < dirtyX) { 
                    dirtyX = dirtyX - sc.getCentre().x - 10;
                }
                if(dirtyY - sc.getCentre().y - 10 < dirtyY) { 
                    dirtyY = dirtyY - sc.getCentre().y - 10; 
                }
                if(dirtyMaxX + (sc.getWidth()-sc.getCentre().x) - 10 > dirtyMaxX) { 
                    dirtyMaxX = dirtyMaxX + (sc.getWidth()-sc.getCentre().x) +10; 
                }
                if(dirtyMaxY + (sc.getHeight()-sc.getCentre().y) - 10> dirtyMaxY) {
                    dirtyMaxY = dirtyMaxY + (sc.getHeight()-sc.getCentre().y) +10; 
                }
            }            
        }        

        int dirtyWidth = dirtyMaxX - dirtyX;
        int dirtyHeight = dirtyMaxY - dirtyY;

        repaint(dirtyX, dirtyY, dirtyWidth, dirtyHeight);
    }
     
    /**
     * Implement the buffering as required by the speed test that was performed 
     * when the application was started.
     * If we draw directly, we simply draw the circuit to the graphics context provided,
     * otherwise if we're doing buffered drawing, we first draw to a clean offscreen 
     * drawing area and then draw the completed image to intial graphics context.
     * 
     * @param g The original graphics context.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (editor.drawDirect()) {
           drawCircuit(g);
        } else {

           // Clear the offscreen drawing area and set the drawing
           // color back to foreground.
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
     * Draw the components, background & grid of this circuit to the graphics context.
     * @param g
     */
    public void drawCircuit(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        
        // Background Colour
        g2.setColor(UIConstants.CIRCUIT_BACKGROUND_COLOUR);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                
        // Grid Snap Dots
        if(UIConstants.DRAW_GRID_DOTS){
            g2.setColor(UIConstants.GRID_DOT_COLOUR);
            for(int i =  UIConstants.GRID_DOT_SPACING; i< this.getWidth(); i+=UIConstants.GRID_DOT_SPACING){
                for(int j = UIConstants.GRID_DOT_SPACING; j < this.getHeight(); j+=UIConstants.GRID_DOT_SPACING){
                    g2.fillRect(i, j, 1, 1);
                }
            }
        }
                
        // Draw components
        for(SelectableComponent sc: drawnComponents){
            g2.translate(-sc.getCentre().x, -sc.getCentre().y);
            sc.draw(g2); 
            g2.translate(sc.getCentre().x, sc.getCentre().y);

            if(UIConstants.SHOW_INVALID_AREA_BOXES){
                g2.draw(sc.getInvalidArea());
            }

            if(UIConstants.SHOW_BOUNDING_BOXES){
                g2.draw(sc.getBoundingBox());
            }             
        }
        
        // Draw Connection Points 
        grid.draw(g2);
        
        // Draw Selection Box
        if(multipleSelection){                        
            g2.setColor(UIConstants.SELECTION_BOX_COLOUR);
            g2.setStroke(UIConstants.SELECTION_BOX_STROKE);
            g2.drawRect(selX, selY, selWidth, selHeight);
        }      
    }
             
    /** @param tool The current tool that being used to modify the circuit. */
    public void setCurrentTool(String tool){      
        this.currentTool = tool;                    
    }
    
    /** @return The name of the tool that is currently being used to modify the circuit */
    public String getCurrentTool(){
        return currentTool;
    }
    
    /**
     * Clear everything associated with this circuit.
     */
    public void resetCircuit(){
        drawnComponents.clear();
        activeComponents.clear();
        highlightedComponent = null;
        temporaryComponent = null;
        nowDragingComponent = false;
        multipleSelection = false;
        grid.clear();
        logicalCircuit.clear();
        for(PinLogger pl: OutputLoggers){
            pl.clear();
        }
        OutputLoggers.clear();
        editor.getClipboard().setHasSelection(false);

        repaint();      
    }    
    
    /** Save this circuit in the appropriate location. */
    public void saveAs(String filename){
        setFilename(filename);
        FileCreator fc = new FileCreator(filename);
        for(SelectableComponent sc: drawnComponents){
            if(sc.isFixed()){
                fc.add(sc);
            }
        }        
        fc.write();
        CircuitFrame frame = getParentFrame();
        frame.setTitle(filename);
        cmdHist.setIsDirty(false);
    }
    
    /**@return The filename of this circuit */
    public String getFilename(){
        return (filename==null)?"Untitled"+getParentFrame().getUntitledIndex()+UIConstants.FILE_EXTENSION:filename;
    }
    
    /**
     * Change the filename of this circuit, also cascade changes to parent frame
     * and application window.
     * @param filename The new filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
        this.getParentFrame().setTitle(filename);
        editor.setTitle("Logic Circuit Workbench - " + filename);
    }
    
    /** Add a list of components to this circuit */
    public void addComponentList(Collection<SelectableComponent> list){
        drawnComponents.addAll(list);
        activeComponents.clear();
        for(SelectableComponent sc: list){
            if(!sc.isFixed()){
                activeComponents.add(sc);
                nowDragingComponent = true;
                currentPoint = endPoint;
            }
            repaint(sc.getBoundingBox());
        }
        editor.getClipboard().setHasSelection(!activeComponents.isEmpty());
    }
     
    /** Add a single component to this circuit */
    public void addComponent(SelectableComponent sc) {
        drawnComponents.push(sc);
        sc.getInvalidArea();
        sc.getBoundingBox();
        setCurrentTool(sc.getComponentTreeName());
        repaint(sc.getBoundingBox());
    }
    
    /** @return Has the specified component been drawn on this circuit */
    public boolean containsComponent(SelectableComponent sc){
        return drawnComponents.contains(sc);
    }
    
    /** Remove the specified component from this circuit */
    public void removeComponent(SelectableComponent sc) {
        drawnComponents.remove(sc);
        grid.removeComponent(sc);
        logicalCircuit.removeSimItem(sc.getLogicalComponent());
    }

    /** Instead of drawing this circuit to the panel, create an image and draw it there.
     * Then write the image to file.
     * @param filename The filename for the new image.
     */
    public void createImage(String filename) {
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        paint(bi.getGraphics());      
        try {
            ImageIO.write(bi, "jpg", new File(filename));
        } catch (IOException ex) {
            ErrorHandler.newError("Image Creation Error","Please refer to the system output below.",ex);
        }        
    }
    
    /** @return The command history associated with this circuit */
    public CommandHistory getCommandHistory(){
        return cmdHist;
    }
    
    /** Convience method to perform (undoable/redoable) commands on this circuit */
    public void doCommand(ui.command.Command cmd){
        cmdHist.doCommand(cmd);
    }
    
     /**
     * Returns true if and only if this circuit is currently active in the editor.
     */
    protected boolean isActiveCircuit() {
        return editor.getActiveCircuit().equals(CircuitPanel.this); 
    }
    
    /**
     * @return The connection point grid associated with this Circuit.
     * @see ui.grid.Grid
     */
    public Grid getGrid() {
        return grid;
    }
    
    /** @return The simulator that calculates the logical changes for this circuit.*/
    public Simulator getSimulator(){
        return simulator;
    }
    
    /** @return The corresponding Logical Circuit for this Visual Circuit.*/
    public Circuit getLogicalCircuit(){
        return logicalCircuit;
    }
    
    /** @return The window that shows the graphical log of Pin Loggers in this circuit.*/
    public ViewerWindow getLoggerWindow(){
        return loggerWindow;
    }
    
    /**
     * Start logging the specified pin, which is associated with the specified simulator.
     * @param pinByName
     * @param simulator
     */
    public void addLogger(sim.pin.Pin pinByName, Simulator simulator) {
        OutputLoggers.add(new PinLogger(pinByName, simulator));
    }
    
    /** @return A Collection of the Pin Loggers in this circuit */
    public Collection<PinLogger> getPinLoggers() {
        return OutputLoggers;
    }
    
    /** @return The parent CircuitFrame of this circuit (i.e. the internal window
     * that contains this panel. */
    public CircuitFrame getParentFrame() {
        return parentFrame;
    }

    /** This inner class handles all MouseListener events */
    private class CircuitPanelMouseAdapter extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (isActiveCircuit() && !currentTool.equals("Wire")) {

                // Area we clicking empty space?
                boolean clickingEmptySpace = true;
                temporaryComponent = null;
                for (SelectableComponent sc : drawnComponents) {
                    if (sc.isFixed() && sc.containsPoint(currentPoint)) {
                        clickingEmptySpace = false;
                        temporaryComponent = sc;
                    }
                }

                if (clickingEmptySpace) {                    
                    resetActiveComponents();

                    // Fix floating selection
                    if (!drawnComponents.isEmpty() && !drawnComponents.peek().isFixed()) {
                        editor.fixComponent(drawnComponents.peek());
                        
                        if (!currentTool.equals("Select")) {
                            // Add another new component
                            CreateComponentCommand ccc = new CreateComponentCommand(
                            new Object[]{
                                currentTool,
                                editor.getOptionsPanel().getComponentRotation(),
                                new Point(0, 0),
                                editor.getOptionsPanel().getCurrentLabel(),
                                (currentTool.equals("Standard.LED")) ? editor.getOptionsPanel().getLEDColour() : null, 
                                (currentTool.equals("Standard.Button Source")) ? editor.getOptionsPanel().getInputSourceState() : null, 
                                CircuitPanel.this
                            });
                            cmdHist.doCommand(ccc);
                            previousEndPoint = new Point(0,0);
                        }
                        
                        repaint(drawnComponents.peek().getBoundingBox());
                    }
                } else {
                    // Activate selected component
                    temporaryComponent.mouseClicked(e);
                    resetActiveComponents();
                    activeComponents.add(temporaryComponent);
                    editor.getClipboard().setHasSelection(true);
                    repaint(temporaryComponent.getBoundingBox());
                    
                    // Update the current selection options panel
                    editor.getOptionsPanel().setComponent(temporaryComponent);
                }
            } 
        }

        public void mousePressed(MouseEvent e) {
            if (isActiveCircuit()) {
                dragStartPoint = Grid.snapToGrid(e.getPoint());
                currentPoint = Grid.snapToGrid(e.getPoint());
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (isActiveCircuit()) {
                // Find the location in the circuit
                endPoint = Grid.snapToGrid(e.getPoint());

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
                        editor.getOptionsPanel().setComponent(activeComponents.get(0));
                    }

                    multipleSelection = false;
                } else if (currentTool.equals("Wire") && !drawnComponents.isEmpty()) {

                    Wire w = (Wire) drawnComponents.peek();
                    
                    // Start drawing the new wire
                    if(w.getOrigin().equals(new Point(0,0)) && grid.isConnectionPoint(endPoint)){
                        w.setStartPoint(endPoint);
                    // We have chosen the start point again, remove the wire
                    }else if (w.getOrigin().equals(endPoint)) {
                        drawnComponents.pop();
                        drawnComponents.push(new Wire(CircuitPanel.this));
                    } else if (!w.getOrigin().equals(new Point(0, 0))) {
                        // Should we continue to draw the wire?
                        //      Only if we have not released on a connection point
                        if (grid.getConnectionPoint(endPoint).getConnections().size() == 1) {
                            w.addWaypoint(endPoint);
                        } else {
                            w.setEndPoint(endPoint);
                            w.translate(0, 0, true);
                            drawnComponents.push(new Wire(CircuitPanel.this));
                        }
                    }
                    // Highlight connection point?
                    if (grid.isConnectionPoint(endPoint)) {
                        grid.setActivePoint(endPoint, true);
                    }
                }
                repaint();
            }
        }
    }
    
    public class CircuitPanelMouseMotionAdapter extends MouseMotionAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            if(isActiveCircuit()){
                // Find the location in the circuit
                endPoint = Grid.snapToGrid(e.getPoint());

                if(!nowDragingComponent && !currentTool.equals("Wire")){                

                    // Moving a new non-fixed component around
                    if(!drawnComponents.isEmpty() 
                            && !drawnComponents.peek().isFixed() 
                            && !nowDragingComponent){
                        SelectableComponent sc = drawnComponents.peek();
                        boolean canMove = grid.canTranslateComponent(sc,
                                endPoint.x-sc.getOrigin().x,
                                endPoint.y-sc.getOrigin().y);
                        
                        // Move the component
                        if(canMove){                     
                            sc.moveTo(endPoint, false);
                            sc.mouseMoved(e);                            
                        }

                        // Activate any connection points that overlap pins on the current non-fixed component
                        for(Pin local: sc.getPins()){
                            Point p = local.getGlobalLocation();
                            if(grid.isConnectionPoint(p) 
                                    && grid.getConnectionPoint(p).noOfConnections()>1){
                                grid.setActivePoint(p,true);
                            }
                        }
                        
                        repaintDirtyAreas();
                        if(canMove){ previousEndPoint = endPoint;}

                    // Hover highlights    
                    } else if (currentTool.equals("Select")){
       
                        temporaryComponent = null;
                        if(getHighlightedComponent()!=null){
                            getHighlightedComponent().revertHoverState();
                            repaint(getHighlightedComponent().getBoundingBox());
                        }

                        // Determine which component the mouse lies in
                        for(SelectableComponent sc: drawnComponents){
                            if(sc.containsPoint(endPoint)){
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
                    w.setEndPoint(endPoint);
                    if(grid.isConnectionPoint(endPoint)){
                        grid.setActivePoint(endPoint, true);
                    }

                    Rectangle dirtyArea = w.getBoundingBox();
                    dirtyArea.add(endPoint);
                    repaint(dirtyArea);
                    repaintDirtyAreas();
                }
            }
        }                   

        @Override
        public void mouseDragged(MouseEvent e) {
            if(isActiveCircuit()){
                // Find the location in the circuit
                endPoint = Grid.snapToGrid(e.getPoint());

                if(currentTool.equals("Select")){

                    if(nowDragingComponent){
                        dragActiveSelection(e,false,false);
                        repaintDirtyAreas();
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
                            repaintDirtyAreas();
                        }
                    }
                    
                } else if (currentTool.equals("Wire") 
                     && !drawnComponents.isEmpty()){

                    Wire w = (Wire) drawnComponents.peek();
                    // Start drawing the new wire
                    if(w.getOrigin().equals(new Point(0,0)) && grid.isConnectionPoint(dragStartPoint)){
                        w.setStartPoint(dragStartPoint);                           
                    }
                    w.setEndPoint(endPoint);                        

                    // Highlight connection point?
                    if(grid.isConnectionPoint(endPoint)){
                        grid.setActivePoint(endPoint, true);
                    }
                    
                    Rectangle dirtyArea = w.getBoundingBox();
                    dirtyArea.add(endPoint);
                    repaint(dirtyArea);
                    repaintDirtyAreas();
                }
                currentPoint = Grid.snapToGrid(e.getPoint());
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
        if(activeComponents.size() == 1 && activeComponents.get(0) instanceof Wire){
            if(finish){
                activeComponents.get(0).mouseDraggedDropped(e);
                nowDragingComponent = false;
            } else {
                activeComponents.get(0).mouseDragged(e);     
            }
        // Translate selection
        } else {
            Point anchor = (temporaryComponent==null)?null:temporaryComponent.getOrigin().getLocation();
            boolean canMoveAll = true;
            for (SelectableComponent sc : activeComponents) {
                if(anchor==null){
                    anchor=sc.getOrigin().getLocation();
                }
                int dx = endPoint.x - anchor.x;
                int dy = endPoint.y - anchor.y;
                canMoveAll &= grid.canTranslateComponent(sc, dx, dy);
            }
            if (canMoveAll) {
                SelectionTranslateCommand stc = null;
                if(finish && !start){
                    stc = new SelectionTranslateCommand();
                }
                for (SelectableComponent sc : activeComponents) {
                    int dx = endPoint.x - anchor.x;
                    int dy = endPoint.y - anchor.y;
                    if(start && !finish){
                        sc.translate(endPoint.x - currentPoint.x, endPoint.y - currentPoint.y, false);
                        sc.mouseDragged(e);
                    } else if(finish && !start){
                        sc.translate(0, 0, false);
                        stc.translate(sc, dx, dy);
                        sc.mouseDraggedDropped(e);
                        multipleSelection = false;
                        nowDragingComponent = false;
                    } else if(start && finish){
                        ErrorHandler.newError("Drag Error", 
                                "You cannot start and finish a drag at the same time");
                    } else {
                        sc.translate(dx, dy, false);
                        sc.mouseDragged(e);
                    }
                }
                if(finish && !start){
                    cmdHist.doCommand(stc);
                    resetActiveComponents();
                }
            }
        }
    }
}
