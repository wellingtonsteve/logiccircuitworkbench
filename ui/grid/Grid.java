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
    
    public ConnectionPoint getConnectionPoint(Point p){
        GridObject go = grid.get(p);
        if(go != null && go instanceof ConnectionPoint){
            return (ConnectionPoint) go;
        } else {
            return null;
        }
    }
    
    public boolean canMoveComponent(SelectableComponent sc, int dx, int dy){
        LinkedList<Point> tempPins = new LinkedList<Point>();
        Point temp;

        // Check each pin
        for(Pin p: sc.getGlobalPins()){
            temp = new Point(p.x + dx, p.y + dy);
            GridObject go = grid.get(temp);  
            if(go != null && !go.hasParent(sc)){//System.out.println("here");
                return false; 
            }           
        }
        
        // Check each internal point of the component
        Rectangle bb = sc.getInvalidArea();    

        for(int i = bb.x; i <= bb.x + bb.width; i+=UIConstants.GRID_DOT_SPACING){
            for(int j = bb.y; j <= bb.y + bb.height; j+=UIConstants.GRID_DOT_SPACING){
                GridObject go = grid.get(snapPointToGrid(new Point(i+dx, j+dy)));
                if(go != null) {
                    return false;
                }
            }
        }        
        return true;
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

    public void movePin(Pin oldPoint, int dx, int dy) {
        if(dx != 0 || dy != 0){
            Point newPoint =  new Point(oldPoint.x + dx, oldPoint.y + dy);
            GridObject oldGo = grid.get(oldPoint);
            GridObject newGo = grid.get(newPoint);

            // Is the new point uninitialised?
            if(newGo == null && oldGo instanceof ConnectionPoint){

                // Intialise connection point
                grid.put(newPoint.getLocation(), new ConnectionPoint(newPoint));
                newGo = grid.get(newPoint);
                
                // Move pins
                ConnectionPoint newCp = (ConnectionPoint) newGo;
                ConnectionPoint oldCp = (ConnectionPoint) oldGo;
                if(oldCp.hasBackup()){
                    oldCp = oldCp.getBackup(true);
                }
                for(Pin pin: oldCp.getConnections()){
                    if(pin.equals(oldPoint)){
                        pin.setLocation(newPoint);
                        oldCp.removeConnection(oldPoint);
                        newCp.addConnection(pin);
                    }
                }    
                                 
                // Get rid of old mapping
                if(!((ConnectionPoint) oldGo).isConnected()){
                    grid.remove(oldGo);
                }
                
                // Add new mapping               
                grid.put(newPoint, newCp); 
                
            // Another pin belonging to the same component is at the target 
            // destination. We have yet to move this pin so make a backup for later.
            } else if(newGo instanceof ConnectionPoint                             
                    && oldGo instanceof ConnectionPoint
                    && newGo.hasParent(oldPoint.getParent())){           
                
                // Move pins 
                ConnectionPoint newCp = (ConnectionPoint) newGo;
                ConnectionPoint oldCp = (ConnectionPoint) oldGo;
                newCp.makeBackup();
                if(oldCp.hasBackup()){
                    oldCp = oldCp.getBackup(true);
                }
                for(Pin pin: oldCp.getConnections()){
                    if(pin.equals(oldPoint)){
                        pin.setLocation(newPoint);
                        oldCp.removeConnection(oldPoint);
                        newCp.addConnection(pin);
                    }
                }    
                newCp.setLocation(newPoint); 
                
                // Get rid of old mapping
                if(!((ConnectionPoint) oldGo).isConnected()){
                    grid.remove(oldGo);
                }
                
                // Add new mapping
                grid.put(newPoint, newCp);                 
            } 
        }
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
    
    public void removePin(Pin p){
        GridObject go = grid.get(p);
        if(go instanceof ConnectionPoint){
            ((ConnectionPoint) go).removeConnection(p);
            if(!((ConnectionPoint) go).isConnected()){
                grid.remove(p);
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
    
    public boolean isConnectionPoint(Point p){
        return grid.get(p) instanceof ConnectionPoint;
    }
    
    public boolean isActivePoint(Point p) {
        if(isConnectionPoint(p)){
            return ((ConnectionPoint) grid.get(p)).isActive();
        }
        return false;
    }
    
    public void setActivePoint(Point p, Boolean active) {
        if(isConnectionPoint(p)){
            ((ConnectionPoint) grid.get(p)).setActive(active);
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
    
    public void draw(Graphics2D g2){
       g2.setColor(UIConstants.CONNECTION_POINT_COLOUR);
        
       for(GridObject p: grid.values()){           
           p.draw(g2);
       }               
    }
    
    
    public void clearBackups(SelectableComponent sc) {
        for(Pin p: sc.getGlobalPins()){
            GridObject go = grid.get(p);
            if(go != null && go instanceof ConnectionPoint){
                ((ConnectionPoint) go).resetBackup();
            }
        }
    }
    
    public void clear(){
        grid.clear();
    }

}
