package ui.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import ui.UIConstants;
import ui.tools.SelectableComponent;

/**
 *
 * @author matt
 */
public class Grid {
    
    private  HashMap<Point,GridObject> grid = new HashMap<Point,GridObject>();

//     {
//        // Add invalid boundary on topmost edge        
//        for(int i = 0; i<UIConstants.GRID_STANDARD_WIDTH; i++){
//            Point p = new Point(i * UIConstants.GRID_DOT_SPACING, 1);
//            grid.put(p, new InvalidPoint(p, null));    
//        }
//
//        // Add invalid boundary on leftmost edge        
//        for(int j = 0; j<UIConstants.GRID_STANDARD_HEIGHT; j++){
//            Point p = new Point(1, j * UIConstants.GRID_DOT_SPACING);
//            grid.put(p, new InvalidPoint(p, null));    
//        }
//    }
    
    
    public  ConnectionPoint getConnectionPoint(Point p){
        GridObject go = getGridObjectAt(p);
        if(go != null && go instanceof ConnectionPoint){
            return (ConnectionPoint) go;
        } else {
            return null;
        }
    }
    
    public  boolean markInvalidAreas(SelectableComponent sc){
        if(!(sc instanceof netlist.standard.Wire)){
            Rectangle bb = sc.getInvalidAreas();
            for(int i = bb.x; i <= bb.x + bb.width; i+=UIConstants.GRID_DOT_SPACING){
                for(int j = bb.y; j <= bb.y + bb.height; j+=UIConstants.GRID_DOT_SPACING){
                    Point p = snapPointToGrid(new Point(i, j));
                    if(bb.contains(p)){
                        grid.put(new Point(p.x, p.y), new InvalidPoint(p, sc));
                    }
                }
            }
        }
        return true;
    }

    public  boolean translateComponent(int dx, int dy, SelectableComponent sc, boolean newComponent) {    
        if(canMoveComponent(sc, dx, dy, newComponent)){
            if((sc.isFixed() || !newComponent) && !(sc instanceof netlist.standard.Wire)){
                Rectangle bb = sc.getInvalidAreas();
                // Remove all invalid point markers
                for(int i = bb.x; i <= bb.x + bb.width; i+=UIConstants.GRID_DOT_SPACING){
                    for(int j = bb.y; j <= bb.y + bb.height; j+=UIConstants.GRID_DOT_SPACING){
                        Point p = snapPointToGrid(new Point(i, j));
                        if(bb.contains(p)
                                && getGridObjectAt(p) instanceof InvalidPoint 
                                && ((InvalidPoint) getGridObjectAt(p)).getParent().equals(sc)){
                            grid.remove(new Point(p.x, p.y));
                        }
                    }
                }
                // Move all pins and move joined wires
                for(Pin p: sc.getGlobalPins()){ 
                   GridObject oldPin = getGridObjectAt(new Pin(p.getParent(), p.x-dx, p.y-dy));
                    if(oldPin instanceof ConnectionPoint){
                        ConnectionPoint cp = (ConnectionPoint) oldPin;
                        Collection<Pin> cpBackup = cp.getConnections();
                        cp.moveWireEnds(new Point(p.x, p.y));          
                        if(!cp.getConnections().equals(cpBackup)){
                            //System.out.println("change");
                        } else {
                            //System.out.println("nochange");
                        }
                    } 
                }   
            }
            return true;
        }    
        return false;    

    }
    
    public  void removeComponent(SelectableComponent sc){
            for(Pin p: sc.getGlobalPins()){ 
                if(getGridObjectAt(p) instanceof ConnectionPoint){
                    ((ConnectionPoint) getGridObjectAt(p)).removeConnection(p);
                    if(!((ConnectionPoint)getGridObjectAt(p)).isConnected()){
                        grid.remove(new Point(p.x, p.y));
                    }
                } 
            }        
            if(!(sc instanceof netlist.standard.Wire)){
                Rectangle bb = sc.getInvalidAreas();
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

    }
    
    public  boolean addPin(Pin p){
        GridObject go = getGridObjectAt(p);
        if(go==null){
            grid.put(new Point(p.x, p.y), new ConnectionPoint(new Point(p.x, p.y)));
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
        return canMoveComponent(sc, new Point(dx, dy), newComponent);
    }
    
    public  boolean canMoveComponent(SelectableComponent sc, Point d, boolean newComponent){
        LinkedList<Point> tempPins = new LinkedList<Point>();
        Point temp;
        
        // Check each pin
        for(Pin p: sc.getGlobalPins()){
            temp = new Point(p.x + d.x, p.y + d.y);
            tempPins.add(temp);
            
//            // Check leftmost/rightmost border
//            if(temp.x == -10 || temp.y == -10){
//                return false;
//            }
        }       
                
        for(Point p: tempPins){
            if(getGridObjectAt(p) instanceof InvalidPoint
                    && !((InvalidPoint)getGridObjectAt(p)).getParent().equals(sc)){
                    return false;
            } 
        }
        
        // Check each internal point of the component
        Rectangle bb = sc.getInvalidAreas();
//        
//            // Check leftmost/rightmost border
//            if(bb.x == 0 || bb.y == 0){
//                return false;
//            }
        
        for(int i = bb.x; i <= bb.x + bb.width; i+=UIConstants.GRID_DOT_SPACING){
            for(int j = bb.y; j <= bb.y + bb.height; j+=UIConstants.GRID_DOT_SPACING){
                GridObject go = getGridObjectAt(new Point(i+d.x, j+d.y));
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
    
    protected  GridObject getGridObjectAt(Point p){
        // Must create a new object for lookup
        return grid.get(new Point(p.x, p.y));
    }

    public  void removePin(Pin p){
        GridObject go = getGridObjectAt(p);

        if(go instanceof ConnectionPoint){
            ((ConnectionPoint) go).removeConnection(p);
            if(!((ConnectionPoint) go).isConnected()){
                grid.remove(new Point(p.x, p.y));
            }
        } 
    }
    
    public  boolean isConnectionPoint(Point p){
        return getGridObjectAt(p) instanceof ConnectionPoint;
    }
    
    public  void clear(){
        grid.clear();
    }

    public  void setActivePoint(Point p, Boolean active) {
        if(isConnectionPoint(p)){
            ((ConnectionPoint) getGridObjectAt(p)).setActive(active);
        } 
    }
    
    public  boolean getIsActivePoint(Point p) {
        if(isConnectionPoint(p)){
            return ((ConnectionPoint) getGridObjectAt(p)).isActive();
        }
        return false;
    }
    
    public  void draw(Graphics2D g2){
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
