/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import ui.file.FileCreator;
import ui.grid.Pin;
import ui.grid.Grid;
import ui.tools.SelectableComponent;
import ui.tools.SelectionState;
import netlist.standard.Wire;

/**
 *
 * @author Matt
 */
public class CircuitPanel extends JPanel {
    
    private int frameOriginX, frameOriginY;
    private Point currentPoint = new Point(0,0), startPoint = new Point(0,0), endPoint;
    private Stack<SelectableComponent> drawnComponents = new Stack<SelectableComponent>();
    private String currentTool = "Select";
    private SelectableComponent temporaryComponent; // Used for reference to a selection from list of drawn components
    private SelectableComponent highlightedComponent; // The currently highlighted (SelectionState.HOVER) component 
    private List<SelectableComponent> activeComponents = new LinkedList<SelectableComponent>();       
    private boolean nowDraging = false;
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

    public CircuitPanel(){
        frameOriginX = this.getX();
        frameOriginY = this.getY();               
        
        addMouseMotionListener(new MouseMotionAdapter(){  
            
            
            @Override
            @SuppressWarnings("static-access")
            public void mouseMoved(MouseEvent e) {
                
                if(!nowDraging && !currentTool.equals("Standard.Wire")){                
                    
                    // Find the location in the circuit
                    endPoint = grid.snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                    
                    // Moving a new non-fixed component around
                    if(!drawnComponents.isEmpty() && !drawnComponents.peek().isFixed() && !nowDraging){
                        drawnComponents.peek().moveTo(endPoint, false);
                        drawnComponents.peek().mouseMoved(e);
                        
                        // Activate any connection points that overlap pins on the current non-fixed component
                        for(Pin p: drawnComponents.peek().getGlobalPins()){
                            if(grid.isConnectionPoint(p)){
                                grid.setActivePoint(p,true);
                            }
                        }
                        

                    // Hover highlights    
                    } else if (getCurrentTool().equals("Select")){

                        temporaryComponent = null;
                        if(getHighlightedComponent()!=null){getHighlightedComponent().unHover();}

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
                    
                } else if(currentTool.equals("Standard.Wire") 
                        && !drawnComponents.isEmpty()
                        && drawnComponents.peek() instanceof Wire){
                    endPoint = grid.snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                    
                    Wire w = (Wire) drawnComponents.peek();
                    w.setEndPoint(endPoint);
                    if(grid.isConnectionPoint(endPoint)){
                        grid.setActivePoint(endPoint, true);
                    }
                    
                    repaint();
                }
                
            }                   
            
            @Override
            public void mouseDragged(MouseEvent e) {
                
                // Find the location in the circuit
                endPoint = grid.snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                
                if(getCurrentTool().equals("Select")){
                        
                    if(nowDraging){
                        for(SelectableComponent sc: activeComponents){
                            if(!(sc instanceof Wire)){ sc.translate(endPoint.x-currentPoint.x, endPoint.y-currentPoint.y, false); }
                            sc.mouseDragged(e);
                        }
                    }  else {

                        // Area we dragging from a fixed component?
                        boolean clickingEmptySpace = true;
                        temporaryComponent = null;
                        for(SelectableComponent sc: drawnComponents){

                            if(sc.isFixed() && sc.containsPoint(startPoint)){
                                clickingEmptySpace = false;
                                temporaryComponent = sc;
                            } 
                        }  

                        if(clickingEmptySpace){

                            multipleSelection = true;

                        } else {

                            nowDraging = true;
                            if(!activeComponents.contains(temporaryComponent)){ 
                                resetActiveComponents();
                                activeComponents.add(temporaryComponent); 
                            } else {
                                // Move active dragged components to the top of the stack
                                drawnComponents.removeAll(activeComponents);
                                drawnComponents.addAll(activeComponents);
                            }
                            
                            
                            for(SelectableComponent sc: activeComponents){                
                                if(!(sc instanceof Wire)){ 
                                    sc.translate(endPoint.x-currentPoint.x, endPoint.y-currentPoint.y, false);
                                }
                                sc.mouseDragged(e);
                            }

                        }
                    }
                    
                    repaint();

                } else if(currentTool.equals("Standard.Wire") 
                        && !drawnComponents.isEmpty()){
                   
                    Wire w = (Wire) drawnComponents.peek();
                    w.setStartPoint(startPoint);                    
                    w.setEndPoint(endPoint);
                    if(grid.isConnectionPoint(endPoint)){
                        grid.setActivePoint(endPoint, true);
                    }
                    repaint();
                }

                currentPoint = grid.snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                        
            }
        });
        
        addMouseListener(new MouseListener(){

            public void mouseClicked(MouseEvent e) {
                
                 if (!currentTool.equals("Standard.Wire")){
                     
                    // Area we clicking empty space?
                    boolean clickingEmptySpace = true;
                    temporaryComponent = null;
                    for(SelectableComponent sc: drawnComponents){

                        if(sc.isFixed() && sc.containsPoint(currentPoint)){
                            clickingEmptySpace = false;
                            temporaryComponent = sc;
                        } 
                    }  

                    if(clickingEmptySpace){

                        // Reset all selections
                        for(SelectableComponent sc: drawnComponents){
                            sc.resetDefaultState();
                        }  
                        resetActiveComponents();

                        // Fix floating selection
                        if(!drawnComponents.isEmpty() && !drawnComponents.peek().isFixed()){
                            editor.fixSelection(drawnComponents.peek(), endPoint);
                            editor.makeToolSelection();
                        }                               

                    } else {
                        // Activate selected component
                        temporaryComponent.mouseClicked(e); 
                        resetActiveComponents();
                        activeComponents.add(temporaryComponent);
                        
                        // Update the current selection options panel
                        editor.getOptionsPanel().setComponent(temporaryComponent);

                    } 
                    repaint();
                }     
                 
                
            };
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}

            public void mousePressed(MouseEvent e) {                                                

                startPoint = grid.snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));  
                currentPoint = grid.snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));

            }

            public void mouseReleased(MouseEvent e) {
                
                // Find the location in the circuit
                endPoint = grid.snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));  

                // Drop draged components
                if(nowDraging){
                    for(SelectableComponent sc: activeComponents){    
                        sc.translate(endPoint.x-currentPoint.x, endPoint.y-currentPoint.y, true);
                        sc.mouseDraggedDropped(e);                         
                    }
                    multipleSelection = false;                
                    nowDraging = false;
                
                // Activate all components within the selection box
                }else if(multipleSelection){
                    setSelectionBox();
                    
                    Rectangle selBox = new Rectangle(selX, selY, selWidth, selHeight); 
                    
                    for(SelectableComponent sc:activeComponents){
                        sc.resetDefaultState();
                    }
                    activeComponents.clear();
                    for(SelectableComponent sc: drawnComponents){
                        if(sc.isFixed() && sc.containedIn(selBox)){                            
                            sc.mouseReleased(e);
                            activeComponents.add(sc);
                        }
                    }
                    
                    // Update the current selection options panel
                    if(activeComponents.size()==1){
                        editor.getOptionsPanel().setComponent(activeComponents.get(0));
                    }
                    
                    
                    multipleSelection = false;

                } else if (currentTool.equals("Standard.Wire") 
                        && !drawnComponents.isEmpty()){
                    // Has the current wire been fixed?
                    Wire w = (Wire) drawnComponents.peek();
                    
                    if(w.getOrigin().equals(endPoint)){
                        // The start point is the same as the end point, 
                        // We don't want this line
                        drawnComponents.pop();
                        drawnComponents.push(new Wire(CircuitPanel.this));
                        
                    } else if(!w.getOrigin().equals(new Point(0,0))){                    
                                                
                        // Should we continue to draw the wire?
                        //      Only if we have not released on a connection point                        
                        if(!grid.isConnectionPoint(endPoint)){
                            w.addWaypoint(endPoint);
                        } else {
                            w.setEndPoint(endPoint);
                            w.translate(0, 0, true);
                            drawnComponents.push(new Wire(CircuitPanel.this));
                        }
                           
                        
                    }
                    
                }               

                repaint();

            }
            
        });
        
    }
    
    public Grid getGrid() {
        return grid;
    }

    public void setFilename(String filename) {
        this.filename = filename;
        this.getParentFrame().setTitle(filename);
    }

    public void removeUnFixedComponents() {
        Stack<SelectableComponent> stack2 = new Stack<SelectableComponent>();
        for(SelectableComponent sc: drawnComponents){
            if(sc.isFixed()){
                stack2.push(sc);
            }
        }
        drawnComponents.clear();
        drawnComponents.addAll(stack2);
        repaint();
    }

    
    private void resetActiveComponents() {
        for (SelectableComponent sc : activeComponents) {
            sc.resetDefaultState();
        }
        activeComponents.clear();
    }
    public void selectAllComponents() {
        activeComponents.clear();
        for (SelectableComponent sc : drawnComponents) {
            sc.mouseClicked(null);
            activeComponents.add(sc);
        }
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
    
    private void repaintDirtyAreas() {
        
       
        int dirtyX = Math.min(endPoint.x, previousPoint.x);
        int dirtyY = Math.min(endPoint.y, previousPoint.y);
        int dirtyMaxX = Math.max(endPoint.x, previousPoint.x);
        int dirtyMaxY = Math.max(endPoint.y, previousPoint.y);
       
        // Include range of current selection (i.e. non-fixed components)
        for(SelectableComponent sc: drawnComponents){
            if(!sc.isFixed()){
                Rectangle bb = sc.getBoundingBox();
                if(dirtyX - sc.getCentre().x < dirtyX) { dirtyX = dirtyX - sc.getCentre().x - 10; }
                if(dirtyY - sc.getCentre().y < dirtyY) { dirtyY = dirtyY - sc.getCentre().y - 10; }
                if(dirtyMaxX + sc.getCentre().x > dirtyMaxX) { dirtyMaxX = dirtyMaxX + sc.getCentre().x +10; }
                if(dirtyMaxY + sc.getCentre().y > dirtyMaxY) { dirtyMaxY = dirtyMaxY + sc.getCentre().y +10; }
//                if(bb.getMinY() < dirtyY) { dirtyY = (int) bb.getMinY(); }
//                if(bb.getMaxX() > dirtyMaxX) { dirtyMaxX = (int) bb.getMaxX(); }
//                if(bb.getMaxY() > dirtyMaxY) { dirtyMaxY = (int) bb.getMaxY(); }
//                if(bb.getMinX() - dX < dirtyX) { dirtyX = (int) bb.getMinX() - dX; }
//                if(bb.getMinY() - dY < dirtyY) { dirtyY = (int) bb.getMinY() - dY; }
//                if(bb.getMaxX() + dX > dirtyMaxX) { dirtyMaxX = (int) bb.getMaxX() + dX; }
//                if(bb.getMaxY() + dY > dirtyMaxY) { dirtyMaxY = (int) bb.getMaxY() + dY; }
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
        
        // Draw previous components
        for(SelectableComponent sc: drawnComponents){
            
            if(sc.isFixed() || (endPoint != null && contains(endPoint))){ // Don't draw the temp component, when mouse is outside viewable area.
                g2.translate(-sc.getCentre().x, -sc.getCentre().y);
                sc.draw(g2); 
                g2.translate(sc.getCentre().x, sc.getCentre().y);    
            }
                   
        }
        
        if(multipleSelection){
                        
            g2.setColor(UIConstants.SELECTION_BOX_COLOUR);
            g2.setStroke(UIConstants.SELECTION_BOX_STROKE);
            setSelectionBox();
            g2.drawRect(selX, selY, selWidth, selHeight);
        }
        
        // Draw Connection Points 
        grid.draw(g2);
    }
             
    public void selectTool(String tool){
        
        // Remove any temporary components left over
        removeUnFixedComponents();
        
        // Create a new non-fixed component
        if(tool.equals("Standard.Wire")){
            drawnComponents.push(new Wire(CircuitPanel.this));
        } 
        
        this.currentTool = tool;            
        
    }
    
    public String getCurrentTool(){
        return this.currentTool;
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
            this.highlightedComponent.unHover();
        }
        this.highlightedComponent = higlightedComponent;
        this.highlightedComponent.hover();
    }
    
    public void deleteActiveComponents(){

        for(SelectableComponent sc: activeComponents){
            grid.removeComponent(sc);
        }
        
        drawnComponents.removeAll(activeComponents);        
        activeComponents.clear();

        repaint();

    }
    
    public List<SelectableComponent> getActiveComponents(){
        return activeComponents;
    }
    
    public String resetCircuit(){
        drawnComponents.clear();
        activeComponents.clear();
        temporaryComponent = null;
        nowDraging = false;
        multipleSelection = false;
        grid.clear();
        
        repaint();
        
        return "Circuit cleared.";
    }    
    
    public void mouseExited(MouseEvent e){
        endPoint = e.getPoint();
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
    }
    
    public String getFilename(){
        return (filename==null)?"Untitled":filename;
    }
            
    public void addComponentList(List<SelectableComponent> list){
        drawnComponents.addAll(list);
    }
    
    
     
    public void addComponent(SelectableComponent sc) {
        drawnComponents.push(sc);
    }

    public void createImage(String filename) {
        BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        this.paint(bi.getGraphics());      
        try {
            ImageIO.write(bi, "jpg", new File(filename));
        } catch (IOException ex) {
            Logger.getLogger(CircuitPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public sim.Simulator getSimulator(){
        return null;
    }
    
    public CircuitFrame getParentFrame() {
        return parentFrame;
    }

    public void removeComponent(SelectableComponent sc) {
        drawnComponents.remove(sc);
    }

    public void setParentFrame(CircuitFrame parentFrame) {
        this.parentFrame = parentFrame;
        editor = ((CircuitFrame) this.getParentFrame()).getEditor();
    }
}