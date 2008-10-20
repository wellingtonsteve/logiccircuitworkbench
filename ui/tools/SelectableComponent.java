/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.tools;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import sim.Component;
import ui.ConnectionPoint;

/**
 *
 * @author Matt
 */
public abstract class SelectableComponent implements MouseMotionListener, MouseListener {
    
    protected Component component;
    protected SelectionState selectionState = SelectionState.DEFAULT, preHoverState;
    protected BufferedImage defaultBi;
    protected BufferedImage selectedBi;
    protected BufferedImage activeBi;
    protected Rectangle boundingBox = null;
    private boolean fixed = false;
    private Point point;
    protected List<ConnectionPoint> connectionPoints = new LinkedList<ConnectionPoint>();

    public SelectableComponent(Component component,Point point){
        this.component = component;
        if(point == null){
            this.point = new Point(0,0);
        } else {
            this.point = point;
        }
        
        setConnectionPoints();
    }
    
    public SelectionState getSelectionState() {
        return selectionState;
    }

    protected void setSelectionState(SelectionState selectiontype) {
        this.selectionState = selectiontype;
    }
    
    public void resetDefaultState(){
        this.selectionState = selectionState.DEFAULT;
    }

    public Component getComponent() {
        return component;
    }

    public Point getOrigin(){
        return point;
    }
    
    public abstract int getWidth();
    
    public abstract int getHeight();
    
    public void translate(int dx, int dy, boolean fixed) {
        this.point.translate(dx, dy);
        this.fixed = fixed;    
        setBoundingBox();
    }
    
    public void moveTo(Point point, boolean fixed){
        this.point = point;
        this.fixed = fixed;
        setBoundingBox();
    }
    
    public boolean isFixed(){
        return fixed;
    }
    
    public void setFixed(){
        this.fixed = true;
    }
    
    public String getName(){
        return component.getType();
    }
    
    public Rectangle getBoundingBox(){
        if(boundingBox == null){
            setBoundingBox();
        }
        return boundingBox;
    }

    public void hover(){
        if(selectionState != null && !this.selectionState.equals(SelectionState.ACTIVE)){
            this.preHoverState = this.selectionState;
            setSelectionState(SelectionState.HOVER);
        }
        
    }
    
    public void unHover() {
        if(selectionState!= null && this.selectionState.equals(SelectionState.HOVER)){
            if(this.preHoverState == null){
                this.preHoverState = selectionState.DEFAULT;
            }
            setSelectionState(this.preHoverState);
        }
    }
    
    protected void setBoundingBox(){
        this.boundingBox = new Rectangle(getOrigin().x,getOrigin().y,getWidth(),getHeight());
    }
    
    public Point getCentre(){
        return new Point(getWidth()/2, getHeight()/2);
    }
    
    public abstract boolean containsPoint(Point point);
       
    public abstract void mouseDragged(MouseEvent e);
    public abstract void mouseMoved(MouseEvent e);
    public abstract void mouseClicked(MouseEvent e);
    public abstract void mouseEntered(MouseEvent e);
    public abstract void mouseExited(MouseEvent e);
    public abstract void mousePressed(MouseEvent e);
    public abstract void mouseReleased(MouseEvent e);
    public abstract void mouseDraggedDropped(MouseEvent e);
    
    public abstract void draw(Graphics g, javax.swing.JComponent parent);
    
    public  List<ConnectionPoint> getConnectionPoints(){
        return connectionPoints;
    }
    
    public abstract void setConnectionPoints();
    
    public boolean containedIn(Rectangle selBox) {
        return selBox.contains(getBoundingBox());
    }
}
