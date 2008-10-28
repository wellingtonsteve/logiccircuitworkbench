/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.tools;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import javax.swing.JComponent;
import sim.Component;
import ui.ConnectionPoint;
import ui.UIConstants;

/**
 *
 * @author Matt
 */
public class Wire extends SelectableComponent {
    private Point endPoint = new Point(0,0);
    private Point startPoint = new Point(0,0);
    
    private Point maxNorth= new Point(-1,-1);
    private Point maxEast = new Point(-1,-1);
    private Point maxSouth = new Point(-1,-1);
    private Point maxWest = new Point(-1,-1);
    
    private int x1 = 0, y1 = 0, x2 = 0, y2 = 0, x3 = 0, y3 = 0;
    
    private GeneralPath path;

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
        return ((point.x >= x1 && point.x <= x2) && 
                (point.y >= y1 && point.y <= y2)) ||
                ((point.x >= x2 && point.x <= x3) && 
                (point.y >= y2 && point.y <= y3)) ||
                ((point.x <= x1 && point.x >= x2) && 
                (point.y <= y1 && point.y >= y2)) ||
                ((point.x <= x2 && point.x >= x3) && 
                (point.y <= y2 && point.y >= y3));
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
        this.startPoint.translate(dx, dy);
        this.endPoint.translate(dx, dy);
        if(fixed){setFixed();}
        setBoundingBox();
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
            
            drawLeg(g, startPoint, endPoint);
        }
        
//        drawLeg(g, startPoint, maxSouth);
//        drawLeg(g, maxSouth, maxEast);
//        drawLeg(g, maxEast, maxNorth);
//        drawLeg(g, maxNorth, maxWest);
//        drawLeg(g, maxWest, endPoint);              
        
//        g.drawLine(startPoint.x, startPoint.y, startPoint.x, maxSouth.y);
//        g.drawLine(startPoint.x, maxSouth.y, maxEast.x, maxSouth.y );
//        g.drawLine(maxEast.x, maxSouth.y, maxEast.x, maxNorth.y);
//        g.drawLine(maxEast.x, maxNorth.y, maxWest.x, maxNorth.y);
//        g.drawLine(maxWest.x, maxNorth.y, maxWest.x, endPoint.y);
//        g.drawLine(maxWest.x, endPoint.y, endPoint.x, endPoint.y);
        
//        g.setColor(UIConstants.GRID_DOT_COLOUR);
//        g.drawOval(startPoint.x-1, startPoint.y-1, 3, 3);
//        g.drawOval(maxSouth.x-1, maxSouth.y-1, 3, 3);
//        g.drawOval(maxEast.x-1, maxEast.y-1, 3, 3);
//        g.drawOval(maxNorth.x-1, maxNorth.y-1, 3, 3);
//        g.drawOval(maxWest.x-1, maxWest.y-1, 3, 3);
//        g.drawOval(endPoint.x-1, endPoint.y-1, 3, 3);
        
    }

    public Point getEndPoint(){
        return endPoint;
    }
    
    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
        if(maxNorth.y == -1){
            maxNorth = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
            maxWest = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }        
        
        if(endPoint.x > maxEast.x){
            maxEast = endPoint;
        }
        if(endPoint.x < maxWest.x){
            maxWest = endPoint;
        }
        if(endPoint.y < maxNorth.y){
            maxNorth = endPoint;
        }
        if(endPoint.y > maxSouth.y){
            maxSouth = endPoint;
        }
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
        if(maxNorth.y == -1){
            maxNorth = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
            maxWest = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }        
        
        if(startPoint.x > maxEast.x){
            maxEast = startPoint;
        }
        if(startPoint.x < maxWest.x){
            maxWest = startPoint;
        }
        if(startPoint.y < maxNorth.y){
            maxNorth = startPoint;
        }
        if(startPoint.y > maxSouth.y){
            maxSouth = startPoint;
        }

    }

    private void drawLeg(Graphics g, Point start, Point end) {
        
        x1 = start.x;
        y1 = start.y;
        
        x2 = end.x;
        y2 = start.y;
        
        x3 = end.x;
        y3 = end.y;
        
//        if(x3 < x1){
//            x2 = start.x;
//            y2 = end.y;
//        }        
                
        g.drawLine(x1, y1, x2, y2);
        g.drawLine(x2, y2, x3, y3);
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
    public void setConnectionPoints() {
        if(isFixed()){
            if(x2-x1 > 0){
                for(int x = 0; x<=x2-x1; x+=UIConstants.GRID_DOT_SPACING){
                    connectionPoints.add(new ConnectionPoint(this, x, 0));
                }
            } else {
                for(int x = 0; x<=x1-x2; x+=UIConstants.GRID_DOT_SPACING){
                    connectionPoints.add(new ConnectionPoint(this, -x, 0));
                }
            }
            if(y3-y2 > 0){  
                for(int y = 0; y<=y3-y2; y+=UIConstants.GRID_DOT_SPACING){
                    connectionPoints.add(new ConnectionPoint(this, x2-x1, y));
                }
            } else {
               for(int y = 0; y<=y2-y3; y+=UIConstants.GRID_DOT_SPACING){
                    connectionPoints.add(new ConnectionPoint(this, x2-x1, -y));
                } 
            }
        }
    }
    
    @Override
    public void setFixed(){
        super.setFixed();
        setConnectionPoints();
    }
    
    @Override
    public boolean containedIn(Rectangle selBox) {
        return selBox.contains(x1, y1) &&
                selBox.contains(x2, y2) &&
                selBox.contains(x3, y3);
    }
}
