package ui.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.LinkedList;
import ui.UIConstants;
import ui.tools.Wire;
import ui.tools.SelectableComponent;

/**
 *
 * @author matt
 */
public class Grid {
    
    private static HashMap<Point,GridObject> grid = new HashMap<Point,GridObject>(); 
    
    public static ConnectionPoint getConnectionPoint(Point p){
        GridObject go = getGridObjectAt(p);
        if(go != null && go instanceof ConnectionPoint){
            return (ConnectionPoint) go;
        } else {
            return null;
        }
    }
    
    public static boolean markInvalidAreas(SelectableComponent sc){
//        for(Pin p: sc.getGlobalPins()){ 
//            if(!addPin(p)){ return false; }
//        }        
        if(!(sc instanceof ui.tools.Wire)){
            Rectangle bb = sc.getBoundingBox();
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

    public static void translateComponent(int dx, int dy, SelectableComponent sc) {
        
        //if(sc.isFixed()){
            
            if(!(sc instanceof ui.tools.Wire)){
                Rectangle bb = sc.getBoundingBox();
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
            
            for(Pin p: sc.getGlobalPins()){ 
                Pin oldPin = new Pin(p.getParent(), p.x-dx, p.y-dy);
                if(getGridObjectAt(oldPin) instanceof ConnectionPoint){
                    if(((ConnectionPoint) getGridObjectAt(oldPin)).isActive()){
                        System.out.println("yes");
                    }
                    ((ConnectionPoint) getGridObjectAt(oldPin)).moveWireEnds(p);                

                } 
            }   
            
            //refreshGridObjects(); Crossovers, Joins etc..
        //}

    }
    
    public static void removeComponent(SelectableComponent sc){
        if(sc.isFixed()){
            for(Pin p: sc.getGlobalPins()){ 
                if(getGridObjectAt(p) instanceof ConnectionPoint){
                    //((ConnectionPoint) getGridObjectAt(p)).moveWireEnds(p);
                    ((ConnectionPoint) getGridObjectAt(p)).removeConnection(p);
                    if(!((ConnectionPoint)getGridObjectAt(p)).isConnected()){
                        grid.remove(p);
                    }
                } else if (getGridObjectAt(p) instanceof WireCrossover){
                    grid.put(p, ((WireCrossover) getGridObjectAt(p)).previousState());            
                }
            }        
            if(!(sc instanceof ui.tools.Wire)){
                Rectangle bb = sc.getBoundingBox();
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

    }
    
    public static boolean addPin(Pin p){
        GridObject go = getGridObjectAt(p);
        if(go==null){
            grid.put(new Point(p.x, p.y), new ConnectionPoint(new Point(p.x, p.y)));
            go = getGridObjectAt(p);
        }
        
        if(go instanceof ConnectionPoint){
//            GridObject north = grid.get(new Point(p.x, p.y-UIConstants.GRID_DOT_SPACING));
//            GridObject south = grid.get(new Point(p.x, p.y+UIConstants.GRID_DOT_SPACING));
//            GridObject west = grid.get(new Point(p.x-UIConstants.GRID_DOT_SPACING, p.y));
//            GridObject east = grid.get(new Point(p.x+UIConstants.GRID_DOT_SPACING, p.y));           
            
            // Is a join point needed?6
            if(p.getParent() instanceof Wire                                // The current pin belongs to a wire
                    && ((ConnectionPoint) go).isNotSameWire(p.getParent())  // Not the same wire
                    && (((Wire) p.getParent()).getEndPoint().equals(p)      // Is the end point of a wire
                       || ((Wire) p.getParent()).getOrigin().equals(p))){   // Is the start point of a wire
                grid.put(p, new WireJoin(p));
            // Is a crossover point needed?
            } else if(p.getParent() instanceof Wire                         // The current pin belongs to a wire
                    && ((ConnectionPoint) go).isNotSameWire(p.getParent())  // Not the same wire               
                    && !((Wire) p.getParent()).getEndPoint().equals(p)      // Not the end point of a wire
                    && !((Wire) p.getParent()).getOrigin().equals(p)){      // Not the start point of a wire

                grid.put(p, new WireCrossover(p, p.getParent(),go));
            } else {
                ((ConnectionPoint) go).addConnection(p);
            }
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean canMoveComponent(SelectableComponent sc, Point d){
        LinkedList<Point> tempPins = new LinkedList<Point>();
        Point temp;
        
        for(Pin p: sc.getGlobalPins()){
            removePin(p);
            
            temp = new Point(p.x + d.x, p.y + d.y);
            tempPins.add(temp);
        }       
                
        for(Point p: tempPins){
            if(getGridObjectAt(p) instanceof InvalidPoint){
                if(UIConstants.DO_SYSTEM_BEEP){UIConstants.beep();}
                return false;
            }           
        }
        
        Rectangle bb = sc.getBoundingBox();
        for(int i = bb.x; i <= bb.x + bb.width; i+=UIConstants.GRID_DOT_SPACING){
            for(int j = bb.y; j <= bb.y + bb.height; j+=UIConstants.GRID_DOT_SPACING){
                Point p = snapPointToGrid(new Point(i, j));
                if(bb.contains(p) && getGridObjectAt(p) instanceof InvalidPoint){
                    if(UIConstants.DO_SYSTEM_BEEP){UIConstants.beep();}
                    return false;
                }
            }
        }
        
        return true;
    }
    
    protected static GridObject getGridObjectAt(Point p){
        return grid.get(new Point(p.x, p.y));
    }

    public static void removePin(Pin p){
        GridObject go = getGridObjectAt(p);
                
        if(go instanceof ConnectionPoint){
            ((ConnectionPoint) go).removeConnection(p);
            if(!((ConnectionPoint) go).isConnected()){
                grid.remove(new Point(p.x, p.y));
            }
        } 
    }
    
    public static boolean isConnectionPoint(Point p){
        return getGridObjectAt(p) instanceof ConnectionPoint;
    }
    
    public static void clear(){
        grid.clear();
    }

    public static void setActivePoint(Point p, Boolean active) {
        if(isConnectionPoint(p)){
            ((ConnectionPoint) getGridObjectAt(p)).setActive(active);
        } 
    }
    
    public static boolean getIsActivePoint(Point p) {
        if(isConnectionPoint(p)){
            return ((ConnectionPoint) getGridObjectAt(p)).isActive();
        }
        return false;
    }
    
    public static void draw(Graphics2D g2){
       g2.setColor(UIConstants.CONNECTION_POINT_COLOUR);
        
       for(GridObject p: grid.values()){           
           p.draw(g2);
       }               

    }
    
    public static Point snapPointToGrid(Point old){
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

            
}
