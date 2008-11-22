package ui.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashSet;
import javax.xml.transform.sax.TransformerHandler;
import sim.Component;
import ui.UIConstants;
import ui.grid.Grid;
import ui.grid.Pin;

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
    protected boolean fixed = false, wasFixed = false;
    private Point point;
    protected Collection<Point> localPins = new HashSet<Point>();
    protected Collection<Pin> globalPins = new HashSet<Pin>();
    protected double rotation = 0; // Rotation in degrees, with 0 being with inputs on left, output on right of standard and-gate
    protected double cosTheta, sinTheta;

    public SelectableComponent(Component component,Point point){
        this.component = component;
        if(point == null){
            this.point = new Point(0,0);
        } else {
            this.point = point;
        }
                
        setLocalPins();

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

        if(Grid.canMoveComponent(this, dx, dy)){
            this.point.translate(dx, dy);
            
            // Adding this component to the grid for the first time
            if(!wasFixed && fixed){ 
                this.fixed = fixed;
                this.wasFixed = true;
                Grid.markInvalidAreas(this);
                setGlobalPins();
            // About to refix the component
            } else if (!this.fixed && fixed){
                this.fixed = fixed;
                setGlobalPins();            
                Grid.translateComponent(dx,dy,this);
                Grid.markInvalidAreas(this);
            // Just moving around 
            } else {
                setGlobalPins();
                this.fixed = fixed;
                Grid.translateComponent(dx,dy,this);
            }


            setBoundingBox();
        } else if(!this.fixed && fixed && UIConstants.DO_SYSTEM_BEEP){
            UIConstants.beep();
        }
               
        
               
    }
    
    public void moveTo(Point point, boolean fixed){
        translate(point.x-this.point.x, point.y-this.point.y, fixed);
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
        Point rotOrigin = rotate(getOrigin());
        this.boundingBox = new Rectangle(rotOrigin.x,rotOrigin.y,getWidth(),getHeight());
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
    
    public abstract void draw(Graphics2D g, javax.swing.JComponent parent);
    
    public  Collection<Pin> getGlobalPins(){
        return globalPins;
    }
    
    protected void setGlobalPins(){
        for(Pin p: globalPins){
            Grid.removePin(p);
        }         
        
        globalPins.clear();
        
        cosTheta = Math.cos(rotation);
        sinTheta = Math.sin(rotation);
        
        for(Point p: getLocalPins()){
            Point rotP = rotate(p); 
            Pin pin = new Pin(this, rotP.x +getOrigin().x-getCentre().x,rotP.y +getOrigin().y-getCentre().x);
            globalPins.add(pin);
            if(isFixed()){ 
                Grid.addPin(pin);
            }
        }
        
    }
    
    protected  Collection<Point> getLocalPins(){
        return localPins;
    }
    
    protected abstract void setLocalPins();
    
    public boolean containedIn(Rectangle selBox) {
        return selBox.contains(getBoundingBox());
    }

    private Point rotate(Point p) {
        Point transP = new Point(p.x - getCentre().x, p.y - getCentre().y);
        Point rotP = Grid.snapPointToGrid(new Point((int) ((transP.x * cosTheta) - (transP.y * sinTheta)), (int) ((transP.y * cosTheta) + (transP.x * sinTheta))));
        return new Point(rotP.x + getCentre().x, rotP.y + getCentre().y);
    }

    public abstract void createXML(TransformerHandler hd);
    
}
