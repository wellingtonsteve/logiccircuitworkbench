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
        GridObject go = getGridObjectAt(p);
        if(go != null && go instanceof ConnectionPoint){
            return (ConnectionPoint) go;
        } else {
            return null;
        }
    }
    
    public boolean markInvalidAreas(SelectableComponent sc){
        if(!(sc instanceof ui.components.standard.Wire)){
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
        return true;
    }

    public boolean movePin(Pin oldPoint, int dx, int dy) {
        Point newPoint =  new Point(oldPoint.x + dx, oldPoint.y + dy);
        GridObject oldGo = getGridObjectAt(oldPoint);
        GridObject newGo = getGridObjectAt(newPoint);
        // Is the new point valid
        if(newGo == null){
            // Update joined wires
            if(oldGo instanceof ConnectionPoint){
                ConnectionPoint cp = (ConnectionPoint) oldGo;
//                Collection<Pin> cpBackup = cp.getConnections();
                cp.moveWireEnds(newPoint);          
//                System.out.println("test");
//                if(!cp.getConnections().equals(cpBackup)){
//                    //System.out.println("change");
//                } else {
//                    //System.out.println("nochange");
//                }
            } else {
                return false;
            }
            
            grid.remove(oldPoint);
            oldGo.setLocation(newPoint);
            grid.put(newPoint, oldGo);                       
            
            return true;
        }        
        return false;
    }

    private void removeInvalidAreas(SelectableComponent sc){
        Rectangle bb = sc.getInvalidArea();
        for(int i = bb.x; i <= bb.x + bb.width; i+=UIConstants.GRID_DOT_SPACING){
            for(int j = bb.y; j <= bb.y + bb.height; j+=UIConstants.GRID_DOT_SPACING){
                Point p = snapPointToGrid(new Point(i, j));
                if(bb.contains(p)
                        && getGridObjectAt(p) instanceof InvalidPoint 
                        && ((InvalidPoint) getGridObjectAt(p)).getParent().equals(sc)){
                    grid.remove(p);
                }
            }
        }
    }
    
    public boolean translateComponent(int dx, int dy, SelectableComponent sc, boolean newComponent) {    
        if(canMoveComponent(sc, dx, dy, newComponent)){
            if((sc.isFixed() || !newComponent) && !(sc instanceof ui.components.standard.Wire)){
                removeInvalidAreas(sc);
            }
            return true;
        }    
        return false;    
    }
    
    public void removeComponent(SelectableComponent sc){
            for(Pin p: sc.getGlobalPins()){ 
                if(getGridObjectAt(p) instanceof ConnectionPoint){
                    ((ConnectionPoint) getGridObjectAt(p)).removeConnection(p);
                    if(!((ConnectionPoint)getGridObjectAt(p)).isConnected()){
                        grid.remove(p);
                    }
                } 
            }        
            if(!(sc instanceof ui.components.standard.Wire)){
                removeInvalidAreas(sc);
            } 

    }
    
    public  boolean addPin(Pin p){
        GridObject go = getGridObjectAt(p);
        if(go==null){
            grid.put(p, new ConnectionPoint(p));
            go = getGridObjectAt(p);
        }
        
        if(go instanceof ConnectionPoint){       
            ((ConnectionPoint) go).addConnection(p);
            return true;
        } else {
            return false;
        }
    }
    
    public  boolean canMoveComponent(SelectableComponent sc, int dx, int dy, boolean newComponent){
        LinkedList<Point> tempPins = new LinkedList<Point>();
        Point temp;
        
        // Check each pin
        for(Pin p: sc.getGlobalPins()){
            temp = new Point(p.x + dx, p.y + dy);
            tempPins.add(temp);
        }       
                
        for(Point p: tempPins){
            if(getGridObjectAt(p) instanceof InvalidPoint
                    && !((InvalidPoint)getGridObjectAt(p)).getParent().equals(sc)){
                    return false;
            } 
        }
        
        // Check each internal point of the component
        Rectangle bb = sc.getInvalidArea();        
        for(int i = bb.x; i <= bb.x + bb.width; i+=UIConstants.GRID_DOT_SPACING){
            for(int j = bb.y; j <= bb.y + bb.height; j+=UIConstants.GRID_DOT_SPACING){
                GridObject go = getGridObjectAt(new Point(i+dx, j+dy));
                if(newComponent && go != null){
                    return false;                   
                } else if(go instanceof InvalidPoint
                        && !((InvalidPoint)go).getParent().equals(sc)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public  boolean canMoveComponent(SelectableComponent sc, Point d, boolean newComponent){
        return canMoveComponent(sc, d.x, d.y, newComponent);
    }
    
    protected GridObject getGridObjectAt(Point p){
        // Must create a new object for lookup
        return grid.get(p);
    }

    public void removePin(Pin p){
        GridObject go = getGridObjectAt(p);

        if(go instanceof ConnectionPoint){
            ((ConnectionPoint) go).removeConnection(p);
            if(!((ConnectionPoint) go).isConnected()){
                grid.remove(p);
            }
        } 
    }
    
    public boolean isConnectionPoint(Point p){
        return getGridObjectAt(p) instanceof ConnectionPoint;
    }
    
    public void clear(){
        grid.clear();
    }

    public void setActivePoint(Point p, Boolean active) {
        if(isConnectionPoint(p)){
            ((ConnectionPoint) getGridObjectAt(p)).setActive(active);
        } 
    }
    
    public boolean getIsActivePoint(Point p) {
        if(isConnectionPoint(p)){
            return ((ConnectionPoint) getGridObjectAt(p)).isActive();
        }
        return false;
    }
    
    public void draw(Graphics2D g2){
       g2.setColor(UIConstants.CONNECTION_POINT_COLOUR);
        
       for(GridObject p: grid.values()){           
           p.draw(g2);
       }               

    }
    
    public  Point snapPointToGrid(Point old){
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
