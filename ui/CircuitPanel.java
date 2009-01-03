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
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import javax.swing.JPanel;
import ui.file.FileCreator;
import ui.grid.Pin;
import ui.grid.Grid;
import ui.tools.AndGate2Input;
import ui.tools.AndGate3Input;
import ui.tools.Input;
import ui.tools.LED;
import ui.tools.NandGate2Input;
import ui.tools.NorGate2Input;
import ui.tools.OrGate2Input;
import ui.tools.SelectableComponent;
import ui.tools.SelectionState;
import ui.tools.UITool;
import ui.tools.Wire;

/**
 *
 * @author Matt
 */
public class CircuitPanel extends JPanel {
    
    private int frameOriginX, frameOriginY;
    private Point currentPoint = new Point(0,0), startPoint = new Point(0,0), endPoint;
    private Stack<SelectableComponent> drawnComponents = new Stack<SelectableComponent>();
    private UITool currentTool = UITool.Select;
    private SelectableComponent temporaryComponent; // Used for reference to a selection from list of drawn components
    private SelectableComponent highlightedComponent; // The currently highlighted (SelectionState.HOVER) component 
    private List<SelectableComponent> activeComponents = new LinkedList<SelectableComponent>();       
    private boolean nowDraging = false;
    private boolean multipleSelection = false;
    private int selX;
    private int selY;
    private int selWidth;
    private int selHeight;
    private Image offscreenImage;
    private Graphics offscreenGraphics;
    private boolean drawDirect = false;
    private boolean detected = false;

    public CircuitPanel(){
        frameOriginX = this.getX();
        frameOriginY = this.getY();               
    
        addMouseMotionListener(new MouseMotionAdapter(){  
            
            @Override
            @SuppressWarnings("static-access")
            public void mouseMoved(MouseEvent e) {
                
                if(!nowDraging && !currentTool.equals(UITool.Wire)){                
                    
                    // Find the location in the circuit
                    endPoint = Grid.snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                    
                    // Moving a new non-fixed component around
                    if(!drawnComponents.isEmpty() && !drawnComponents.peek().isFixed() && !nowDraging){
                        drawnComponents.peek().moveTo(endPoint, false);
                        drawnComponents.peek().mouseMoved(e);
                        
                        // Activate any connection points that overlap pins on the current non-fixed component
                        for(Pin p: drawnComponents.peek().getGlobalPins()){
                            if(Grid.isConnectionPoint(p)){
                                Grid.setActivePoint(p,true);
                            }
                        }
                        

                    // Hover highlights    
                    } else if (getCurrentTool().equals(UITool.Select)){

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
                    
                    // Repaint only dirty areas
                     //repaintDirtyAreas();
                    repaint();
                    
                } else if(currentTool.equals(UITool.Wire) 
                        && !drawnComponents.isEmpty()
                        && drawnComponents.peek() instanceof Wire){
                    endPoint = Grid.snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                    
                    Wire w = (Wire) drawnComponents.peek();
                    w.setEndPoint(endPoint);
                    if(Grid.isConnectionPoint(endPoint)){
                        Grid.setActivePoint(endPoint, true);
                    }
                    
                    repaint();
                }
                
            }                   
            
            @Override
            public void mouseDragged(MouseEvent e) {
                
                // Find the location in the circuit
                endPoint = Grid.snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                
                if(getCurrentTool().equals(UITool.Select)){
                        
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

                } else if(currentTool.equals(UITool.Wire) 
                        && !drawnComponents.isEmpty()){
                   
                    Wire w = (Wire) drawnComponents.peek();
                    w.setStartPoint(startPoint);                    
                    w.setEndPoint(endPoint);
                    if(Grid.isConnectionPoint(endPoint)){
                        Grid.setActivePoint(endPoint, true);
                    }
                    repaint();
                }

                currentPoint = Grid.snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                        
            }
        });
        
        addMouseListener(new MouseListener(){

            public void mouseClicked(MouseEvent e) {
                
                 if (!currentTool.equals(UITool.Wire)){
                     
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
                            drawnComponents.peek().moveTo(endPoint, true);                          
                            selectTool(currentTool);
                        }                               

                    } else {
                        // Activate selected component
                        temporaryComponent.mouseClicked(e); 
                        resetActiveComponents();
                        activeComponents.add(temporaryComponent);

                    } 
                    repaint();
                }     
                 
                
            };
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}

            public void mousePressed(MouseEvent e) {                                                

                startPoint = Grid.snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));  
                currentPoint = Grid.snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));

            }

            public void mouseReleased(MouseEvent e) {
                
                // Find the location in the circuit
                endPoint = Grid.snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));  

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
                    
                    multipleSelection = false;

                } else if (currentTool.equals(UITool.Wire) 
                        && !drawnComponents.isEmpty()){
                    // Has the current wire been fixed?
                    Wire w = (Wire) drawnComponents.peek();
                    
                    if(w.getOrigin().equals(endPoint)){
                        // The start point is the same as the end point, 
                        // We don't want this line
                        drawnComponents.pop();
                        drawnComponents.push(new Wire());
                        
                    } else if(!w.getOrigin().equals(new Point(0,0))){                    
                                                
                        // Should we continue to draw the wire?
                        //      Only if we have not released on a connection point                        
                        if(!Grid.isConnectionPoint(endPoint)){
                            w.addWaypoint(endPoint);
                        } else {
                            w.setEndPoint(endPoint);
                            w.translate(0, 0, true);
                            drawnComponents.push(new Wire());
                        }
                           
                        
                    }
                    
                }               

                repaint();

            }
            
        });
        
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

        
        int dirtyX = Math.min(startPoint.x, Math.min(endPoint.x, currentPoint.x));
        int dirtyY = Math.min(startPoint.y, Math.min(endPoint.y, currentPoint.y));
        int dirtyMaxX = Math.max(startPoint.x, Math.max(endPoint.x, currentPoint.x));
        int dirtyMaxY = Math.max(startPoint.y, Math.max(endPoint.y, currentPoint.y));
        
        // Include range of current selection (i.e. non-fixed components)
        for(SelectableComponent sc: drawnComponents){
            if(!sc.isFixed()){
                Rectangle bb = sc.getBoundingBox();
                if(bb.getMinX() < dirtyX) { dirtyX = (int) bb.getMinX(); }
                if(bb.getMinY() < dirtyY) { dirtyY = (int) bb.getMinY(); }
                if(bb.getMaxX() > dirtyMaxX) { dirtyMaxX = (int) bb.getMaxX(); }
                if(bb.getMaxY() > dirtyMaxY) { dirtyMaxY = (int) bb.getMaxY(); }
            }
        }
        
        int dirtyWidth = dirtyMaxX - dirtyX;
        int dirtyHeight = dirtyMaxY - dirtyY;
        
        repaint(dirtyX, dirtyY, dirtyWidth, dirtyHeight);
    }
     
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // If we haven't run auto-detection yet, do it now
          if (!detected) {
               doAutoDetect(g);
          }

          // If we draw direct, go ahead and call the parent update. This will
          // clear the drawing area and then call paint. If you don't want the
          // drawing area cleared, just change the super.update(g);
          // to paint(g);

          if (drawDirect) {
               myPaintComponents(g);
          } else {

               // If we're doing buffered drawing, simulate the effects of the
               // default update method by clearing the offscreen drawing area.
               // If you don't want the drawing area cleared, remove the calls
               // to setColor and fillRect.

               // Clear the offscreen drawing area and set the drawing
               // color back to foreground.

               offscreenGraphics.setColor(getBackground());
               offscreenGraphics.fillRect(0, 0, getWidth(),
                    getHeight());
               offscreenGraphics.setColor(getForeground());

               // Paint to the offscreen image

               myPaintComponents(offscreenGraphics);

               // Copy the offscreen image to the screen

               g.drawImage(offscreenImage, 0, 0, this);
          }
                                
    }
    
    public void myPaintComponents(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        
        // Background Colour
        g2.setColor(UIConstants.CIRCUIT_BACKGROUND_COLOUR);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                
        // Grid Snap Dots
        g2.setColor(UIConstants.GRID_DOT_COLOUR);
        for(int i =  UIConstants.GRID_DOT_SPACING; i< this.getWidth(); i+=UIConstants.GRID_DOT_SPACING){
            for(int j = UIConstants.GRID_DOT_SPACING; j < this.getHeight(); j+=UIConstants.GRID_DOT_SPACING){
                g2.fillRect(i, j, 1, 1);
            }
        }
                
        // Draw previous components
        for(SelectableComponent sc: drawnComponents){
            g2.translate(-sc.getCentre().x, -sc.getCentre().y);
            if(sc.isFixed() || (endPoint != null && contains(endPoint))){ // Don't draw the temp component, when mouse is outside viewable area.
                sc.draw(g2, this);
            }
            g2.translate(sc.getCentre().x, sc.getCentre().y);           
        }
        
        if(multipleSelection){
                        
            g2.setColor(UIConstants.SELECTION_BOX_COLOUR);
            g2.setStroke(UIConstants.SELECTION_BOX_STROKE);
            setSelectionBox();
            g2.drawRect(selX, selY, selWidth, selHeight);
        }
        
        // Draw Connection Points 
        Grid.draw(g2);
    }
            
    public void selectTool(UITool tool){
        
        // Remove any temporary components left over
        if(!drawnComponents.isEmpty() && !drawnComponents.peek().isFixed()){
            drawnComponents.pop();
        }
        repaint();
        
        // Create a new non-fixed component
        switch(tool){
            case Wire:
                drawnComponents.push(new Wire());
                break;
            case AndGate2Input:
                drawnComponents.push(new AndGate2Input(null, endPoint));
                break;
            case NandGate2Input:
                drawnComponents.push(new NandGate2Input(null, endPoint));
                break;
            case AndGate3Input:
                drawnComponents.push(new AndGate3Input(null, endPoint));
                break;
            case OrGate2Input:
                drawnComponents.push(new OrGate2Input(null, endPoint));
                break;    
            case NorGate2Input:
                drawnComponents.push(new NorGate2Input(null, endPoint));
                break;    
            case Input:
                drawnComponents.push(new Input(null, endPoint));
                break;  
            case LED:
                drawnComponents.push(new LED(null, endPoint));
                break;  
            default:
                break;
        }
        this.currentTool = tool;            
        
    }
    
    public UITool getCurrentTool(){
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

        int size = activeComponents.size();

        for(SelectableComponent sc: activeComponents){
            Grid.removeComponent(sc);
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
        Grid.clear();
        
        repaint();
        
        return "Circuit cleared.";
    }    
    
    public void mouseExited(MouseEvent e){
        endPoint = e.getPoint();
    }
    
    public void saveAs(String filename){
        FileCreator fc = new FileCreator(filename);
        for(SelectableComponent sc: drawnComponents){
            if(sc.isFixed()){
                fc.add(sc);
            }
        }
        fc.write();
    }
        
    public void addComponentList(List<SelectableComponent> list){
        drawnComponents.addAll(list);
    }
    
    // From: Expert Solutions by Mark Wutka, et. al. (http://www.webbasedprogramming.com/Java-Expert-Solutions/)
    // doAutoDetect performs tries drawing to the screen and to a
    // buffer. Whichever one takes the least time (actually, whichever
    // one it can do the most times within a set time constraint) is
    // the one that is best.

     protected void doAutoDetect(Graphics g)
     {

        // Create the off-screen drawing area
        offscreenImage = createImage(getWidth(), getHeight());
        offscreenGraphics = offscreenImage.getGraphics();

          long start;
          long end;

          // Tally the number of times we were able to draw direct and buffered
          int directCount = 0;
          int bufferedCount = 0;

            // Draw in the applet's background color, makes the autodetection invisible.

          g.setColor(getBackground());
          // Mark what time we started
          start = System.currentTimeMillis();
          end = start;

          // Paint patterns directly to the screen, but only for 500 milliseconds
          while ((end-start) < 500) {
               paintDetectDesign(g);
               end = System.currentTimeMillis();
               directCount++;
          }
          g.setColor(getForeground());

          // record the total time spent drawing directly
          long directTime = end - start;

          start = System.currentTimeMillis();
          end = start;

          // Paint patterns to the offscreen graphics, but only for 500 milliseconds
          while ((end-start) < 500) {
               paintDetectDesign(offscreenGraphics);
               end = System.currentTimeMillis();
               bufferedCount++;
          }

          long bufferedTime = end - start;

          // If we were able to draw more times using the buffered graphics,
          // or if the drawing counts are the same, but the total time for
          // the buffering was less, buffering is faster.

          if ((bufferedCount > directCount) ||
               ((bufferedCount == directCount) &&
                (bufferedTime < directTime))) {
               drawDirect = false;
          } else {

          // If we want to draw direct, free the space taken up by the
          // offscreen image and graphics context.
               offscreenImage.flush();
               offscreenImage = null;
               offscreenGraphics = null;
               drawDirect = true;
          }
          detected = true;
          System.out.println(bufferedCount + " " + directCount + " " + drawDirect);
    }

    // paintDetectDesign performs some graphical operations to gauge the time
    // it takes to paint either directly or to an offscreen area. It just draws
    // some lines, boxes and ovals a number of times and then returns.

     protected void paintDetectDesign(Graphics g)
     {
          for (int i=0; i < 10; i++) {
               g.drawLine(0, 0, 100, 100);
               g.fillRect(0, 0, 100, 100);
               g.fillOval(0, 0, 100, 100);
          }
     }
}
