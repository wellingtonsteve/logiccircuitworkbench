package ui.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import ui.UIConstants;
import ui.components.SelectableComponent;
import ui.components.SelectableComponent.Pin;

/**
 * Pre-object/instance changes = rev 154
 * @author matt
 */
public class Grid {

    /**
     * The Grid represents a 2-dimensional cartesian co-ordinate space where 
     * each point (x,y), has an associated Grid Object which is either a Connection
     * Point or and Invalid Area. 
     * 
     * @see GridObject
     * @see ConnectionPoint
     * @see InvalidPoint
     */
    private HashMap<Point,GridObject> grid = new HashMap<Point,GridObject>();
        
    /**
     * Check whether the specified translation of the component is valid.
     * PRE: sc.getParent().hasActiveSelection()
     * @param sc The component to test
     * @param dx The x-direction displacement
     * @param dy The y-direction displacement
     * @return Is the move valid?
     */
    public boolean canTranslateComponent(SelectableComponent sc, int dx, int dy){
        
        // Check each pin
        for(Pin local: sc.getPins()){
            Point p = local.getGlobalLocation();
            Point temp = new Point(p.x + dx, p.y + dy);
            GridObject go = grid.get(temp);  
            if(go != null 
                    // Pins can overlap other pins but not invalid points
                    && go instanceof InvalidPoint 
                    && (!go.hasParent(sc) || !go.hasParentInCollection(sc.getParent().getActiveComponents()))){
                return false; 
            }           
        }
        
        // Check each internal point of the component
        Rectangle bb = sc.getInvalidArea(); 
        Point origin = snapToGrid(bb.getLocation());
        Point p, temp;
        GridObject go;
        for(int i = origin.x; i <= origin.x + bb.width; i+=UIConstants.GRID_DOT_SPACING){
            for(int j = origin.y; j <= origin.y + bb.height; j+=UIConstants.GRID_DOT_SPACING){
                p = new Point(i, j);
                temp = new Point(p.x + dx, p.y + dy);
                if(bb.contains(p)){
                    go = grid.get(temp);
                    if(go != null 
                            && (!go.hasParent(sc) || !go.hasParentInCollection(sc.getParent().getActiveComponents()))){
                        return false;
                    }
                }
            }
        }        
        return true;
    }
    
    /**
     * Remove the connections and invalid area points from the grid.
     * @param sc The component to remove from the grid.
     */
    public void removeComponent(SelectableComponent sc){
        for(Pin local: sc.getPins()){
            Point p = local.getGlobalLocation();
            if(grid.get(p) instanceof ConnectionPoint){
                ((ConnectionPoint) grid.get(p)).removeConnection(local);
                if(!((ConnectionPoint)grid.get(p)).isConnected()){
                    grid.remove(p);
                }
            } 
        }        
        unmarkInvalidAreas(sc); 
    }   

    /**
     * Add a new pin to the grid. Not the method local.getLocation() returns the
     * local co-ordinates of the pin, so the method local.getGlobalLocation() must
     * be used to get the local in world (grid) co-ordinates.
     * 
     * @param local The pin to add.
     * @return Was the addition successful?
     */
    public boolean addPin(Pin local){
        Point p = local.getGlobalLocation();
        GridObject go = grid.get(p);
        if(go==null){
            grid.put(p.getLocation(), new ConnectionPoint(p));
            go = grid.get(p);
        }
        
        if(go instanceof ConnectionPoint){       
            ((ConnectionPoint) go).addConnection(local);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Remove the specified pin from the grid.
     * @param local The pin to remove.
     */
    public void removePin(Pin local){
        Point p = local.getGlobalLocation();
        GridObject go = grid.get(p);
        if(go instanceof ConnectionPoint){
            ((ConnectionPoint) go).removeConnection(local);
            if(!((ConnectionPoint) go).isConnected()){
                grid.remove(p);
            }
        } 
    }
    
    /**
     * Set invalid areas for a specified selectable component
     * @param sc
     */
    public void markInvalidAreas(SelectableComponent sc){
        Rectangle bb = sc.getInvalidArea();
        for(int i = bb.x; i <= bb.x + bb.width; i+=UIConstants.GRID_DOT_SPACING){
            for(int j = bb.y; j <= bb.y + bb.height; j+=UIConstants.GRID_DOT_SPACING){
                Point p = snapToGrid(new Point(i, j));
                if(bb.contains(p)){
                    grid.put(p, new InvalidPoint(p, sc));
                }
            }
        }  
    }
    
    /**
     * Remove the invalid area grid objects that were set for the specified 
     * selectable component.
     * @param sc
     */
    public void unmarkInvalidAreas(SelectableComponent sc) {    
        Rectangle bb = sc.getInvalidArea();
        for(int i = bb.x; i <= bb.x + bb.width; i+=UIConstants.GRID_DOT_SPACING){
            for(int j = bb.y; j <= bb.y + bb.height; j+=UIConstants.GRID_DOT_SPACING){
                Point p = snapToGrid(new Point(i, j));
                if(bb.contains(p)
                        && grid.get(p) instanceof InvalidPoint 
                        && ((InvalidPoint) grid.get(p)).getParent().equals(sc)){
                    grid.remove(p);
                }
            }
        }
    }
    
    /**
     * @param p The point to check
     * @return Is the GridObject at Point p a ConnectionPoint?
     */
    public boolean isConnectionPoint(Point p){
        return grid.get(p) instanceof ConnectionPoint;
    }
    
    /**
     * @param p
     * @return The connection point at p, or null if Point p is not a connection
     * Point.
     */
    public ConnectionPoint getConnectionPoint(Point p){
        if(grid.get(p) instanceof ConnectionPoint){
            return (ConnectionPoint) grid.get(p);
        } else {
            return null;
        }
    }
    
    /**
     * Mark the GridPoint at p as active/unactive (draw a red box around it). Fails 
     * silently if the point is not a connection point.
     * @param p The point to check
     * @param active Should we mark it as active or unactive?
     */
    public void setActivePoint(Point p, Boolean active) {
        if(isConnectionPoint(p)){
            ((ConnectionPoint) grid.get(p)).setActive(active);
        } 
    }
    
    /**
     * @param old The point to snap.
     * @return a new point that is the nearest point on the grid to the old point.
     */
    public static Point snapToGrid(Point old){
        if(UIConstants.SNAP_TO_GRID){
            
            int d = UIConstants.GRID_DOT_SPACING;            
            int xDivD = (int) old.x / d;
            int yDivD = (int) old.y / d;            
            int newX = xDivD * d;
            int newY = yDivD * d;
            
            if((old.x + (d/2)) >= (xDivD + 1) * d){
                newX += d;
            }
            if((old.y + (d/2)) >=  (yDivD + 1) * d){
                newY += d;
            }
            
            return new Point(newX, newY);            
        } else {
            return old;
        }                
    }
    
    /**
     * Draw the grid.
     * @param g2 The graphics context on which to draw.
     */
    public void draw(Graphics2D g2){
       g2.setColor(UIConstants.CONNECTION_POINT_COLOUR);
        
       for(GridObject p: grid.values()){           
           p.draw(g2);
       }               
    }   
    
    /**
     * Clear the grid and all cached information.
     */
    public void clear(){
        grid.clear();
    }

}
