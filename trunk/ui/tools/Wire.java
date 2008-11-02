/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.tools;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.JComponent;
import ui.UIConstants;
import ui.grid.Grid;

/**
 *
 * @author Matt
 */
public class Wire extends SelectableComponent {
    private Point endPoint = new Point(0,0);
    private Point startPoint = new Point(0,0);
    private LinkedList<Point> waypoints = new LinkedList<Point>();
    private int x1 = 0, y1 = 0, x2 = 0, y2 = 0, x3 = 0, y3 = 0;

    public Wire() {
        super(null, null);
    }

    @Override
    public int getWidth() {
        return Math.abs(startPoint.x - endPoint.x);
    }

    @Override
    public int getHeight() {
        return Math.abs(startPoint.y - endPoint.y);
    }

    @Override
    public boolean containsPoint(Point point) {
//        return ((point.x >= x1 && point.x <= x2) && 
//                (point.y >= y1 && point.y <= y2)) ||
//                ((point.x >= x2 && point.x <= x3) && 
//                (point.y >= y2 && point.y <= y3)) ||
//                ((point.x <= x1 && point.x >= x2) && 
//                (point.y <= y1 && point.y >= y2)) ||
//                ((point.x <= x2 && point.x >= x3) && 
//                (point.y <= y2 && point.y >= y3));
        return false;
    }
    
    @Override
    /**
     * Origin = Start point for a wire
     */
    public Point getOrigin() {
        return startPoint;
    }
    
    @Override
    public void translate(int dx, int dy, boolean fixed) {
        Grid.removeComponent(this);
        this.startPoint.translate(dx, dy);
        this.endPoint.translate(dx, dy);
        this.fixed = fixed;    
        setBoundingBox();
        setLocalPins();
        setGlobalPins();
        if(fixed){ Grid.addComponent(this); }
    }

    @Override
    public void draw(Graphics g, JComponent parent) {
        
        if(!startPoint.equals(new Point(0,0)) && !endPoint.equals(new Point(0,0))){
            switch(getSelectionState()){
                case ACTIVE:
                    g.setColor(UIConstants.ACTIVE_WIRE_COLOUR); 
                    break;
                case HOVER:
                    g.setColor(UIConstants.HOVER_WIRE_COLOUR); 
                    break;
                default:    
                    g.setColor(UIConstants.DEFAULT_WIRE_COLOUR);
                    
                    
            }
            
            Point current = startPoint, next = startPoint;
            for(Point waypoint: waypoints){
                next = waypoint;
                drawLeg(g, current, next);
                current = waypoint;
            }
            drawLeg(g, next, endPoint);
        }
                
    }

    public Point getEndPoint(){
        return endPoint;
    }
    
    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }
    
    public void addWaypoint(Point wp){
        waypoints.add(wp);
    }

    private void createLeg(Point from, Point to) {

        x1 = from.x;
        y1 = from.y;

        x2 = to.x;
        y2 = from.y;

        x3 = to.x;
        y3 = to.y;
    }

    private void drawLeg(Graphics g, Point from, Point to) {
                
        createLeg(from, to);
                        
        g.drawLine(x1, y1, x2, y2);
        g.drawLine(x2, y2, x3, y3);
        
    }
    
    private void setPinsOnLeg(Point from, Point to) {        
        
        int dx = from.x - startPoint.x;
        int dy = from.y - startPoint.y;
        
        createLeg(from, to);
                        
        if(x2-x1 > 0){
            for(int x = 0; x<=x2-x1; x+=UIConstants.GRID_DOT_SPACING){
                localPins.add(new Point(x + dx, dy));
            }
        } else {
            for(int x = 0; x<=x1-x2; x+=UIConstants.GRID_DOT_SPACING){
                localPins.add(new Point(-x + dx, dy));
            }
        }
        if(y3-y2 > 0){  
            for(int y = 0; y<=y3-y2; y+=UIConstants.GRID_DOT_SPACING){
                localPins.add(new Point(x2-x1 + dx, y + dy));
            }
        } else {
           for(int y = 0; y<=y2-y3; y+=UIConstants.GRID_DOT_SPACING){
                localPins.add(new Point(x2-x1 + dx, -y + dy));
            } 
        }
        
    }
    
    @Override
    public Point getCentre(){
        return new Point(0,0);
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        setSelectionState(SelectionState.ACTIVE);
    }

    public void mouseDraggedDropped(MouseEvent e) {
        setSelectionState(selectionState.ACTIVE);
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        if(!isFixed() && !getSelectionState().equals(SelectionState.ACTIVE)){
             setSelectionState(SelectionState.DEFAULT);
        } 
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(isFixed()){
            if(getSelectionState().equals(SelectionState.ACTIVE)){
                 setSelectionState(SelectionState.HOVER);
                 
            } else {
                 setSelectionState(SelectionState.ACTIVE);
            } 
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");   
    }

    @Override
    public void mouseReleased(MouseEvent e) {
         setSelectionState(SelectionState.ACTIVE);
    }

    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLocalPins() {
        localPins.clear();
        if(waypoints != null){
            Point current = startPoint, next = startPoint;
            for(Point waypoint: waypoints){
                next = waypoint;
                setPinsOnLeg(current, next);           
                current = waypoint;
            }
            setPinsOnLeg(next, endPoint);
        }
        
    }
        
    @Override
    public boolean containedIn(Rectangle selBox) {
//        return selBox.contains(x1, y1) &&
//                selBox.contains(x2, y2) &&
//                selBox.contains(x3, y3);
        return false;
    }
}
