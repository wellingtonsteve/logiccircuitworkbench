package ui.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import ui.UIConstants;
import ui.components.SelectableComponent;
import ui.components.SelectableComponent.Pin;

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
        
        // Check each pin
        for(Pin local: sc.getPins()){
            Point p = local.getGlobalLocation();
            Point temp = new Point(p.x + dx, p.y + dy);
            GridObject go = grid.get(temp);  
            if(go != null 
                    && go instanceof InvalidPoint // Pins can overlap other pins but not invalid points
                    && !go.hasParent(sc)){
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
                    if(go != null && !go.hasParent(sc)) {
                        return false;
                    }
                }
            }
        }        
        return true;
    }
    
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

//    public void movePin(Pin2 oldPoint, int dx, int dy) {
//        if(dx != 0 || dy != 0){
//            Point newPoint =  new Point(oldPoint.x + dx, oldPoint.y + dy);
//            GridObject oldGo = grid.get(oldPoint);
//            GridObject newGo = grid.get(newPoint);
//
//            // Is the new point uninitialised?
//            if(newGo == null && oldGo instanceof ConnectionPoint){
//               
//                // Move pins
//                ConnectionPoint newCp = new ConnectionPoint(newPoint);
//                ConnectionPoint oldCp = (ConnectionPoint) oldGo;
//                if(oldCp.hasBackup()){
//                    oldCp.removeConnection(oldPoint);
//                    oldCp = oldCp.getBackup(true);
//                }
////                for(Pin pin: oldCp.getConnections()){
////                    if(pin.equals(oldPoint)){
////                        pin.setLocation(newPoint);
////                        oldCp.removeConnection(oldPoint);
////                        newCp.addConnection(pin);
////                        break;
////                    }
////                }    
//                       
//                        oldCp.removeConnection(oldPoint);
//                        newCp.addConnection(oldPoint); 
//                        oldPoint.setLocation(newPoint);
//                // Get rid of old mapping
//                if(!((ConnectionPoint) oldGo).isConnected()){
//                    grid.remove(oldGo);
//                }
//                
//                // Add new mapping               
//                grid.put(newPoint, newCp); 
//                
//            // Another pin belonging to the same component is at the target 
//            // destination. We have yet to move this pin so make a backup for later.
//            } else if(newGo instanceof ConnectionPoint                             
//                    && oldGo instanceof ConnectionPoint
//                    && newGo.hasParent(oldPoint.getParent())){           
//                
//                // Move pins 
//                ConnectionPoint newCp = (ConnectionPoint) newGo;
//                ConnectionPoint oldCp = (ConnectionPoint) oldGo;
//                newCp.makeBackup();
//                if(oldCp.hasBackup()){
//                    oldCp.removeConnection(oldPoint);
//                    oldCp = oldCp.getBackup(true);
//                }
////                for(Pin pin: oldCp.getConnections()){
////                    if(pin.equals(oldPoint)){
////                        pin.setLocation(newPoint);
////                        oldCp.removeConnection(oldPoint);
////                        
////                        newCp.addConnection(pin);
////                        break;
////                    }
////                }    
//                oldCp.removeConnection(oldPoint);
//                newCp.addConnection(oldPoint); 
//                oldPoint.setLocation(newPoint); 
//                
//                // Get rid of old mapping
//                if(!((ConnectionPoint) oldGo).isConnected()){
//                    grid.remove(oldGo);
//                }         
//            } 
//        }
//    }
    
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
    
    public Point snapToGrid(Point old){
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
        Collection<Pin> bPins = b.getPins();        
        for(Pin pinA: a.getPins()){
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
    
//    public void clearBackups(SelectableComponent sc) {
//        for(Pin local: sc.getPins()){
//            Point p = local.getGlobalLocation();
//            GridObject go = grid.get(p);
//            if(go != null && go instanceof ConnectionPoint){
//                ((ConnectionPoint) go).resetBackup();
//            }
//        }
//    }
    
    public void clear(){
        grid.clear();
    }

}
