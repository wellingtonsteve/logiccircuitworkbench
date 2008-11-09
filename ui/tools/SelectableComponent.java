package ui.tools;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashSet;
import sim.Component;
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
        // TODO: add check that translation is valid
                //System.out.println(this.fixed + " " + fixed);
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
    
    public  Collection<Pin> getGlobalPins(){
        return globalPins;
    }
    
    protected void setGlobalPins(){
        //if(isFixed()){
            for(Pin p: globalPins){
                Grid.removePin(p);
            }
        //}           
        
        globalPins.clear();
        
        for(Point p: getLocalPins()){
            Pin pin = new Pin(this, p.x + getOrigin().x - getCentre().x,p.y + getOrigin().y - getCentre().y);
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
}
