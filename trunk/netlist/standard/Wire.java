/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netlist.standard;

import java.awt.BasicStroke;
import java.awt.Cursor;
import ui.tools.*;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.LinkedList;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import ui.CircuitPanel;
import ui.UIConstants;
import ui.grid.Pin;

/**
 *
 * @author Matt
 */
public class Wire extends SelectableComponent {

    private Point endPoint = new Point(0, 0);
    private Point startPoint = new Point(0, 0);
    private LinkedList<Point> waypoints = new LinkedList<Point>(); // NOTE: Waypoints are specified in Global (World) Co-ordinates
    private int x1 = 0,  y1 = 0,  x2 = 0,  y2 = 0,  x3 = 0,  y3 = 0;
    private Point hoverWaypoint;
    private Point hoverMousePoint = new Point(0,0);

    public Wire(CircuitPanel parent){
        super(parent, null);
    }
    
    public Wire(CircuitPanel parent, Point o) {
        super(parent, null); // Ignore point, as we are going to drag to start it
    }
    
    @Override
    public String getName(){
        return "Wire";
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
        boolean retval = false; 
        Point current = startPoint, next = startPoint;
        for (Point waypoint : waypoints) {
            next = waypoint;

            createLeg(current, next);
            retval = ((point.x >= x1 && point.x <= x2) &&
                    (point.y >= y1 && point.y <= y2)) ||
                    ((point.x >= x2 && point.x <= x3) &&
                    (point.y >= y2 && point.y <= y3)) ||
                    ((point.x <= x1 && point.x >= x2) &&
                    (point.y <= y1 && point.y >= y2)) ||
                    ((point.x <= x2 && point.x >= x3) &&
                    (point.y <= y2 && point.y >= y3));

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
        retval = ((point.x >= x1 && point.x <= x2) &&
                (point.y >= y1 && point.y <= y2)) ||
                ((point.x >= x2 && point.x <= x3) &&
                (point.y >= y2 && point.y <= y3)) ||
                ((point.x <= x1 && point.x >= x2) &&
                (point.y <= y1 && point.y >= y2)) ||
                ((point.x <= x2 && point.x >= x3) &&
                (point.y <= y2 && point.y >= y3));

        if(retval){
            hoverWaypoint = endPoint;
        } else {
            hoverWaypoint = null;
        }
        
        return retval;
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
        for (Point p : waypoints) {
            p.translate(dx, dy);
        }
        setInvalidAreas();
        this.fixed = fixed; 
        if(fixed){
            wasEverFixed = true;
        }
        setLocalPins();
        setGlobalPins();       
        
    }

    @Override
    public void draw(Graphics2D g) {
        
        // Find duplicate waypoints
        int i = 0, j = 0;
        dups: for(Point ptA: waypoints){             
            i = waypoints.indexOf(ptA);
            j = waypoints.lastIndexOf(ptA)-1;
            if(i < j){
                break dups;
            }  
        }
       
        // Remove waypoints duplicate
        if(i != waypoints.size()-1){
            for(int m = 0; m < waypoints.size(); m++){
                if(m > i && m <= j ){
                    waypoints.remove(m);
                }
            }
        }

        // If not default values
        if (!startPoint.equals(new Point(0, 0)) && !endPoint.equals(new Point(0, 0))) {
            
            // Draw each leg along waypoints         
            Point current = startPoint, next = startPoint;
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

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;      
        
        // Detect and resolve wire overlaps
        int len = waypoints.size();
        Point start = null;
        if(len == 1){
            start = startPoint;
        } else if (len > 1){
            start = waypoints.get(len - 2);
        }
        if(start != null){
            removeCommonLineWaypoints(start, waypoints.getLast(), endPoint, isFixed());
        }  
    }
    
    public void moveEndPoint(Point p) {        
        
        setEndPoint(p);
        if(!waypoints.isEmpty()){
            
            if(waypoints.getLast().x == endPoint.x && waypoints.getLast().y == endPoint.y){
                Point last = waypoints.removeLast();
                Point lastButOne;
                if(waypoints.size()==0){
                    lastButOne = startPoint;
                } else {
                    lastButOne = waypoints.getLast();
                } 
                createLeg(lastButOne, last);
                addWaypoint(new Point(x2,x2));
                                
            } 
            
        }
        
//        // Find duplicates and remove waypoints between them
//        int i = 0, j = 0;
//        dups: for(Point ptA: waypoints){             
//            i = waypoints.indexOf(ptA);
//            j = waypoints.lastIndexOf(ptA)-1;
//            if(i < j){
//                break dups;
//            }
//  
//        }
//       
//        if(i != waypoints.size()-1){
//            for(int m = 0; m < waypoints.size(); m++){
//                if(m > i && m <= j ){
//                    waypoints.remove(m);
//                }
//            }
//        }
        
        setLocalPins();
        setGlobalPins(); 
        
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
        
        // Detect and resolve wire overlaps
        int len = waypoints.size();
        Point last = null;
        if(len == 1 && isFixed()){
            last = endPoint;
        } else if (len > 1){
            last = waypoints.get(1);
        }
        if(last != null){
            removeCommonLineWaypoints(startPoint, waypoints.get(0), last, true);
        }  
        
    }
    
    public void moveStartPoint(Point p) {

        setStartPoint(p);
        setLocalPins();
        setGlobalPins(); 
          
    }
    
    public boolean reportSelfCrossover(Point p){
        
        Point start = null;
        Point end = null;
        
        if (waypoints != null) {
            Point current = startPoint;
            Point next = startPoint;
            Point last = endPoint;
            
            forbreak: for (Point waypoint : waypoints) {
               
                next = waypoint;

                createLeg(current, next);
                Line2D.Double l1 = new Line2D.Double(x1, y1, x2, y2);
                Line2D.Double l2 = new Line2D.Double(x2, y2, x3, y3);
                if(l1.ptLineDist(p)==0.0 || l2.ptLineDist(p)==0.0){
                    if(start == null){
                        start = current;
                    } else {
                        end = next;
                        break forbreak;
                    }
                } 
                
                // Don't count connections between legs
                if(next.equals(start)){
                    start = null;
                }
                current = waypoint;
                
            }
            
            createLeg(next, last);
            Line2D.Double l1 = new Line2D.Double(x1, y1, x2, y2);
            Line2D.Double l2 = new Line2D.Double(x2, y2, x3, y3);
            if(l1.ptLineDist(p)==0.0 || l2.ptLineDist(p)==0.0){
                    end = last;
            } 
            
            if(start!=null && end != null && !waypoints.contains(new Point(p.x, p.y))){
                System.out.println(start + " " + end + " " + p);
                
                LinkedList<Point> badWaypoints = new LinkedList<Point>();
                boolean foundStart = false;
                for(Point wp: waypoints){
                    if(wp.equals(end)){
                        break;
                    }
                    if(foundStart){
                        badWaypoints.add(wp);
                    }                    
                    if(wp.equals(start)){
                        foundStart = true;
                    }                    
                }
                
                waypoints.removeAll(badWaypoints);
                waypoints.add(waypoints.indexOf(start)+1, p);
                
                return true;
            }
        }
        return false;
        
        
    }

    public void addWaypoint(Point wp) {
               
//        // Detect and resolve wire overlaps
//        int len = waypoints.size();
//        Point start = null;
//        if(len == 1){
//            start = startPoint;
//        } else if (len > 1){
//            start = waypoints.get(len - 2);
//        }
//        if(start != null){ 
//            removeCommonLineWaypoints(start, waypoints.getLast(), wp, true);
//        }  
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

    private void drawLeg(Graphics2D g, Point from, Point to) {

        createLeg(from, to);
        
        if(to.equals(hoverWaypoint)){
            if(hoverMousePoint == null){
                hoverMousePoint = new Point(0,0);
            }
            if (((x1 <= hoverMousePoint.x && x2 >= hoverMousePoint.x)
                    ||(x1 >= hoverMousePoint.x && x2 <= hoverMousePoint.x))
               && ((y1-UIConstants.WIRE_HOVER_THICKNESS <= hoverMousePoint.y && y2+UIConstants.WIRE_HOVER_THICKNESS >= hoverMousePoint.y)
                    ||(y1+UIConstants.WIRE_HOVER_THICKNESS >= hoverMousePoint.y && y2-UIConstants.WIRE_HOVER_THICKNESS <= hoverMousePoint.y))){
                                    
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
                        g.setColor(UIConstants.ACTIVE_COMPONENT_COLOUR);
                        g.setStroke(UIConstants.ACTIVE_WIRE_STROKE);                        
                        g.drawLine(handleX0, handleY0, handleX1, handleY1);
                        break;
                    case HOVER:
                        g.setColor(UIConstants.HOVER_WIRE_COLOUR);
                        g.setStroke(UIConstants.ACTIVE_WIRE_STROKE);                        
                        g.drawLine(handleX0, handleY0, handleX1, handleY1);
                        break;
                    default:
                        break;
                }   
                                
            } else if (((y2 <= hoverMousePoint.y && y3 >= hoverMousePoint.y)
                    ||(y2 >= hoverMousePoint.y && y3 <= hoverMousePoint.y))
               && ((x2-UIConstants.WIRE_HOVER_THICKNESS <= hoverMousePoint.x && x3+UIConstants.WIRE_HOVER_THICKNESS >= hoverMousePoint.x)
                    ||(x2+UIConstants.WIRE_HOVER_THICKNESS >= hoverMousePoint.x && x3-UIConstants.WIRE_HOVER_THICKNESS <= hoverMousePoint.x))){
                
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
                        g.setColor(UIConstants.ACTIVE_COMPONENT_COLOUR);
                        g.setStroke(UIConstants.ACTIVE_WIRE_STROKE);                        
                        g.drawLine(handleX0, handleY0, handleX1, handleY1);
                        break;
                    case HOVER:
                        g.setColor(UIConstants.HOVER_WIRE_COLOUR);
                        g.setStroke(UIConstants.ACTIVE_WIRE_STROKE);                        
                        g.drawLine(handleX0, handleY0, handleX1, handleY1);
                        break;
                    default:
                        break;
                }   
                              
            } 
            
            
        }
        g.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);
        g.setStroke(new BasicStroke(1.0f));
        g.drawLine(x1, y1, x2, y2); 
        g.drawLine(x2, y2, x3, y3);

    }

    /*
     * sets the localPins, in thier correct object co-ordinates 
     */
    private void setPinsOnLeg(Point from, Point to) {

        int dx = from.x - startPoint.x;
        int dy = from.y - startPoint.y;
        Point p;
        
        createLeg(from, to);

        if (x2 - x1 > 0) {
            for (int x = 0; x <= x2 - x1; x += UIConstants.GRID_DOT_SPACING) {
                p = new Point(x + dx, dy);
                localPins.add(p);               
            }
        } else {
            for (int x = 0; x <= x1 - x2; x += UIConstants.GRID_DOT_SPACING) {
                p = new Point(-x + dx, dy);
                localPins.add(p);
            }
        }
        if (y3 - y2 > 0) {
            for (int y = 0; y <= y3 - y2; y += UIConstants.GRID_DOT_SPACING) {
                p = new Point(x2 - x1 + dx, y + dy);
                localPins.add(p);
            }
        } else {
            for (int y = 0; y <= y2 - y3; y += UIConstants.GRID_DOT_SPACING) {
                p = new Point(x2 - x1 + dx, -y + dy);
                localPins.add(p);
            }
        }

    }

    @Override
    public Point getCentre() {
        return new Point(0, 0);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        setSelectionState(SelectionState.ACTIVE);
        parent.getParentFrame().getEditor().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        
        // Moving a segment of the wire
        if(hoverWaypoint!=null && !hoverWaypoint.equals(endPoint)){
            int i = waypoints.indexOf(hoverWaypoint);
            Point p = parent.getGrid().snapPointToGrid(e.getPoint());
            
            // We have more that one waypoint, let's get the i-1 th waypoint and move the right part of the wire
            if(i > 0){ 
                Point previousWaypoint = waypoints.get(i-1);
                // There is no intermeditate point in the joining wire between the two waypoints
                if(previousWaypoint.x == hoverWaypoint.x){ 
                    previousWaypoint.x = p.x;
                    hoverWaypoint.x = p.x;
                } else
                // There is no intermeditate point in the joining wire between the two waypoints
                if(previousWaypoint.y == hoverWaypoint.y){ 
                    previousWaypoint.y = p.y;
                    hoverWaypoint.y = p.y;
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
            // We only have one waypoint, just move it
            } else { 
                hoverWaypoint.x = p.x;
                hoverWaypoint.y = p.y;
            }            
        }        
    }

    public void mouseDraggedDropped(MouseEvent e) {
        setSelectionState(selectionState.ACTIVE);
        parent.getParentFrame().getEditor().setCursor(Cursor.getDefaultCursor());
        
        // Remove uneeded waypoints introduced by dragging
        int i = waypoints.indexOf(hoverWaypoint);
        if(i > 0 & i < waypoints.size()-1){ 
            Point previousWaypoint = waypoints.get(i-1);
            Point nextWaypoint = waypoints.get(i+1);
            removeCommonLineWaypoints(previousWaypoint, hoverWaypoint, nextWaypoint, true);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isFixed() && !getSelectionState().equals(SelectionState.ACTIVE)) {
            setSelectionState(SelectionState.DEFAULT);
        }
        hoverMousePoint = parent.getGrid().snapPointToGrid(e.getPoint());
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isFixed()) {
            if (getSelectionState().equals(SelectionState.ACTIVE)) {
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
        if (waypoints != null) {
            Point current = startPoint;
            Point next = startPoint;
            Point last = endPoint;
            
            for (Point waypoint : waypoints) {
                
                next = waypoint;
                setPinsOnLeg(current, next);
                current = waypoint;
                
            }

            setPinsOnLeg(next, last);
        }

    }
    
    @Override
    protected void setGlobalPins(){
        for(Pin p: globalPins){
            parent.getGrid().removePin(p);
        }         
        
        globalPins.clear();
        
        cosTheta = Math.cos(rotation);
        sinTheta = Math.sin(rotation);
        
        LinkedList<Point> oldwaypoints = new LinkedList<Point>();
        oldwaypoints.addAll(waypoints);
        
        for(Point p: getLocalPins()){
            Point rotP = rotate(p); 
            Pin pin = new Pin(this, rotP.x +getOrigin().x-getCentre().x,rotP.y +getOrigin().y-getCentre().x);
            globalPins.add(pin);
            if(isFixed()){ 
                parent.getGrid().addPin(pin);
            }
        }
        
        boolean waypointsHaveChanged = false;
        for(Point p: oldwaypoints){
            if(!waypoints.contains(p)){
                waypointsHaveChanged = true;
            }
        }
        if(waypointsHaveChanged){
            setLocalPins();
            setGlobalPins();
        }
        
    }
    
    @Override
    protected void setInvalidAreas(){
        int maxX = startPoint.x, minX = startPoint.x, maxY = startPoint.y, minY = startPoint.y;
        
        for (Point waypoint : waypoints) {
            if(waypoint.x > maxX){
                maxX = waypoint.x;
            }
            if(waypoint.x < minX){
                minX = waypoint.x;
            }
            if(waypoint.y > maxY){
                maxY = waypoint.y;
            }
            if(waypoint.y < minY){
                minY = waypoint.y;
            }
        }        
             
        if(endPoint.x > maxX){
            maxX = endPoint.x;
        }
        if(endPoint.x < minX){
            minX = endPoint.x;
        }
        if(endPoint.y > maxY){
            maxY = endPoint.y;
        }
        if(endPoint.y < minY){
            minY = endPoint.y;
        }
        
        this.invalidArea = new Rectangle(minX, minY, maxX-minX, maxY-minY);
    }
    
    @Override
    public boolean containedIn(Rectangle selBox) {
        boolean retval = false;
        Point current = startPoint, next = startPoint;
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

    private boolean removeCommonLineWaypoints(Point last, Point current, Point next, Boolean newWaypoint) {
       
        createLeg(last, current);
        
        int l_x2 = x2;
        int l_y2 = y2;

        createLeg(current, next);
        
        if((x2 == l_x2 || y2 == l_y2)){
            if(newWaypoint && (current.x == next.x || current.y == next.y)){
                waypoints.remove(current);   
            } else {
                current.x = x2; current.y = y2;
            }            
            return true;
        }
        
        return false;
    }
    
    public void createXML(TransformerHandler hd) {
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "", "startx", "CDATA", String.valueOf(startPoint.x));
            atts.addAttribute("", "", "starty", "CDATA", String.valueOf(startPoint.y));
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
    protected void setComponentTreeName() {
        componentTreeName = "Standard.Wire";
    }
}
