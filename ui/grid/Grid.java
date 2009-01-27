package ui.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import ui.UIConstants;
import ui.components.SelectableComponent;

/**
 * Pre-object/instance changes = rev 154
 * @author matt
 */
public class Grid {
    
    private HashMap<Point,GridObject> grid = new HashMap<Point,GridObject>();

    public void clearBackups(SelectableComponent sc) {
        for(Pin p: sc.getGlobalPins()){
            GridObject go = grid.get(p);
            if(go != null && go instanceof ConnectionPoint){
                ((ConnectionPoint) go).resetBackup();
            }
        }
    }
    
    public ConnectionPoint getConnectionPoint(Point p){
        GridObject go = grid.get(p);
        if(go != null && go instanceof ConnectionPoint){
            return (ConnectionPoint) go;
        } else {
            return null;
        }
    }

    public void movePin(Pin oldPoint, int dx, int dy) {
        if(dx != 0 && dy != 0){
            Point newPoint =  new Point(oldPoint.x + dx, oldPoint.y + dy);
            GridObject oldGo = grid.get(oldPoint);
            GridObject newGo = grid.get(newPoint);

            // Is the new point uninitialised?
            if(newGo == null){            
                // Update other pins on same connection 
                if(oldGo instanceof ConnectionPoint){
                    // Get rid of old mapping
                    grid.remove(oldPoint);

                    // Move pins and add new mapping
                    ConnectionPoint cp = (ConnectionPoint) oldGo;
                    if(cp.hasBackup()){
                        cp = cp.getBackup(true);
                    }
                    for(Pin pin: cp.getConnections()){
                        pin.setLocation(newPoint);
                    }     
                    cp.setLocation(newPoint);                
                    grid.put(newPoint, cp); 
                } 
            // Another pin belonging to the same component is at the target 
            // destination. We have yet to move this pin so make a backup for later.
            } else if(newGo instanceof ConnectionPoint 
                    && newGo.hasParent(oldPoint.getParent())){          
                // Update other pins on same connection 
                if(oldGo instanceof ConnectionPoint){
                    // Get rid of old mapping
                    grid.remove(oldPoint);

                    // Move pins and add new mapping
                    ConnectionPoint newCp = (ConnectionPoint) oldGo;
                    ConnectionPoint oldCp = (ConnectionPoint) oldGo;
                    newCp.makeBackup();
                    if(oldCp.hasBackup()){
                        oldCp = oldCp.getBackup(true);
                    }
                    for(Pin pin: oldCp.getConnections()){
                        pin.setLocation(newPoint);
                        newCp.addConnection(pin);
                    }    
                    newCp.setLocation(newPoint); 
                    grid.put(newPoint, newCp); 
                } 
            } else {
                ui.error.ErrorHandler.newError("Component Translation Error",
                        "An unknown error occured whilst trying to move the component.");
            }
        }
    }

    public void markInvalidAreas(SelectableComponent sc){
        Rectangle bb = sc.getInvalidArea();
        for(int i = bb.x; i <= bb.x + bb.width; i+=UIConstants.GRID_DOT_SPACING){
            for(int j = bb.y; j <= bb.y + bb.height; j+=UIConstants.GRID_DOT_SPACING){
                Point p = snapPointToGrid(new Point(i, j));
                if(bb.contains(p)){
                    grid.put(p, new InvalidPoint(p, sc));
                }
            }
        }  
    }
    
    public void unmarkInvalidAreas(SelectableComponent sc) {    
        Rectangle bb = sc.getInvalidArea();
        for(int i = bb.x; i <= bb.x + bb.width; i+=UIConstants.GRID_DOT_SPACING){
            for(int j = bb.y; j <= bb.y + bb.height; j+=UIConstants.GRID_DOT_SPACING){
                Point p = snapPointToGrid(new Point(i, j));
                if(bb.contains(p)
                        && grid.get(p) instanceof InvalidPoint 
                        && ((InvalidPoint) grid.get(p)).getParent().equals(sc)){
                    grid.remove(p);
                }
            }
        }
    }
    
    public void removeComponent(SelectableComponent sc){
        for(Pin p: sc.getGlobalPins()){ 
            if(grid.get(p) instanceof ConnectionPoint){
                ((ConnectionPoint) grid.get(p)).removeConnection(p);
                if(!((ConnectionPoint)grid.get(p)).isConnected()){
                    grid.remove(p);
                }
            } 
        }        
        unmarkInvalidAreas(sc); 
    }
    
    public boolean addPin(Pin p){
        GridObject go = grid.get(p);
        if(go==null){
            grid.put(p.getLocation(), new ConnectionPoint(p));
            go = grid.get(p);
        }
        
        if(go instanceof ConnectionPoint){       
            ((ConnectionPoint) go).addConnection(p);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean canMoveComponent(SelectableComponent sc, int dx, int dy, boolean fresh){
        LinkedList<Point> tempPins = new LinkedList<Point>();
        Point temp;
        
        // Check each pin
        for(Pin p: sc.getGlobalPins()){
            temp = new Point(p.x + dx, p.y + dy);
            tempPins.add(temp);
        }       
                
        for(Point p: tempPins){
            if(grid.get(p) instanceof InvalidPoint
                    && !((InvalidPoint)grid.get(p)).getParent().equals(sc)){
                    return false;
            } 
        }
        
        // Check each internal point of the component
        Rectangle bb = sc.getInvalidArea();        
        for(int i = bb.x; i <= bb.x + bb.width; i+=UIConstants.GRID_DOT_SPACING){
            for(int j = bb.y; j <= bb.y + bb.height; j+=UIConstants.GRID_DOT_SPACING){
                GridObject go = grid.get(new Point(i+dx, j+dy));
                if(!fresh && go != null){
                    return false;                   
                } else if(go instanceof InvalidPoint
                        && !((InvalidPoint)go).getParent().equals(sc)) {
                    return false;
                }
            }
        }        
        return true;
    }
    
    public boolean canMoveComponent(SelectableComponent sc, Point d, boolean fresh){
        return canMoveComponent(sc, d.x, d.y, fresh);
    }
    
    public void removePin(Pin p){
        GridObject go = grid.get(p);

        if(go instanceof ConnectionPoint){
            ((ConnectionPoint) go).removeConnection(p);
            if(!((ConnectionPoint) go).isConnected()){
                grid.remove(p);
            }
        } 
    }
    
    public boolean isConnectionPoint(Point p){
        return grid.get(p) instanceof ConnectionPoint;
    }
    
    public void clear(){
        grid.clear();
    }

    public void setActivePoint(Point p, Boolean active) {
        if(isConnectionPoint(p)){
            ((ConnectionPoint) grid.get(p)).setActive(active);
        } 
    }
    
    public boolean isActivePoint(Point p) {
        if(isConnectionPoint(p)){
            return ((ConnectionPoint) grid.get(p)).isActive();
        }
        return false;
    }
    
    public void draw(Graphics2D g2){
       g2.setColor(UIConstants.CONNECTION_POINT_COLOUR);
        
       for(GridObject p: grid.values()){           
           p.draw(g2);
       }               
    }
    
    public Point snapPointToGrid(Point old){
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

    public boolean areDirectlyConnected(SelectableComponent a, SelectableComponent b){        
        Collection<Pin> bPins = b.getGlobalPins();        
        for(Pin pinA: a.getGlobalPins()){
            if(bPins.contains(pinA)){
                return true;
            }
        }        
        return false;        
    }                
}
