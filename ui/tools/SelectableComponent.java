/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.tools;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import ui.*;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import sim.Component;

/**
 *
 * @author Matt
 */
public abstract class SelectableComponent implements MouseMotionListener, MouseListener {
    
    protected Component component;
    //protected int x;
    //protected int y;
    protected SelectionType selectionType = SelectionType.DEFAULT;
    protected BufferedImage defaultBi;
    protected BufferedImage selectedBi;
    protected BufferedImage activeBi;
    protected Rectangle boundingBox = null;
    private boolean fixed = false;
    private Point point;

    public SelectableComponent(Component component,Point point){
        this.component = component;
        this.point = point;
    }
    
    public SelectionType getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(SelectionType selectiontype) {
        this.selectionType = selectiontype;
    }

    public Component getComponent() {
        return component;
    }

    public Point getOrigin(){
        return point;
    }
    
    public abstract int getWidth();
    
    public abstract int getHeight();
    
    //TODO: Implement translation
    public void translate(Point point, boolean fixed) {
        this.point = point;
        this.fixed = fixed;    
        setBoundingBox();
    }
    
    public boolean isFixed(){
        return fixed;
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
    
    protected void setBoundingBox(){
        this.boundingBox = new Rectangle((int)point.getX(),(int)point.getY(),getWidth(),getHeight());
    }
    
    public abstract boolean containsPoint(Point point);
       
    public abstract void mouseDragged(MouseEvent arg0);
    public abstract void mouseMoved(MouseEvent arg0);
    public abstract void mouseClicked(MouseEvent arg0);
    public abstract void mouseEntered(MouseEvent arg0);
    public abstract void mouseExited(MouseEvent arg0);
    public abstract void mousePressed(MouseEvent arg0);
    public abstract void mouseReleased(MouseEvent arg0);
    
    public abstract void draw(Graphics g, javax.swing.JComponent parent);
}
