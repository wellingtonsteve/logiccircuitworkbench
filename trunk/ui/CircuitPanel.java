package ui;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import ui.log.PinLogger;
import ui.log.ViewerWindow;

/**
 *
 * @author Matt
 */
public class CircuitPanel extends JPanel {

    private Point currentPoint = new Point(0,0), startPoint = new Point(0,0), endPoint;
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
    private Point previousPoint = new Point(0,0);
    private CommandHistory cmdHist;
    private Simulator simulator;
    private Circuit logicalCircuit;
    private LinkedList<PinLogger> OutputLoggers = new LinkedList<PinLogger>();
    private ViewerWindow loggerWindow;

    public CircuitPanel(){
        addMouseMotionListener(new CircuitPanelMouseMotionAdapter());
        addMouseListener(new CircuitPanelMouseListener());
        this.logicalCircuit = new Circuit();
        this.simulator = new Simulator(logicalCircuit);
        this.loggerWindow = new ViewerWindow(this);
        this.loggerWindow.setVisible(false);
    }

    public void addLogger(sim.pin.Pin pinByName, Simulator simulator) {
        OutputLoggers.add(new PinLogger(pinByName, simulator));
    }
    
    public Grid getGrid() {
        return grid;
    }

    public Collection<PinLogger> getPinLoggers() {
        return OutputLoggers;
    }

    public void setFilename(String filename) {
        this.filename = filename;
        this.getParentFrame().setTitle(filename);
        editor.setTitle("Logic Circuit Workbench - " + filename);
    }

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

    
    private void resetActiveComponents() {
        for (SelectableComponent sc : activeComponents) {
            sc.resetDefaultState();
        }
        activeComponents.clear();
        editor.getClipboard().setHasSelection(false);
    }
    public void selectAllComponents() {
        activeComponents.clear();
        for (SelectableComponent sc : drawnComponents) {
            sc.mouseClicked(null);
            activeComponents.add(sc);
        }
        editor.getClipboard().setHasSelection(false);
    }
    
    // Selection box attributes created by selecting the selection tool and dragging
    private void setSelectionBox() {
        selX = startPoint.x;
        selY = startPoint.y;
        selWidth = endPoint.x - startPoint.x;
        selHeight = endPoint.y - startPoint.y;

        if (selWidth < 0) {
            selX = selX + selWidth;
            selWidth = selWidth * -1;
        }
        if (selHeight < 0) {
            selY = selY + selHeight;
            selHeight = selHeight * -1;
        }
    }
    
    public void repaintDirtyAreas() {              
        int dirtyX = Math.min(endPoint.x, previousPoint.x);
        int dirtyY = Math.min(endPoint.y, previousPoint.y);
        int dirtyMaxX = Math.max(endPoint.x, previousPoint.x);
        int dirtyMaxY = Math.max(endPoint.y, previousPoint.y);
       
        // Include range of current selection (i.e. non-fixed components)
        for(SelectableComponent sc: drawnComponents){
            if(!sc.isFixed()){
                if(dirtyX - sc.getCentre().x < dirtyX) { dirtyX = dirtyX - sc.getCentre().x - 10; }
                if(dirtyY - sc.getCentre().y < dirtyY) { dirtyY = dirtyY - sc.getCentre().y - 10; }
                if(dirtyMaxX + sc.getCentre().x > dirtyMaxX) { dirtyMaxX = dirtyMaxX + sc.getCentre().x +10; }
                if(dirtyMaxY + sc.getCentre().y > dirtyMaxY) { dirtyMaxY = dirtyMaxY + sc.getCentre().y +10; }
            }            
        }

        int dirtyWidth = dirtyMaxX - dirtyX;
        int dirtyHeight = dirtyMaxY - dirtyY;
        
        repaint(dirtyX, dirtyY, dirtyWidth, dirtyHeight);
    }
     
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
          // If we draw direct, go ahead and call the parent update. This will
          // clear the drawing area and then call paint. If you don't want the
          // drawing area cleared, just change the super.update(g);
          // to paint(g);

          if (editor.drawDirect()) {
               drawCircuit(g);
          } else {
               
               // If we're doing buffered drawing, simulate the effects of the
               // default update method by clearing the offscreen drawing area.
               // If you don't want the drawing area cleared, remove the calls
               // to setColor and fillRect.

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
        
        if(multipleSelection){                        
            g2.setColor(UIConstants.SELECTION_BOX_COLOUR);
            g2.setStroke(UIConstants.SELECTION_BOX_STROKE);
            setSelectionBox();
            g2.drawRect(selX, selY, selWidth, selHeight);
        }      
    }
             
    public void setCurrentTool(String tool){      
        this.currentTool = tool;                    
    }
    
    public String getCurrentTool(){
        return currentTool;
    }
    
    public boolean hasActiveSelection(){
        for(SelectableComponent sc: drawnComponents){
            if(sc.getSelectionState().equals(SelectionState.ACTIVE)){
                return true;
            }
        }        
        return false;
    }
    
    public SelectableComponent getHighlightedComponent() {
        return highlightedComponent;
    }

    public void setHighlightedComponent(SelectableComponent higlightedComponent) {
        if(highlightedComponent!=null){
            this.highlightedComponent.revertHoverState();
        }
        this.highlightedComponent = higlightedComponent;
        this.highlightedComponent.setHoverState();
    }
    
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
    
    public LinkedList<SelectableComponent> getActiveComponents(){
        return activeComponents;
    }
    
    public String resetCircuit(){
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
        return "Circuit cleared.";
    }    
    
    public void saveAs(String filename){
        this.setFilename(filename);
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
    
    public String getFilename(){
        return (filename==null)?"Untitled"+getParentFrame().getUntitledIndex()+UIConstants.FILE_EXTENSION:filename;
    }
            
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
     
    public void addComponent(SelectableComponent sc) {
        drawnComponents.push(sc);
        sc.getInvalidArea();
        sc.getBoundingBox();
        setCurrentTool(sc.getComponentTreeName());
        repaint(sc.getBoundingBox());
    }
    
    public boolean containsComponent(SelectableComponent sc){
        return drawnComponents.contains(sc);
    }

    public void createImage(String filename) {
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        paint(bi.getGraphics());      
        try {
            ImageIO.write(bi, "jpg", new File(filename));
        } catch (IOException ex) {
            ErrorHandler.newError("Image Creation Error","Please refer to the system output below.",ex);
        }
        
    }
    
    public CommandHistory getCommandHistory(){
        return cmdHist;
    }
    
    public CircuitFrame getParentFrame() {
        return parentFrame;
    }

    public void removeComponent(SelectableComponent sc) {
        drawnComponents.remove(sc);
        grid.removeComponent(sc);
        logicalCircuit.removeSimItem(sc.getLogicalComponent());
    }

    public void setParentFrame(CircuitFrame parentFrame) {
        this.parentFrame = parentFrame;
        editor = ((CircuitFrame) this.getParentFrame()).getEditor();
        cmdHist = new CommandHistory(editor);
    }
    
    private class CircuitPanelMouseListener implements MouseListener {

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
                            CreateComponentCommand ccc = new CreateComponentCommand(cmdHist,
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
                        }
                    }
                } else {
                    // Activate selected component
                    temporaryComponent.mouseClicked(e);
                    resetActiveComponents();
                    activeComponents.add(temporaryComponent);
                    editor.getClipboard().setHasSelection(true);
                    
                    // Update the current selection options panel
                    editor.getOptionsPanel().setComponent(temporaryComponent);
                }
                repaint();
            } 
        }
            
        public void mouseEntered(MouseEvent e) {}

        public void mouseExited(MouseEvent e) {}

        public void mousePressed(MouseEvent e) {
            if (isActiveCircuit()) {
                startPoint = Grid.snapToGrid(e.getPoint());
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
                        drawnComponents.peek().moveTo(endPoint, false);
                        drawnComponents.peek().mouseMoved(e);

                        // Activate any connection points that overlap pins on the current non-fixed component
                        for(Pin local: drawnComponents.peek().getPins()){
                            Point p = local.getGlobalLocation();
                            if(grid.isConnectionPoint(p) 
                                    && grid.getConnectionPoint(p).noOfConnections()>1){
                                grid.setActivePoint(p,true);
                            }
                        }

                    // Hover highlights    
                    } else if (currentTool.equals("Select")){

                        temporaryComponent = null;
                        if(getHighlightedComponent()!=null){
                            getHighlightedComponent().revertHoverState();
                        }

                        // Determine which component the mouse lies in
                        for(SelectableComponent sc: drawnComponents){
                            if(sc.containsPoint(endPoint)){
                                temporaryComponent = sc;
                            }                            
                        }                           

                        // Pass the "Highlight Token"
                        if(temporaryComponent!=null){
                            setHighlightedComponent(temporaryComponent);
                            temporaryComponent.mouseMoved(e);
                        }
                    }             

                    repaint();

                } else if(currentTool.equals("Wire") 
                        && !drawnComponents.isEmpty()
                        && drawnComponents.peek() instanceof Wire){

                    Wire w = (Wire) drawnComponents.peek();
                    w.setEndPoint(endPoint);
                    if(grid.isConnectionPoint(endPoint)){
                        grid.setActivePoint(endPoint, true);
                    }

                    repaint();
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
                    }  else {

                        // Area we dragging from a fixed component?
                        boolean clickingEmptySpace = true;
                        temporaryComponent = null;
                        for(SelectableComponent sc: drawnComponents){
                            if(sc.isFixed() && sc.containsPoint(startPoint)){
                                clickingEmptySpace = false;
                                temporaryComponent = sc;
                                break;
                            } 
                        }  

                        if(clickingEmptySpace){
                            multipleSelection = true;
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

                    repaint();

                } else if (currentTool.equals("Wire") 
                     && !drawnComponents.isEmpty()){

                    Wire w = (Wire) drawnComponents.peek();
                    // Start drawing the new wire
                    if(w.getOrigin().equals(new Point(0,0)) && grid.isConnectionPoint(startPoint)){
                        w.setStartPoint(startPoint);                           
                    }
                    w.setEndPoint(endPoint);                        

                    // Highlight connection point?
                    if(grid.isConnectionPoint(endPoint)){
                        grid.setActivePoint(endPoint, true);
                    }
                    repaint();
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
                    stc = new SelectionTranslateCommand(cmdHist);
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
    
    /**
     * Returns true if and only if this circuit is currently active in the editor.
     */
    protected boolean isActiveCircuit() {
        return editor.getActiveCircuit().equals(CircuitPanel.this);
    }
    
    public Simulator getSimulator(){
        return simulator;
    }
    
    public Circuit getLogicalCircuit(){
        return logicalCircuit;
    }
    
    public ViewerWindow getLoggerWindow(){
        return loggerWindow;
    }
}
