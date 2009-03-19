package ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.LinkedList;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import ui.CircuitPanel;
import ui.UIConstants;
import ui.grid.Grid;

/**
 * This special drawable component simulates a wire between components. Wires are
 * contructed in L-shaped legs between waypoints. Legs take a horizontal path first 
 * and then a vertical path joining points on the Grid. This class also 
 * optimises some wires by removing loops and duplicate waypoints. 
 * 
 * Wires can be moved by selecting the "handle" in the middle of a leg section 
 * (excluding the initial and final segments), or by selecting the start or end 
 * point. Although connections and crossovers are drawn and stored on the grid the 
 * actual flow of data is controlled by the sister Wire class in the Sim package.
 *  
 * @author Matt
 * @see Grid
 * @see sim.pin.Wire
 */
public class Wire extends SelectableComponent {

    private Point endPoint = SelectableComponent.getDefaultOrigin();
    // NOTE: Waypoints are specified in Global (World) Co-ordinates
    private LinkedList<Point> waypoints = new LinkedList<Point>(); 
    private int x1 = 0,  y1 = 0,  x2 = 0,  y2 = 0,  x3 = 0,  y3 = 0;
    private Point hoverWaypoint;
    private Point hoverMousePoint = SelectableComponent.getDefaultOrigin();
    private Point reportedSelfCrossover = null;
    private Color wireColour = UIConstants.DEFAULT_COMPONENT_COLOUR;
    private sim.joinable.Wire logicalWire;

    public Wire(CircuitPanel parent){
        super(parent, null, null, null);
        setLocalPins();     
        addListeners();
        logicalWire = new sim.joinable.Wire();
    }
    
    @Override
    public String getName(){
        return "Wire";
    }

    @Override
    public String getKeyName() {
        return "Wire";
    } 
    
    public sim.joinable.Wire getLogicalWire() {
        return logicalWire;
    }
   
    /** Don't do anything, we don't want to rotate wires */
    @Override
    public void setRotation(double rotation, boolean updateGrid){}

    @Override
    public void setParent(CircuitPanel parent) {
        this.parent = parent;
        this.logicalWire = new sim.joinable.Wire();
        refreshLocalPins();
        setLocalPins();
    }

    @Override
    public void translate(int dx, int dy, boolean fixed) {        
        ui.grid.Grid grid = parent.getGrid();
        if(grid.canTranslateComponent(this, dx, dy)|| (dx == 0 && dy == 0)){
            if(this.fixed && !fixed) {
                // Rememeber my position at the moment I started to move
                unFixedPoint = origin.getLocation();
                unsetGlobalPins();
            }         
            // Fix loops if finished drawing wire
            if(!this.fixed && fixed){
                fixSelfCrossover();
            }
            // Small speed up, no point in moving things if the translation is 0
            if(dx != 0 || dy != 0){ 
                this.origin.translate(dx, dy);
                this.endPoint.translate(dx, dy);
                for (Point p : waypoints) {
                    p.translate(dx, dy);
                }            
                setInvalidAreas();
                setBoundingBox();  
            }           
            this.fixed = fixed; 
            if(fixed){
                setLocalPins();
                setGlobalPins();  
            }
        }
    }

    /** @return The last point of the wire. */
    public Point getEndPoint() {
        return endPoint;
    }

    /** Change the endpoint of a wire. Once the end point has been set, 
     * the #moveEndPoint() should be used instead. */
    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;      
        // We only need to update the pins on the last leg of the wire
        if(!origin.equals(SelectableComponent.getDefaultOrigin())){
            setLastLegPins();
        }        
        optimiseWireEnds();        
        setBoundingBox();
    }
    
    /** Move the end point but also update the waypoints if appropriate.
     * @param p The new end point. */
    public void moveEndPoint(Point p) {   
        setEndPoint(p);       
        if(!waypoints.isEmpty() && waypoints.getLast().equals(endPoint)){
            Point last = waypoints.removeLast();
            Point lastButOne;
            if(waypoints.size()==0){
                lastButOne = origin;
            } else {
                lastButOne = waypoints.getLast();
            } 
            createLeg(lastButOne, last);
            addWaypoint(new Point(x2,x2));                                
        }
        removeDuplicateWaypoints();        
        setLocalPins();
        setGlobalPins();         
        setBoundingBox();
    }

    /** Change the start point of a wire. Once the start point has been set, 
     * the #moveStartPoint() should be used instead.
     * @param startPoint The new start point */
    public void setStartPoint(Point startPoint) {
        this.origin = startPoint;    
    }
    
    /** Move the start point but also update the waypoints if appropriate.
     * @param p The new start point. */
    public void moveStartPoint(Point p) {
        setStartPoint(p);
        setLocalPins();
        setGlobalPins(); 
        setBoundingBox();
    }
    
    @Override
    public Point getCentre() {
        return new Point(0, 0);
    }
    
    /** Append a waypoint to the end of the waypoint list for this wire. */
    public void addWaypoint(Point wp) {               
        // Set start equal to the penultimate waypoint (possibly the start point)
        int len = waypoints.size();
        Point start = null;
        if(len == 1){
            start = origin;
        } else if (len > 1){
            start = waypoints.get(len - 2);
        }        
        // Remove waypoints that lie on the same line
        if(start != null){ 
            removeCommonLineWaypoints(start, waypoints.getLast(), wp, true);
        }  
            
        waypoints.add(wp);           
        setInvalidAreas();
        setBoundingBox();  
    }
    
    @Override
    protected void setLocalPins() {
        localPins = new LinkedList<Pin>();
        
        if (waypoints != null) {
            Point current = origin;
            Point next = origin;
            Point last = endPoint;        
            LinkedList<Point> oldwaypoints = new LinkedList<Point>();
            oldwaypoints.addAll(waypoints);                      
            
            for (Point waypoint : waypoints) {                
                next = waypoint;
                setPinsOnLeg(current, next);
                current = waypoint;
            }
            setPinsOnLeg(next, last);            
            localPins.add(new Pin(endPoint.x-getOrigin().x, endPoint.y-getOrigin().y));
            
            fixSelfCrossover();
        
            // If waypoints have changed, we need to reset the local, and global pins again
            boolean waypointsHaveChanged = false;
            for(Point p: oldwaypoints){
                if(!waypoints.contains(p)){
                    waypointsHaveChanged = true;
                    break;
                }
            }
            if(waypointsHaveChanged){
                setLocalPins();
            }        
        }     
    }

    private void optimiseWireEnds() {
        // Check overlap of middle point of first leg with other wires
        if(!waypoints.isEmpty()){
            Grid grid = parent.getGrid();
            if (waypoints.size() > 0) {
                createLeg(origin, waypoints.getFirst());
            } else {
                createLeg(origin, endPoint);
            }
            if (grid.isConnectionPoint(new Point(x2, y2))) {
                for (Pin p: grid.getConnectionPoint(new Point(x2, y2)).getConnections()) {
                    if (p.getParent() instanceof Wire) {
                        origin = new Point(x2, y2);
                        break;
                    }
                }
            }

            // Check overlap of middle point of last leg with other wires
            if(fixed){
                if (waypoints.size() > 0) {
                    createLeg(waypoints.getLast(), endPoint);
                } else {
                    createLeg(origin, endPoint);
                }
                if (grid.isConnectionPoint(new Point(x2, y2))) {
                    for (Pin p : grid.getConnectionPoint(new Point(x2, y2)).getConnections()) {
                        if (p.getParent() instanceof Wire) {
                            endPoint = new Point(x2, y2);
                            break;
                        }
                    }
                }
            }
        }
    }
        
    /** Similar to setLocalPins() except it only does so for the last leg. 
     * used when adding waypoints and moving the end point so that the whole wire
     * does not have to be replotted. */
    private void setLastLegPins() {
        // Create pins between last waypoint and end point
        if(!waypoints.isEmpty()){
            // Backup waypoints, for later test of changes
            int waypointIndex = 0;            
            LinkedList<Point> oldwaypoints = new LinkedList<Point>();
            oldwaypoints.addAll(waypoints);
            
            // Remove old pins, and note the index of the last waypoint pin
            LinkedList<Pin> oldPins = new LinkedList<Pin>();
            Point wp = waypoints.getLast();
            for(int i = localPins.size()-1;i>=0;i--){
                Pin p = localPins.get(i);
                oldPins.add(p);
                parent.getGrid().removePin(p);
                if(p.getGlobalLocation().equals(wp)){
                    waypointIndex = i;
                    break;
                }                
            }            
            localPins.removeAll(oldPins);           
            
            // Create new local pins
            setPinsOnLeg(waypoints.getLast(), endPoint);
            localPins.add(new Pin(endPoint.x-getOrigin().x,endPoint.y-getOrigin().y));
            
            fixSelfCrossover();
        
            // If waypoints have changed, we need to reset the local, and global pins again
            boolean waypointsHaveChanged = false;
            for(Point p: oldwaypoints){
                if(!waypoints.contains(p)){
                    waypointsHaveChanged = true;
                    break;
                }
            }
            if(waypointsHaveChanged){
                setLastLegPins();
            }        
            
            // Add new pins to grid
            if(isFixed()){
                for(int i = waypointIndex; i<localPins.size(); i++){
                    parent.getGrid().addPin(localPins.get(i));
                }
            }
        // Set pins between the start and end point
        } else {
            unsetGlobalPins();
            localPins.clear();            
            setPinsOnLeg(origin, endPoint);
            localPins.add(new Pin(endPoint.x-getOrigin().x, endPoint.y-getOrigin().y));
            setGlobalPins();
        }      
    }
    
    /**  hoverMousePoint is the current mouse point hovering over the wire */
    @Override
    public void mouseDragged(MouseEvent e) {
        setSelectionState(SelectionState.ACTIVE);
        
        if(isFixed()){
            Point p = Grid.snapToGrid(e.getPoint());
            
            Rectangle startPointRectangle = new Rectangle(getOrigin().x-UIConstants.WIRE_HOVER_THICKNESS,
                getOrigin().y-UIConstants.WIRE_HOVER_THICKNESS,
                2*UIConstants.WIRE_HOVER_THICKNESS,
                2*UIConstants.WIRE_HOVER_THICKNESS);
            Rectangle endPointRectangle = new Rectangle(getEndPoint().x-UIConstants.WIRE_HOVER_THICKNESS,
                    getEndPoint().y-UIConstants.WIRE_HOVER_THICKNESS,
                    2*UIConstants.WIRE_HOVER_THICKNESS,
                    2*UIConstants.WIRE_HOVER_THICKNESS);
            boolean isOverStartPoint = startPointRectangle.contains(hoverMousePoint);
            boolean isOverEndPoint = endPointRectangle.contains(hoverMousePoint);

            if(isOverStartPoint){
                moveStartPoint(p);
                hoverMousePoint = p;
            } else if (isOverEndPoint){
                moveEndPoint(p);
                hoverMousePoint = p;                
            // Moving a segment of the wire
            } else if(hoverWaypoint!=null && !hoverWaypoint.equals(endPoint)){
                parent.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                int i = waypoints.indexOf(hoverWaypoint);
                
                // We have more that one waypoint, let's get the i-1 th waypoint and move the right part of the wire
                if(i > 0 && i < waypoints.size()){ 
                                    
                    Point previousWaypoint = waypoints.get(i-1);
                    // There is no intermeditate point in the joining wire between the two waypoints (vertical)
                    if(previousWaypoint.x == hoverWaypoint.x){ 
                        createLeg(previousWaypoint, hoverWaypoint);
                        if(previousWaypoint.equals(new Point(x2, y2))){
                            hoverWaypoint.x = p.x;
                        } else {
                            previousWaypoint.x = p.x;
                        }                    
                        hoverMousePoint.x = p.x;
                    } else
                    // There is no intermeditate point in the joining wire between the two waypoints (horizontal)
                    if(previousWaypoint.y == hoverWaypoint.y){ 
                        createLeg(previousWaypoint, hoverWaypoint);
                        if(previousWaypoint.equals(new Point(x2, y2))){
                            hoverWaypoint.y = p.y;
                        } else {
                            previousWaypoint.y = p.y;
                        }  
                        hoverMousePoint.y = p.y;
                    } else
                    // Just move the horizontal part of the wire leg
                    if(previousWaypoint.y == hoverMousePoint.y){ 
                        previousWaypoint.y = p.y;
                        hoverMousePoint.y = p.y;
                    } else
                    // Just move the vertical part of the wire leg
                    if(hoverWaypoint.x == hoverMousePoint.x){ 
                        hoverMousePoint.x = p.x;
                        hoverWaypoint.x = p.x;
                    }
                } else if(i == 0) { 
                    createLeg(origin, hoverWaypoint);
                    Line2D.Double l2 = new Line2D.Double(x2, y2, x3, y3);
                    if(l2.ptSegDist(hoverMousePoint)==0.0){
                        hoverWaypoint.x = p.x;
                        hoverMousePoint.x = p.x;
                    }                
                }
            // Special Case for horizonal section of last leg
            } else if (hoverWaypoint!=null 
                    && hoverWaypoint.equals(endPoint) 
                    && !waypoints.isEmpty()){
                parent.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                Point lastWaypoint = waypoints.getLast();
                createLeg(lastWaypoint, endPoint);
                Line2D.Double l1 = new Line2D.Double(x1, y1, x2, y2);
                if(l1.ptSegDist(hoverMousePoint)==0.0){
                    lastWaypoint.y = p.y;
                    hoverMousePoint.y = p.y;
                }     
            }      
        }
    }

    @Override
    public void mouseDraggedDropped(MouseEvent e) {
        setSelectionState(selectionState.ACTIVE);
        if(isFixed()){
            parent.setCursor(Cursor.getDefaultCursor());
            removeCommonLineWaypoints();      
            unsetGlobalPins();
            setLocalPins();
            setGlobalPins();  
            setBoundingBox();
        }
        hoverMousePoint = Grid.snapToGrid(e.getPoint());
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isFixed() && !getSelectionState().equals(SelectionState.ACTIVE)) {
            setSelectionState(SelectionState.DEFAULT);
            wireColour = UIConstants.DEFAULT_COMPONENT_COLOUR;
        }
        hoverMousePoint = Grid.snapToGrid(e.getPoint());
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (isFixed()) {
            if (getSelectionState().equals(SelectionState.ACTIVE)) {
                setSelectionState(SelectionState.HOVER);
                wireColour = UIConstants.DEFAULT_COMPONENT_COLOUR;
            } else {
                setSelectionState(SelectionState.ACTIVE);
                wireColour = UIConstants.ACTIVE_COMPONENT_COLOUR;
            }
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {throw new UnsupportedOperationException("Not supported yet.");  }
    @Override
    public void mouseReleased(MouseEvent e) {
        setSelectionState(SelectionState.ACTIVE);
        wireColour = UIConstants.ACTIVE_COMPONENT_COLOUR;
    }

    @Override
    public void resetDefaultState() {
        super.resetDefaultState();
        wireColour = UIConstants.DEFAULT_COMPONENT_COLOUR;
    }

    /** Detect whether the mid points in two line legs lie on the same line. If so
     * remove the mid waypoint as it is unneeded. */
    private void removeCommonLineWaypoints() {
        if (waypoints != null && isFixed()) {
            Point previous = origin;
            Point current = origin;
            Point next = origin;
            Point last = endPoint;
            
            int len = waypoints.size();
            if(len == 1){
                removeCommonLineWaypoints(previous, waypoints.get(0), last, true);
            } else if (len > 1){
                int i=0;               
                while(i<len){
                    if(i > 0){                       
                        previous = waypoints.get(i-1);
                    }                    
                    current = waypoints.get(i);
                    
                    if(i==(len-1)){
                        next = last;
                    } else {    
                        next = waypoints.get(i+1);
                    }
                    
                    removeCommonLineWaypoints(previous, current, next, true);
                    len = waypoints.size(); // Bug fix: Index out of bounds after deleting waypoints                    
                    i++;
                }
            }
        }
    }
    
    /** Detect whether the mid points in two line legs lie on the same line. If so
     * remove the mid waypoint as it is unneeded. 
     * 
     * @param last    - The waypoint at position i-1
     * @param current - The waypoint at position i
     * @param next    - The waypoint at position i+1
     * @param deleteWaypoint - Should we delete or just move the waypoint?
     * @return Were waypoints removed? */
    private boolean removeCommonLineWaypoints(Point last, Point current, Point next, Boolean deleteWaypoint) {       
        createLeg(last, current);        
        int l_x2 = x2;
        int l_y2 = y2;
        createLeg(current, next);
        
        if((x2 == l_x2 || y2 == l_y2)){
            if(deleteWaypoint && (current.x == next.x || current.y == next.y)){
                waypoints.remove(current);   
            } else {
                current.x = x2; current.y = y2;
            }    
            return true;
        }        
        return false;
    }
    
    /** Remove points that occur in the waypoint list more than once */
    private void removeDuplicateWaypoints() {
        // Find waypoints
        int i = 0;
        int j = 0;
        for (Point ptA : waypoints) {
            i = waypoints.indexOf(ptA);
            j = waypoints.lastIndexOf(ptA) - 1;
            if (i < j) {
                break;
            }
        }

        // Remove waypoints duplicate
        if (i != waypoints.size() - 1) {
            for (int m = 0; m < waypoints.size(); m++) {
                if (m > i && m <= j) {
                    waypoints.remove(m);
                }
            }
        }
        
        // Are waypoints duplicating the start or end points
        if(waypoints.contains(origin)){
            waypoints.remove(origin);
        }
        if(waypoints.contains(endPoint)){
            waypoints.remove(endPoint);
        }
    }
    
    /** Store the location of a reported crossover of the wire (a self loop) 
     * @param p The crossover point (in Line co-ordinates) */
    private void reportSelfCrossover(Point p){        
        if(reportedSelfCrossover == null){        
            reportedSelfCrossover = p;       
        }
    }
    
    /** Finds the waypoints succeeding and preceding the crossover point p and 
     * removes all waypoints between them.
     * A waypoint is then added at point p.*/
    private void fixSelfCrossover(){        
        Point start = null; // The first waypoint after the crossover
        Point end = null; // The waypoint just before the crossover (but after start)

        if (waypoints != null && reportedSelfCrossover != null) {            
            // Translate crossover point to world (circuit) co-ordintates.
            Point p = new Point(reportedSelfCrossover.x+getOrigin().x,
                    reportedSelfCrossover.y+getOrigin().y);            
            Point current = origin;
            Point next = null;

            // Find the first waypoint
            for (Point waypoint : waypoints) {
                next = waypoint;
                // Determine whether the crossover point is on this leg?
                //      i.e. is the distance between it and the horizontal or
                //      vertical sections zero?
                createLeg(current, next);
                Line2D.Double l1 = new Line2D.Double(x1, y1, x2, y2);
                Line2D.Double l2 = new Line2D.Double(x2, y2, x3, y3);
                if(l1.ptSegDist(p)==0.0 || l2.ptSegDist(p)==0.0){
                    start = next; // Select the waypoint after the crossover
                    break;
                } 
                current = waypoint;
            }            

            // Find the last waypoint
            if(start != null){
                current = endPoint;
                next = null;

                // Start from the end
                Iterator<Point> waypointReverse = waypoints.descendingIterator();
                while (waypointReverse.hasNext()) {

                    next = waypointReverse.next(); 
                    // Determine whether the crossover point is on this leg?
                    //      i.e. is the distance between it and the horizontal or
                    //      vertical sections zero?
                    createLeg(next, current);
                    Line2D.Double l1 = new Line2D.Double(x1, y1, x2, y2);
                    Line2D.Double l2 = new Line2D.Double(x2, y2, x3, y3);
                    if(l1.ptSegDist(p)==0.0 || l2.ptSegDist(p)==0.0){
                        end = next;
                        break;
                    } 
                    current = next;
                }

                if(end == null){
                    createLeg(origin, next);
                    Line2D.Double l1 = new Line2D.Double(x1, y1, x2, y2);
                    Line2D.Double l2 = new Line2D.Double(x2, y2, x3, y3);
                    if(l1.ptSegDist(p)==0.0 || l2.ptSegDist(p)==0.0){
                            end = origin;
                    } 
                }                
            }

            // Remove Unneeded Waypoints   
            if(start != null && end != null){
                LinkedList<Point> badWaypoints = new LinkedList<Point>();
                boolean foundStart = false;
                for(Point wp: waypoints){
                    if(wp.equals(start)){
                        foundStart = true;
                    }   
                    if(foundStart){
                        badWaypoints.add(wp);
                    }
                    if(wp.equals(end)){
                        break;
                    }

                }
                int newWaypointIndex = waypoints.indexOf(new Point(start.x, start.y));
                waypoints.removeAll(badWaypoints);
                
                // Add new waypoint and tidy up
                waypoints.add(newWaypointIndex, p);
                removeCommonLineWaypoints();
                removeDuplicateWaypoints();            
            }
        }        
        reportedSelfCrossover = null;
    }
    
    /**
     * Set the co-ordinates of the start, mid and endpoints of the current leg.
     * 
     * @param from
     * @param to
     */
    private void createLeg(Point from, Point to) {
        x1 = from.x;
        y1 = from.y;
        x2 = to.x;
        y2 = from.y;
        x3 = to.x;
        y3 = to.y;
    }

    /**
     * Draw a leg (an L-shaped section between two waypoints)
     * Also draw the dragging handle in the middle of a horizontal or vertical section.
     * 
     * @param g - The Graphics object on which to draw
     * @param from - the start Point
     * @param to - the end Point
     */
    private void drawLeg(Graphics2D g, Point from, Point to) {
        createLeg(from, to);
        
        Rectangle horizontalHoverRectangle = new Rectangle(Math.min(x1,x2),
                Math.min(y1,y2)-UIConstants.WIRE_HOVER_THICKNESS,
                Math.abs(x2-x1),
                2*UIConstants.WIRE_HOVER_THICKNESS);
        Rectangle verticalHoverRectangle = new Rectangle(Math.min(x2,x3)-UIConstants.WIRE_HOVER_THICKNESS,
                Math.min(y2,y3),
                2*UIConstants.WIRE_HOVER_THICKNESS,
                Math.abs(y3-y2));
        Rectangle startPointRectangle = new Rectangle(getOrigin().x-UIConstants.WIRE_HOVER_THICKNESS,
                getOrigin().y-UIConstants.WIRE_HOVER_THICKNESS,
                2*UIConstants.WIRE_HOVER_THICKNESS,
                2*UIConstants.WIRE_HOVER_THICKNESS);
        Rectangle endPointRectangle = new Rectangle(getEndPoint().x-UIConstants.WIRE_HOVER_THICKNESS,
                getEndPoint().y-UIConstants.WIRE_HOVER_THICKNESS,
                2*UIConstants.WIRE_HOVER_THICKNESS,
                2*UIConstants.WIRE_HOVER_THICKNESS);
        
        if(from.equals(origin)){
            horizontalHoverRectangle = new Rectangle();
        }        
        if(to.equals(endPoint)){
            verticalHoverRectangle = new Rectangle();
        }        
        
        boolean isOverStartPoint = startPointRectangle.contains(hoverMousePoint);
        boolean isOverEndPoint = endPointRectangle.contains(hoverMousePoint);

        if(UIConstants.SHOW_WIRE_HOVER_BOXES){
            g.setColor(UIConstants.HOVER_WIRE_COLOUR);
            g.draw(startPointRectangle);
            g.draw(endPointRectangle);
            g.draw(horizontalHoverRectangle);
            g.draw(verticalHoverRectangle);
        }
        
        if(to.equals(hoverWaypoint) && hoverMousePoint != null){

            if(isOverStartPoint){
                g.setColor(UIConstants.HOVER_WIRE_COLOUR);
                Stroke def = g.getStroke();
                g.setStroke(UIConstants.CONNECTED_POINT_STROKE);
                g.drawRect(origin.x-3, origin.y-3, 7, 7); 
                g.setStroke(def);                
            } else if(isOverEndPoint){
                g.setColor(UIConstants.HOVER_WIRE_COLOUR);
                Stroke def = g.getStroke();
                g.setStroke(UIConstants.CONNECTED_POINT_STROKE);
                g.drawRect(endPoint.x-3, endPoint.y-3, 7, 7); 
                g.setStroke(def);   
            } else if(horizontalHoverRectangle.contains(hoverMousePoint)){
                                    
                int handleX0, handleX1, handleY0, handleY1;                
                handleX0 = (x1 + x2 - UIConstants.WIRE_HANDLE_LENGTH) / 2;
                handleX1 = handleX0 + UIConstants.WIRE_HANDLE_LENGTH;
                handleY0 = y1;
                handleY1 = y2;                
                // Allow for very short wires
                if(handleX0 < x1 && x1 < x2){
                    handleX0 = x1;
                    handleX1 = x2;
                }                
                if(handleX0 > x1 && x1 > x2){
                    handleX0 = x1;
                    handleX1 = x2;
                }
                
                switch (getSelectionState()) {                  
                    case ACTIVE:
                        g.setColor(UIConstants.HOVER_WIRE_COLOUR);
                        g.setStroke(UIConstants.ACTIVE_WIRE_STROKE);                        
                        g.drawLine(handleX0, handleY0, handleX1, handleY1);
                        wireColour = UIConstants.ACTIVE_COMPONENT_COLOUR;
                        break;
                    case HOVER:
                        g.setColor(UIConstants.HOVER_WIRE_COLOUR);
                        g.setStroke(UIConstants.ACTIVE_WIRE_STROKE);                        
                        g.drawLine(handleX0, handleY0, handleX1, handleY1);
                        wireColour = UIConstants.DEFAULT_COMPONENT_COLOUR;
                        break;
                    default:
                        wireColour = UIConstants.DEFAULT_COMPONENT_COLOUR;
                        break;
                }   
                
            } else if(verticalHoverRectangle.contains(hoverMousePoint)){
                
                int handleX0, handleX1, handleY0, handleY1;            
                handleX0 = x2;
                handleX1 = x3;
                handleY0 = (y2 + y3 - UIConstants.WIRE_HANDLE_LENGTH) / 2;
                handleY1 = handleY0 + UIConstants.WIRE_HANDLE_LENGTH; 
                // Allow for very short wires
                if(handleY0 < y2 && y2 < y3){
                    handleY0 = y2;
                    handleY1 = y3;
                }                
                if(handleY0 > y2 && y2 > y3){
                    handleY0 = y2;
                    handleY1 = y3;
                }
                
                switch (getSelectionState()) {                  
                    case ACTIVE:
                        g.setColor(UIConstants.HOVER_WIRE_COLOUR);
                        g.setStroke(UIConstants.ACTIVE_WIRE_STROKE);                        
                        g.drawLine(handleX0, handleY0, handleX1, handleY1);
                        wireColour = UIConstants.ACTIVE_COMPONENT_COLOUR;
                        break;
                    case HOVER:
                        g.setColor(UIConstants.HOVER_WIRE_COLOUR);
                        g.setStroke(UIConstants.ACTIVE_WIRE_STROKE);                        
                        g.drawLine(handleX0, handleY0, handleX1, handleY1);
                        wireColour = UIConstants.DEFAULT_COMPONENT_COLOUR;
                        break;
                    default:
                        wireColour = UIConstants.DEFAULT_COMPONENT_COLOUR;
                        break;
                }                                 
            }            
        }
        g.setColor(wireColour);
        g.setStroke(new BasicStroke(1.0f));
        g.drawLine(x1, y1, x2, y2); 
        g.drawLine(x2, y2, x3, y3);

    }

    @Override
    public void draw(Graphics2D g) {

        // If not default values
        if (!origin.equals(new Point(0, 0)) && !endPoint.equals(new Point(0, 0))) {            
            // Draw each leg along waypoints         
            Point current = origin, next = origin;
            for (Point waypoint : waypoints) {
                next = waypoint;
                drawLeg(g, current, next);
                current = waypoint;
            }
           drawLeg(g, next, endPoint);            
        }
        
        // Debugging
        if(UIConstants.SHOW_WIRE_WAYPOINTS){
            for(Point p: waypoints){
                g.setColor(UIConstants.WIRE_WAYPOINT_COLOUR);
                g.drawOval(p.x-1, p.y-1, 3, 3);
            }           
        }
    }

    /**
     * Sets the localPins, in thier object co-ordinate position along the 
     * horizontal and vertical sections of each leg
     * 
     * @param from The start of the leg
     * @param to The end of the leg
     */
    private void setPinsOnLeg(Point from, Point to) {
        int dx = from.x - origin.x;
        int dy = from.y - origin.y;
        Pin p;
        
        createLeg(from, to);

        if (x2 - x1 > 0) {
            for (int x = 0; x < x2 - x1; x += UIConstants.GRID_DOT_SPACING) {
                p = new Pin(x + dx, dy);
                if(localPins.contains(p)){ reportSelfCrossover(p); } 
                localPins.add(p);
            }
        } else {
            for (int x = 0; x < x1 - x2; x += UIConstants.GRID_DOT_SPACING) {
                p = new Pin(-x + dx, dy);
                if(localPins.contains(p)){ reportSelfCrossover(p);} 
                localPins.add(p);
            }
        }
        if (y3 - y2 > 0) {
            for (int y = 0; y < y3 - y2; y += UIConstants.GRID_DOT_SPACING) {
                p = new Pin(x2 - x1 + dx, y + dy);
                if(localPins.contains(p)){reportSelfCrossover(p);} 
                localPins.add(p);
            }
        } else {
            for (int y = 0; y < y2 - y3; y += UIConstants.GRID_DOT_SPACING) {
                p = new Pin(x2 - x1 + dx, -y + dy);
                if(localPins.contains(p)){reportSelfCrossover(p);} 
                localPins.add(p);
            }
        }
    }
    
    /** A wire does not have any invalid areas. */
    @Override
    protected void setInvalidAreas(){
        this.invalidArea = new Rectangle();
    }

    @Override
    public boolean containsPoint(Point point) {
        boolean retval = false; 
        Point current = origin, next = origin;
        for (Point waypoint : waypoints) {
            next = waypoint;

            createLeg(current, next);
            Line2D.Double l1 = new Line2D.Double(x1, y1, x2, y2);
            Line2D.Double l2 = new Line2D.Double(x2, y2, x3, y3);
            retval = l1.ptSegDist(point)==0.0 || l2.ptSegDist(point)==0.0; 

            current = next;
            if(retval){
                break;
            }
        }
        if(retval){
            hoverWaypoint = current;
            return retval;
        }
        
        createLeg(next, endPoint);        
        Line2D.Double l1 = new Line2D.Double(x1, y1, x2, y2);
        Line2D.Double l2 = new Line2D.Double(x2, y2, x3, y3);
        retval = l1.ptSegDist(point)==0.0 || l2.ptSegDist(point)==0.0; 

        if(retval){
            hoverWaypoint = endPoint;
        } else {
            hoverWaypoint = null;
        }
        
        return retval;
    }
    
    @Override
    public boolean containedIn(Rectangle selBox) {
        boolean retval = false;
        Point current = origin, next = origin;
        for (Point waypoint : waypoints) {
            next = waypoint;

            createLeg(current, next);
            retval = retval || (selBox.contains(x1, y1) &&
                    selBox.contains(x2, y2) &&
                    selBox.contains(x3, y3));

            current = waypoint;
        }
        createLeg(next, endPoint);
        retval = retval || (selBox.contains(x1, y1) &&
                selBox.contains(x2, y2) &&
                selBox.contains(x3, y3));

        return retval;
    }
    
    public void createXML(TransformerHandler hd) {
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "", "startx", "CDATA", String.valueOf(origin.x));
            atts.addAttribute("", "", "starty", "CDATA", String.valueOf(origin.y));
            atts.addAttribute("", "", "endx", "CDATA", String.valueOf(endPoint.x));
            atts.addAttribute("", "", "endy", "CDATA", String.valueOf(endPoint.y));
            hd.startElement("", "", "wire", atts);
            for (Point p: waypoints) {
                atts.clear();
                atts.addAttribute("", "", "x", "CDATA", String.valueOf(p.x));
                atts.addAttribute("", "", "y", "CDATA", String.valueOf(p.y));
                hd.startElement("", "", "waypoint", atts);
                hd.endElement("", "", "waypoint");
            }            
            hd.endElement("", "", "wire");            
        } catch (SAXException ex) {
            ui.error.ErrorHandler.newError("XML Creation Error","Please refer to the system output below",ex);
        }
    }

    @Override
    protected void setBoundingBox(){
        boundingBox = new Rectangle(origin);
        boundingBox.add(endPoint);
        for(Point wp: waypoints){
            boundingBox.add(wp);
        }
        boundingBox.grow(10, 10); // Allow for connection points
        boundingBox = rotate(boundingBox);  
    }

    @Override
    public int getWidth() {
        return (int) boundingBox.getWidth();
    }

    @Override
    public int getHeight() {
        return (int) boundingBox.getHeight();
    }
    
    /** Make new copy of the waypoints list, to resolve copied pointer issues **/
    private void refreshWaypoints(){
         LinkedList<Point> newwaypoints = new LinkedList<Point>();
         for(Point wp: waypoints){
             newwaypoints.add(wp.getLocation());
         }
         waypoints = newwaypoints;
    }
    
    @Override
    public SelectableComponent copy(){
        try {
            Wire retval = (Wire) this.clone();
            retval.setOrigin(getOrigin().getLocation());
            retval.endPoint = getEndPoint().getLocation();      
            retval.refreshLocalPins();         
            retval.refreshWaypoints();    
            retval.unsetGlobalPins();
            retval.setBoundingBox();
            retval.setInvalidAreas();
            return retval;
        } catch (CloneNotSupportedException ex) {
            ui.error.ErrorHandler.newError("Component Copying Error",
                    "It is not possible to copy this component.",ex);
        }
        return null;
    }
}