/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import javax.swing.JPanel;
import ui.tools.AndGate2Input;
import ui.tools.AndGate3Input;
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
class CircuitPanel extends JPanel {
    
    private int frameOriginX, frameOriginY;
    private Point currentPoint, startPoint = new Point(0,0), endPoint;
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
    private HashMap<Point,LinkedList<ConnectionPoint>> zBuffer = new HashMap<Point, LinkedList<ConnectionPoint>>(); 

    public CircuitPanel(){
        frameOriginX = this.getX();
        frameOriginY = this.getY();               
                    
        addMouseMotionListener(new MouseMotionAdapter(){  
            
            @Override
            public void mouseMoved(MouseEvent e) {
                
                if(!nowDraging && !currentTool.equals(UITool.Wire)){                
                    
                    // Find the location in the circuit
                    endPoint = snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                    
                    // Moving a new non-fixed component around
                    if(!drawnComponents.isEmpty() && !drawnComponents.peek().isFixed() && !nowDraging){
                        drawnComponents.peek().moveTo(endPoint, false);
                        drawnComponents.peek().mouseMoved(e);
                        
                        for(ConnectionPoint cp: drawnComponents.peek().getConnectionPoints()){
                            if(zBuffer.get(cp.getLocation()) != null){
                                zBuffer.get(cp.getLocation()).get(0).setIsActive(true);
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
                } else if(currentTool.equals(UITool.Wire) && drawnComponents.peek() instanceof Wire){
                    endPoint = snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                    
                    Wire w = (Wire) drawnComponents.peek();
                    w.setEndPoint(endPoint);

                }
                repaint();
            }                   
            
            @Override
            public void mouseDragged(MouseEvent e) {
                
                // Find the location in the circuit
                endPoint = snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                
                if(getCurrentTool().equals(UITool.Select)){
                        
                    if(nowDraging){
                        for(SelectableComponent sc: activeComponents){
                            sc.translate(endPoint.x-currentPoint.x, endPoint.y-currentPoint.y, false);
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
                                sc.translate(endPoint.x-currentPoint.x, endPoint.y-currentPoint.y, false);
                                sc.mouseDragged(e);
                            }

                        }
                    }
            
                    repaint();
                } else if(currentTool.equals(UITool.Wire)){
                   
                    Wire w = (Wire) drawnComponents.peek();
                    w.setStartPoint(startPoint);                    
                    w.setEndPoint(endPoint);

                    repaint();
                }

                currentPoint = snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                        
            }
        });
        
        addMouseListener(new MouseListener(){

            public void mouseClicked(MouseEvent e) {
                
                 if (currentTool.equals(UITool.Wire)){
                   
                    drawnComponents.push(new Wire());
                    ((Wire) drawnComponents.peek()).setStartPoint(endPoint);
                    
                } else {
                     
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
                            
                            // Add connectionpoints to grid dots
                            for(ConnectionPoint cp: drawnComponents.peek().getConnectionPoints()){
                                if(zBuffer.get(cp.getLocation())==null){
                                    zBuffer.put(cp.getLocation(), new LinkedList<ConnectionPoint>());
                                }
                                zBuffer.get(cp.getLocation()).add(cp);
                            }
                            
                            selectTool(currentTool);
                        }                               

                    } else {
                        // Activate selected component
                        temporaryComponent.mouseClicked(e); 
                        resetActiveComponents();
                        activeComponents.add(temporaryComponent);

                    } 
                }     
                 
                repaint();
            }
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}

            public void mousePressed(MouseEvent e) {                                                

                startPoint = snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));  
                currentPoint = snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));

            }

            public void mouseReleased(MouseEvent e) {
                
                // Find the location in the circuit
                endPoint = snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));  

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

                } else if (currentTool.equals(UITool.Wire)){
                    if(!drawnComponents.peek().getOrigin().equals(new Point(0,0))){
                        
                        drawnComponents.peek().setFixed();
                        
                        // Add connection points to grid dots
                        for(ConnectionPoint cp: drawnComponents.peek().getConnectionPoints()){
                            if(zBuffer.get(cp.getLocation())==null){
                                zBuffer.put(cp.getLocation(), new LinkedList<ConnectionPoint>());
                            }
                            zBuffer.get(cp.getLocation()).add(cp);
                        }               
                        
                        drawnComponents.push(new Wire());
                        ((Wire) drawnComponents.peek()).setStartPoint(endPoint);
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
     
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        
        // Background Colour
        g2.setColor(UIConstants.CIRCUIT_BACKGROUND_COLOUR);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        // Grid Snap Dots
        g2.setColor(UIConstants.GRID_DOT_COLOUR);
        for(int i =  0; i< this.getWidth(); i+=UIConstants.GRID_DOT_SPACING){
            for(int j = 0; j < this.getHeight(); j+=UIConstants.GRID_DOT_SPACING){
                g2.fillRect(i, j, 1, 1);
            }
        }
                
        // Draw previous components
        for(SelectableComponent sc: drawnComponents){
            g2.translate(-sc.getCentre().x, -sc.getCentre().y);
            sc.draw(g2, this);
            g2.translate(sc.getCentre().x, sc.getCentre().y);           
        }
        
        if(nowDraging){
           
        } else if(multipleSelection){
                        
            g2.setColor(UIConstants.SELECTION_BOX_COLOUR);
            g2.setStroke(UIConstants.SELECTION_BOX_STROKE);
            setSelectionBox();
            g2.drawRect(selX, selY, selWidth, selHeight);
        }
        
        // Draw Connection Points 
        g2.setColor(UIConstants.CONNECTION_POINT_COLOUR);
        for(Point p: zBuffer.keySet()){
            if(p.equals(endPoint) && (currentTool.equals(UITool.Select) || currentTool.equals(UITool.Wire))){
                Stroke def = g2.getStroke();
                g2.setStroke(UIConstants.CONNECTED_POINT_STROKE);
                g2.drawRect(p.x-3, p.y-3, 7, 7); 
                g2.setStroke(def);
            }
            for(ConnectionPoint cp: zBuffer.get(p)){
                if(cp.isActive()){
                    Stroke def = g2.getStroke();
                    g2.setStroke(UIConstants.CONNECTED_POINT_STROKE);
                    g2.drawRect(p.x-3, p.y-3, 7, 7); 
                    g2.setStroke(def);
                    cp.setIsActive(false);
                }
                if(UIConstants.SHOW_CONNECTION_POINTS){
                    g2.drawOval(cp.getLocation().x-1, cp.getLocation().y-1, 3, 3);
                    g2.fillOval(cp.getLocation().x-1, cp.getLocation().y-1, 3, 3);
                }
            }
        }               
                                
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
    
    public String deleteActiveComponents(){

        int size = activeComponents.size();

        drawnComponents.removeAll(activeComponents);        
        activeComponents.clear();

        repaint();

        return size +" component(s) deleted.";

    }
    
    public String resetCircuit(){
        drawnComponents.clear();
        activeComponents.clear();
        temporaryComponent = null;
        nowDraging = false;
        multipleSelection = false;
        zBuffer.clear();
        
        repaint();
        
        return "Circuit cleared.";
    }
    
    private Point snapPointToGrid(Point old){
        if(UIConstants.SNAP_TO_GRID){
            //return new Point((int) (old.x / UIConstants.GRID_DOT_SPACING)*UIConstants.GRID_DOT_SPACING,
            //        (int) (old.y / UIConstants.GRID_DOT_SPACING)*UIConstants.GRID_DOT_SPACING);
            
            int d = UIConstants.GRID_DOT_SPACING;
            
            int xDivD = (int) old.x / d;
            int yDivD = (int) old.y / d;
            
            int newX = xDivD * d;
            int newY = yDivD * d;
            
            if((old.x + (d/2)) >= (xDivD + 1) * d){
                newX += d;
            }
            if((old.y + (d/2)) >=  (yDivD + 1) * d){
                newY += d;
            }
            
            return new Point(newX, newY);
            
        } else {
            return old;
        }                
    }

}
