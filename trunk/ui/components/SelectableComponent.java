package ui.components;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.LinkedList;
import javax.xml.transform.sax.TransformerHandler;
import sim.Component;
import ui.CircuitPanel;
import ui.UIConstants;
import ui.grid.Pin;

/**
 * SelectableComponents represent the visual components that can be place and edited on 
 * a circuit workarea. 
 * 
 * @author Matt
 */
public abstract class SelectableComponent implements MouseMotionListener, MouseListener, Labeled, Cloneable {
    

    /** Set the default selection state of this component */
    protected SelectionState selectionState = SelectionState.DEFAULT;
    
    /** To store the Selection state of the logicalComponent before it enters the hover state.*/
    protected SelectionState preHoverState;
    
    /** @see getLogicalComponent() */
    protected Component logicalComponent;
    
    /** @see getBoundingBox() */
    protected Rectangle boundingBox = null;
    
    /** @see getInvalidArea() */
    protected Rectangle invalidArea = null;
    
    /** Describes the current fixed state */
    protected boolean fixed = false;    
    
    /** Indicates whether fixed has ever been true i.e. whether this is a brand new piece or not */
    protected boolean wasEverFixed = false; // 
    
    /** @see getOrigin() */
    private Point origin;
    
    /** @see getLocalPins() */
    protected Collection<Point> localPins = new LinkedList<Point>();
    
    /** @see getGlobalPins() */
    protected Collection<Pin> globalPins = new LinkedList<Pin>();
    
    /** Rotation in radians, with 0 being with inputs on left, output on right of standard and-gate */
    protected double rotation = 0; 
    
    /** Precompute the values of Math.cos(rotation) and Math.cos(rotation) */
    protected double cosTheta, sinTheta;

    /** @see getLabel() */
    private String label = "";
    
    /** @see getParent() */
    protected CircuitPanel parent;
    
    /** @see getComponentTreeName() */
    protected String componentTreeName;

    /**
     * Default constructor for a SelectableComponent. 
     * 
     * @param parent The parent circuit panel to which this component wll be drawn.
     * @param origin The initial origin of this component.
     */
    public SelectableComponent(CircuitPanel parent, Point origin){
        this.parent = parent;
        if(origin == null){
            this.origin = new Point(0,0);
        } else {
            this.origin = origin;
        }
                
        setLocalPins();
    }
    
    /**
     * The parent of this logicalComponent is the CircuitPanel to which the logicalComponent is drawn.
     * 
     * @return The Component's parent CircuitPanel
     */
    public CircuitPanel getParent() {
        return parent;
    }
    
    /**
     * The state of this logicalComponent.
     * 
     * @return SelectionState.DEFAULT - The logicalComponent is neither selected nor active
     *          and is drawn in its default view
     *         SelectionState.HOVER - The logicalComponent is only in this state when the 
     *          mouse as been positioned inside the logicalComponent's bounding box and 
     *          the logicalComponent is not already active
     *         SelectionState.ACTIVE - The logicalComponent has been selected (clicked) and
     *          marked as active so that it's properties can be changed in the options 
     *          panel or so that it can be moved.
     */
    public SelectionState getSelectionState() {
        return selectionState;
    }

    /**
     * Set the selection state of this logicalComponent.
     * 
     * @param selectiontype SelectionState.DEFAULT - The logicalComponent is neither selected 
     *          nor active and is drawn in its default view
     *         SelectionState.HOVER - The logicalComponent is only in this state when the 
     *          mouse as been positioned inside the logicalComponent's bounding box and 
     *          the logicalComponent is not already active
     *         SelectionState.ACTIVE - The logicalComponent has been selected (clicked) and
     *          marked as active so that it's properties can be changed in the options 
     *          panel or so that it can be moved.
     */
    public void setSelectionState(SelectionState selectiontype) {
        this.selectionState = selectiontype;
    }
    
    /**
     * Convenience method to return the state of this logicalComponent to it's default setting
     */
    public void resetDefaultState(){
        setSelectionState(SelectionState.DEFAULT);
    }

    /**
     * Get the underlying logical component that is associated with a particular
     * instance of this object.
     * 
     * @return The corresponding logical Component.
     */
    public Component getLogicalComponent() {
        return logicalComponent;
    }

    /**
     * Get the local origin of this component in world co-ordinates.
     * 
     * @return The component's origin.
     */
    public Point getOrigin(){
        return origin;
    }
    
    /**
     * Centre points of components are use as the anchor for the mouse pointer.
     * In local co-ordinates
     * 
     * @return The centre point of this component.
     */
    public Point getCentre(){
        return new Point(getWidth()/2, getHeight()/2);
    }
    
    /**
     * @return The width of the component.
     */
    public abstract int getWidth();

    /**
     * @return The height of the component.
     */
    public abstract int getHeight();
    
    /**
     * Translate this component on the Circuit Panel. The move is first checked 
     * to be valid and after the translation the component is fixed/unfixed to
     * the workarea as specified
     * 
     * @param dx The displacement in the x-direction.
     * @param dy The displacement in the y-direction.
     * @param fixed Should the component be fixed after the translation?
     */
    public void translate(int dx, int dy, boolean fixed) {       

        if(parent.getGrid().canMoveComponent(this, dx, dy, !wasEverFixed)){
            this.origin.translate(dx, dy);
            
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
        
        // Alert the user that this was an invalid move and we cannot fix the 
        // component here
        if(!this.fixed && fixed && UIConstants.DO_SYSTEM_BEEP){
            UIConstants.beep();
        }                    
               
    }
    
    /**
     * Convienience method to move the origin of the component to a specified point
     * 
     * @param point The new origin of the component
     * @param fixed Should the component be fixed after the translation?
     */
    public void moveTo(Point newOrigin, boolean fixed){
        translate(newOrigin.x-origin.x, newOrigin.y-origin.y, fixed);
    }
    
    /**
     * Is the the component currently fixed to the parent circuit. Components are
     * by default unfixed whilst they are being positioned/created.
     * 
     * @return Is this component fixed?
     */
    public boolean isFixed(){
        return fixed;
    }
    
    /**
     * @return Short description of the Component
     */
    public abstract String getName();
    
    /**
     * Rotations of this component are specified in radians and are rotated clockwise about
     * the centre point of the component. The zero radian position is the standard
     * rotation with inputs on the left and outputs on the right (i.e. for an AND
     * gate). 
     * 
     * @return The rotation of the component.
     */
    public double getRotation() {
       return rotation;
    }
     
    /**
     * Rotations of this component are specified in radians and are rotated clockwise about
     * the centre point of the component. The zero radian position is the standard
     * rotation with inputs on the left and outputs on the right (i.e. for an AND
     * gate). 
     * 
     * @param rotation The new value of the rotation of this component
     */
    public void setRotation(double rotation) {
        this.rotation = rotation % (Math.PI * 2);
        setLocalPins();
        setGlobalPins();
        setInvalidAreas();
        setBoundingBox();
    }
        
    /**
     * Each component specifies an invalid area within which no other component, 
     * pin or wire may be placed.
     * 
     * @return The Invalid Area rectangle for this component (World Co-ordinates).
     */
    public Rectangle getInvalidAreas(){
        if(invalidArea == null){
            setInvalidAreas();
        }
        return invalidArea;
    }
    
    /**
     * Each component specifies an invalid area within which no other component, 
     * pin or wire may be placed.
     * 
     * The Invalid areas are set upon component initialisation and updated dynamically
     * upon translation or rotation.
     */    
    protected void setInvalidAreas(){
        this.invalidArea = new Rectangle(getOrigin().x,getOrigin().y,getWidth(),getHeight());   
    }
    
    /**
     * The bounding box of a component specifies the area within which mouse movements
     * may select or activate this component.
     * 
     * @return The Bounding Box rectangle for this component (World Co-ordinates).
     */
    public Rectangle getBoundingBox(){
        if(boundingBox == null){
            setBoundingBox();
        }
        return boundingBox;
    }
    
    /**
     * The bounding box of a component specifies the area within which mouse movements
     * may select or activate this component.
     * 
     * The Bounding box is set upon component initialisation and updated dynamically
     * upon translation or rotation.
     */
    protected void setBoundingBox() {
        Point rotOrigin = rotate(getOrigin());
        this.boundingBox = new Rectangle(rotOrigin.x,rotOrigin.y,getWidth(),getHeight());
    } 
    
    /**
     * {@inheritDoc}
     */
    public String getLabel() {
        return label;
    }

    /**
     * {@inheritDoc}
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeLabel(){
        this.label = "";
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasLabel(){
        return label != null && !label.isEmpty();
    }
 
    /**
     * The Component Tree Name is the value that is returned from the Component Tree
     * in the toolbox when this type of component is selected. The name is also the
     * key for all netlist mappings.
     * 
     * @return The component tree name of this component
     */
    public String getComponentTreeName() {
        return componentTreeName;
    }

    /**
     * The Component Tree Name is the value that is returned from the Component Tree
     * in the toolbox when this type of component is selected. The name is also the
     * key for all netlist mappings.
     * 
     * The component tree name must be set in all subclasses.
     */
    protected abstract void setComponentTreeName();

    /**
     * Convience method to record the current state of the object and then to change
     * the state to SelectionState.HOVER
     */
    public void setHoverState(){
        if(selectionState != null && !this.selectionState.equals(SelectionState.ACTIVE)){
            this.preHoverState = this.selectionState;
            setSelectionState(SelectionState.HOVER);
        }
        
    }
    
    /**
     * Undo the change made by the last setHoverState() action and change the state
     * back to its value immediately before that action.
     */
    public void revertHoverState() {
        if(selectionState!= null && this.selectionState.equals(SelectionState.HOVER)){
            if(this.preHoverState == null){
                this.preHoverState = selectionState.DEFAULT;
            }
            setSelectionState(this.preHoverState);
        }
    }
       
    /**
     * Return the list of Pins (Connection Points) of this component. The global
     * pins are in world co-ordinates.
     * 
     * @return a collection of the pins of this component.
     */
    public  Collection<Pin> getGlobalPins(){
        return globalPins;
    }
    
    /**
     * Remove all old pins from the connection point grid and then recreate the 
     * pins from the local pins of this component, adding them back to the grid 
     * if necessary.
     */
    protected void setGlobalPins(){
        for(Pin p: globalPins){
            parent.getGrid().removePin(p);
        }         
        
        globalPins.clear();
        
        cosTheta = Math.cos(rotation);
        sinTheta = Math.sin(rotation);
        
        for(Point p: getLocalPins()){
            Point rotP = rotate(p); 
            Pin pin = new Pin(this,
                    rotP.x +getOrigin().x-getCentre().x,
                    rotP.y +getOrigin().y-getCentre().x);
            globalPins.add(pin);
            if(isFixed()){ 
                parent.getGrid().addPin(pin);
            }
        }        
    }
    
    /**
     * Return a list of the pins belonging to this component in their local
     * co-ordinates. Local pins are used to do all processing (e.g. for crossovers
     * of wires) and generation of connection points before they are added to the grid.
     * 
     * @return
     */
    protected  Collection<Point> getLocalPins(){
        return localPins;
    }
    
    /**
     * Set the pins of this component in local co-ordinates. Local pins are used 
     * to do all processing (e.g. for crossovers of wires) and generation of 
     * connection points before they are added to the grid. All subclasses must 
     * implement this method.
     */
    protected abstract void setLocalPins();
    
    /**
     * Returns true if, and only if, the specified point lies with in this point.
     * 
     * @param point The point to test.
     * @return
     */
    public abstract boolean containsPoint(Point point);
    
    /**
     * Returns true if, and only if, the invalid areas of this component lie entirely
     * within the specified rectangle. Used primarily for testing inclusion in a
     * selection box.
     * 
     * @param rect The rectangle to test for inclusion in
     * @return
     */
    public boolean containedIn(Rectangle rect) {
        return rect.contains(getInvalidAreas());
    }
    
    /**
     * Draw this component to the graphics object specified.
     * 
     * @param g The graphics object to draw to.
     */
    public void draw(Graphics2D g){            
        if(hasLabel()){
            g.setColor(UIConstants.LABEL_TEXT_COLOUR);
            g.drawString(getLabel(), 
                    getOrigin().x+UIConstants.LABEL_COMPONENT_X_OFFSET,
                    getOrigin().y+UIConstants.LABEL_COMPONENT_Y_OFFSET);
        }
    }

    /**
     * Helper method to calculate the rotation of Point p about the centre Point
     * of the component in the clockwise direction.
     * 
     * @param p The point to rotate
     * @return The new rotated location
     */
    protected Point rotate(Point p) {
        Point transP = new Point(p.x - getCentre().x, p.y - getCentre().y);
        Point rotP = new Point((int) ((transP.x * cosTheta) - (transP.y * sinTheta)),
                (int) ((transP.y * cosTheta) + (transP.x * sinTheta)));
        Point ansP = new Point(rotP.x + getCentre().x, rotP.y + getCentre().y);
        return parent.getGrid().snapPointToGrid(ansP);
    }

    /**
     * Visitor method for saving a file. Each component is visited and must provide
     * and XML representation of itself to the TransformerHandler.
     * 
     * @param hd The TransformerHandler which will record the XML.
     */
    public abstract void createXML(TransformerHandler hd);   
    
    /**
     * The following methods must all change the state appropriately upon the 
     * corresponding mouse actions being called.
     * @param e The MouseEvent of the calling action.
     */
    public abstract void mouseDragged(MouseEvent e);
    public abstract void mouseMoved(MouseEvent e);
    public abstract void mouseClicked(MouseEvent e);
    public abstract void mouseEntered(MouseEvent e);
    public abstract void mouseExited(MouseEvent e);
    public abstract void mousePressed(MouseEvent e);
    public abstract void mouseReleased(MouseEvent e);
    public abstract void mouseDraggedDropped(MouseEvent e);    
    
    /**
     * Similar to clone except that the exception is caught and the Cast to 
     * SelectableComponent is also done.
     * 
     * @return Return an exact copy of this component. 
     */
    public SelectableComponent copy(){
        try {
            return (SelectableComponent) this.clone();
        } catch (CloneNotSupportedException ex) {
            ui.error.ErrorHandler.newError("Component Copying Error",
                    "It is not possible to copy this component.",ex);
        }
        return null;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
