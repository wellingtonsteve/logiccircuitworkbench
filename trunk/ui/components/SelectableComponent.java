package ui.components;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.LinkedList;
import javax.xml.transform.sax.TransformerHandler;
import sim.pin.*;
import sim.SimItem;
import sim.State;
import ui.CircuitPanel;
import ui.UIConstants;
import ui.grid.ConnectionPoint;
import ui.grid.Grid;

/**
 * SelectableComponents represent the visual components that can be placed
 * and edited on a circuit workarea. 
 * 
 * @author Matt
 */
public abstract class SelectableComponent implements Labeled, Cloneable {
    

    /** Set the default selection state of this component */
    protected SelectionState selectionState = SelectionState.DEFAULT;
    
    /** To store the Selection state of the component before it enters the hover state.*/
    protected SelectionState preHoverState;
    
    /** @see getLogicalComponent() */
    protected SimItem logicalComponent;
    
    /** @see getBoundingBox() */
    protected Rectangle boundingBox = null;
    
    /** @see getInvalidArea() */
    protected Rectangle invalidArea = null;
    
    /** Describes the current fixed state */
    protected boolean fixed = false;    
    
    /** @see getOrigin() */
    private Point origin;
    
    /** @see getLocalPins() */
    protected Collection<Pin> localPins = new LinkedList<Pin>();
        
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
    
    /** Store the point at which this component was unfixed */
    protected Point unFixedPoint;
    
    /**
     * Default constructor for a SelectableComponent. 
     * 
     * @param parent The parent circuit panel to which this component wll be drawn.
     * @param origin The initial origin of this component.
     */
    public SelectableComponent(CircuitPanel parent, Point origin, SimItem logicalComponent){
        this.parent = parent;
        this.logicalComponent = logicalComponent;
        if(origin == null){
            this.origin = new Point(0,0);
        } else {
            this.origin = origin;
        }
                
        setLocalPins();
        setGlobalPins();
    }
    
    /**
     * The parent of this component is the CircuitPanel to which the component is drawn.
     * 
     * @return The Component's parent CircuitPanel
     */
    public CircuitPanel getParent() {
        return parent;
    }
    
    /**
     * Note: components which are listed in circuits who are not thier parent 
     * will not be drawn.
     * 
     * @param parent The new parent Circuit
     */
    public void setParent(CircuitPanel parent) {
        this.parent = parent;
    }
    
    /**
     * @see ui.components.SelectionState
     * @return The state of this component.
     */
    public SelectionState getSelectionState() {
        return selectionState;
    }

    /**
     * Set the selection state of this component.
     * 
     * @see ui.components.SelectionState
     */
    public void setSelectionState(SelectionState selectiontype) {
        this.selectionState = selectiontype;
    }
    
    /**
     * Convenience method to return the state of this component to it's default setting
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
    public SimItem getLogicalComponent() {
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
     * Change the origin of this component. Use with caution, movements of the
     * component should be done using either the moveTo() or translate() methods.
     * 
     * @param origin The new origin.
     */
    public void setOrigin(Point origin){
        this.origin = origin;
    }
    
    /** 
     * Get the point at which this component was unfixed 
     */
    public Point getUnfixedOrigin(){
        return unFixedPoint;
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
        ui.grid.Grid grid = parent.getGrid();

        if(grid.canTranslateComponent(this, dx, dy)){
            for(Pin p: localPins){
                parent.getGrid().removePin(p);
            } 
            
            grid.unmarkInvalidAreas(this);
            this.origin.translate(dx, dy);
            setInvalidAreas();
            setBoundingBox();
            
            // Rememeber my position at the moment I started to move
            if(this.fixed && !fixed){
                unFixedPoint = origin.getLocation();
            }
            
            if (fixed){ 
                grid.markInvalidAreas(this);                 
            }            
            this.fixed = fixed; 
            setGlobalPins();
            
        // Alert the user that this was an invalid move and we cannot fix the 
        // component here
        } else if(!this.fixed && fixed && UIConstants.DO_SYSTEM_BEEP){
            UIConstants.beep();  
        }
    }
    
    /**
     * Convienience method to move the origin of the component to a specified point
     * 
     * @param point The new origin of the component
     * @param fixed Should the component be fixed after the move?
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
    public void setRotation(double rotation, boolean updateGrid) {
        for(Pin p: localPins){
            parent.getGrid().removePin(p);
        } 
        parent.getGrid().unmarkInvalidAreas(this);
        this.rotation = rotation % (Math.PI * 2);
        setInvalidAreas();
        setBoundingBox();
        if(updateGrid){
            setGlobalPins();
            parent.getGrid().markInvalidAreas(this);
        }        
    }
        
    /**
     * Each component specifies an invalid area within which no other component, 
     * pin or wire may be placed.
     * 
     * @return The Invalid Area rectangle for this component (World Co-ordinates).
     */
    public Rectangle getInvalidArea(){
        if(invalidArea == null){
            setInvalidAreas();
        }
        return invalidArea;
    }
    
    /**
     * Each component specifies an invalid area within which no other component, 
     * pin or wire may be placed. It is also this area specifies the area within
     * which mouse movements may select or activate this component. If you are 
     * overriding this method please don't forget to use the rotate method one 
     * you have created your rectangle to ensure that the invalid area matches
     * the orientation of the component.
     * 
     * The Invalid areas are set upon component initialisation and updated dynamically
     * upon translation or rotation.
     */    
    protected void setInvalidAreas(){
        this.invalidArea = new Rectangle(getOrigin().x,getOrigin().y,getWidth(),getHeight());   
    }
    
    /**
     * The bounding box of a component that is redrawn if this component is to
     * be repainted. The Bounding box is set upon component initialisation and updated dynamically
     * upon translation or rotation.
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
     * The bounding box of a component that is redrawn if this component is to
     * be repainted. The Bounding box is set upon component initialisation and updated dynamically
     * upon translation or rotation.
     */
    protected void setBoundingBox() {
        boundingBox = new Rectangle(getOrigin().x-getCentre().x,getOrigin().y-getCentre().y,getWidth(),getHeight());
        boundingBox = rotate(boundingBox);        
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
     * Remove all old pins from the connection point grid and then recreate the 
     * pins from the local pins of this component, adding them back to the grid 
     * if necessary.
     */
    protected void setGlobalPins(){           
        for(Pin p: localPins){
            parent.getGrid().addPin(p);
        }    
    }
    
    /**
     * Return a list of the pins belonging to this component in their local
     * co-ordinates. Local pins are used to do all processing (e.g. for crossovers
     * of wires) and generation of connection points before they are added to the grid.
     * 
     * @return
     */
    protected  Collection<Pin> getLocalPins(){
        return localPins;
    }
    
    
    public Collection<Pin> getPins(){
        return localPins;
    }
    /**
     * Set the pins of this component in local co-ordinates. Local pins are used 
     * to do all processing (e.g. for crossovers of wires) and generation of 
     * connection points before they are added to the grid. All subclasses must 
     * either call the super.setLocalPins() first or remember to reset the list
     * of pins themselves.
     */
    protected void setLocalPins(){
        localPins.clear();
    }
    
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
        return rect.contains(getInvalidArea());
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
        return Grid.snapToGrid(ansP);
    }
    
    /**
     * Helper method to calculate the rotation of Rectangle src about the centre Point
     * of the component in the clockwise direction. Used to rotate the invalid
     * area and bounding box rectangles.
     * 
     * @param src The rectangle to rotate
     * @return The new rotated rectangle
     */
    protected Rectangle rotate(Rectangle src){
        Rectangle retval;
        java.awt.geom.AffineTransform rotationTransformation = new java.awt.geom.AffineTransform();
        rotationTransformation.rotate(rotation, getOrigin().x, getOrigin().y);
        retval = rotationTransformation.createTransformedShape(new Rectangle(src)).getBounds();
        
        return retval;
    }
    
    /**
     * Visitor method for saving a file. Each component is visited and must provide
     * and XML representation of itself to the TransformerHandler.
     * 
     * @param hd The TransformerHandler which will record the XML.
     */
    public abstract void createXML(TransformerHandler hd);   
    
    /**
     * The following methods all change the state appropriately upon the 
     * corresponding mouse actions being called.
     * @param e The MouseEvent of the calling action.
     */
    public void mouseMoved(MouseEvent e) {
        if(!isFixed() && !getSelectionState().equals(SelectionState.ACTIVE)){
             setSelectionState(SelectionState.DEFAULT);
        } 
    }
    public void mouseClicked(MouseEvent e) {
        if(isFixed()){
            if(getSelectionState().equals(SelectionState.ACTIVE)){
                 setSelectionState(SelectionState.HOVER);
            } else {
                 setSelectionState(SelectionState.ACTIVE);
            }
        }
    }
    public void mousePressed(MouseEvent e) {
        ui.error.ErrorHandler.newError("Mouse Press Error", 
                "An action has not yet been defined to pressing the mouse on a \"" +
                getComponentTreeName() + "\".");
    }
    public void mouseReleased(MouseEvent e) {
        setSelectionState(SelectionState.ACTIVE);
    }
    public void mouseDragged(MouseEvent e) {
        setSelectionState(SelectionState.ACTIVE);
    }
    public void mouseDraggedDropped(MouseEvent e) {
        setSelectionState(SelectionState.DEFAULT);
    }
    
    /**
     * Similar to clone except that the exception is caught and the Cast to 
     * SelectableComponent is also done.
     * 
     * @return Return an exact copy of this component. 
     */
    public SelectableComponent copy(){
        try {
            SelectableComponent retval = (SelectableComponent) this.clone();
            retval.setOrigin(new Point(this.getOrigin().x,this.getOrigin().y));
            return retval;
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
    
    public class Pin extends Point implements ValueListener {

        private SelectableComponent parent;
        private State value;
        private ConnectionPoint cp;
        private sim.pin.Pin simPin;

        public Pin(int x, int y){
            super(x,y);
            this.parent = SelectableComponent.this;
        }   

        public Pin(int x, int y, sim.pin.Pin simPin){
            super(x,y);
            this.parent = SelectableComponent.this;
            this.simPin = simPin;
            this.simPin.addValueListener(this);
        }   

        public SelectableComponent getParent(){
            return parent;        
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            } else if (obj instanceof Pin){
                Pin other = (Pin) obj;
                return super.equals(obj) && this.getParent().equals(other.getParent());
            } else {
                return super.equals(obj);
            }
        }

        public void valueChanged(State value) {
            this.value = value;
        }

        public Point getGlobalLocation() {
            Point rotP;
            if(rotation != 0.0){
                cosTheta = Math.cos(rotation);
                sinTheta = Math.sin(rotation);
                rotP = rotate(getLocation()); 
            } else {
                rotP = getLocation();
            }
            Point retval = new Point(
                        rotP.x +getOrigin().x-getCentre().x,
                        rotP.y +getOrigin().y-getCentre().y);
            return retval;
        } 

        public ConnectionPoint getConnectionPoint() {
            return cp;
        }

        public void setConnectionPoint(ConnectionPoint cp) {
            this.cp = cp;
        }
    }
}
