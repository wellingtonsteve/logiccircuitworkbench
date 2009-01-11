package ui.tools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.sax.TransformerHandler;
import sim.Component;
import ui.CircuitPanel;
import ui.Labeled;
import ui.UIConstants;
import ui.grid.Pin;

/**
 *
 * @author Matt
 */
public abstract class SelectableComponent implements MouseMotionListener, MouseListener, Labeled, Cloneable {
    
    protected Component component;
    protected SelectionState selectionState = SelectionState.DEFAULT, preHoverState;
    protected BufferedImage defaultBi;
    protected BufferedImage selectedBi;
    protected BufferedImage activeBi;
    protected Rectangle boundingBox = null;
    protected boolean fixed = false, wasEverFixed = false; // fixed describes the current state, wasEverFixed indicates whether fixed has ever been true i.e. whether this is a brand new piece or not
    private Point point;
    protected Collection<Point> localPins = new HashSet<Point>();
    protected Collection<Pin> globalPins = new HashSet<Pin>();
    protected double rotation = 0; // Rotation in degrees, with 0 being with inputs on left, output on right of standard and-gate
    protected double cosTheta, sinTheta;
    protected Rectangle invalidArea = null;
    private String label = new String();
    protected CircuitPanel parent;    

    public SelectableComponent(CircuitPanel parent, Point point){
        this.parent = parent;
        if(point == null){
            this.point = new Point(0,0);
        } else {
            this.point = point;
        }
                
        setLocalPins();

    }

    public SelectableComponent copy(){
        try {
            return (SelectableComponent) this.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(SelectableComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
   
    public double getRotation() {
       return this.rotation;
    }

    public SelectionState getSelectionState() {
        return selectionState;
    }

    public void setSelectionState(SelectionState selectiontype) {
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

        if(parent.getGrid().canMoveComponent(this, dx, dy, !wasEverFixed)){
            this.point.translate(dx, dy);
            
            // Adding this component to the grid for the first time
            if(!wasEverFixed && fixed){ 
                this.fixed = fixed;
                this.wasEverFixed = true;
                parent.getGrid().markInvalidAreas(this);
                setGlobalPins();
            // About to refix the component
            } else if (!this.fixed && fixed){
                this.fixed = fixed;
                setGlobalPins();            
                parent.getGrid().translateComponent(dx,dy,this, !wasEverFixed);
                parent.getGrid().markInvalidAreas(this); 
            // Just moving around 
            } else {
                setGlobalPins();
                this.fixed = fixed;
                parent.getGrid().translateComponent(dx,dy,this, !wasEverFixed);
            }

            setInvalidAreas();
            setBoundingBox();
        }         
        
        if(!this.fixed && fixed && UIConstants.DO_SYSTEM_BEEP){
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
        return "TestName";
    }
    
    public Rectangle getInvalidAreas(){
        if(invalidArea == null){
            setInvalidAreas();
        }
        return invalidArea;
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
    
    protected void setInvalidAreas(){
        Point rotOrigin = rotate(getOrigin());
        this.invalidArea = new Rectangle(rotOrigin.x,rotOrigin.y,getWidth(),getHeight());   
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
       
    public  Collection<Pin> getGlobalPins(){
        return globalPins;
    }
    
    protected void setGlobalPins(){
        for(Pin p: globalPins){
            parent.getGrid().removePin(p);
        }         
        
        globalPins.clear();
        
        cosTheta = Math.cos(rotation);
        sinTheta = Math.sin(rotation);
        
        for(Point p: getLocalPins()){
            Point rotP = rotate(p); 
            Pin pin = new Pin(this, rotP.x +getOrigin().x-getCentre().x,rotP.y +getOrigin().y-getCentre().x);
            globalPins.add(pin);
            if(isFixed()){ 
                parent.getGrid().addPin(pin);
            }
        }
        
    }
    
    protected  Collection<Point> getLocalPins(){
        return localPins;
    }
    
    protected abstract void setLocalPins();
    
    public boolean containedIn(Rectangle selBox) {
        return selBox.contains(getInvalidAreas());
    }

    private Point rotate(Point p) {
        Point transP = new Point(p.x - getCentre().x, p.y - getCentre().y);
        Point rotP = new Point((int) ((transP.x * cosTheta) - (transP.y * sinTheta)), (int) ((transP.y * cosTheta) + (transP.x * sinTheta)));
        Point ansP = new Point(rotP.x + getCentre().x, rotP.y + getCentre().y);
        return parent.getGrid().snapPointToGrid(ansP);
    }

    public abstract void createXML(TransformerHandler hd);    
    
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    protected void setBoundingBox() {
        Point rotOrigin = rotate(getOrigin());
        this.boundingBox = new Rectangle(rotOrigin.x,rotOrigin.y,getWidth(),getHeight());
    }
    
    public void draw(Graphics2D g){
            
        if(hasLabel()){
            g.setColor(UIConstants.LABEL_TEXT_COLOUR);
            g.drawString(getLabel(), getOrigin().x+UIConstants.LABEL_COMPONENT_X_OFFSET, getOrigin().y+UIConstants.LABEL_COMPONENT_Y_OFFSET);
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    public void removeLabel(){
        this.label = "";
    }
    
    public boolean hasLabel(){
        return !label.isEmpty() || !label.equals("");
    }
    
}
