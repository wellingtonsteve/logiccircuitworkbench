/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.LinkedList;
import ui.UIConstants;
import ui.tools.Wire;

/**
 *
 * @author Matt
 */
public class ConnectionPoint extends GridObject {
    private LinkedList<Pin> connections = new LinkedList<Pin>();
    private boolean isActive = false;

    public ConnectionPoint(Point p){
        super(p);
    }
    
    public void addConnection(Pin p){
        connections.add(p);
    }
    
    public boolean removeConnection(Pin p){
        return connections.remove(p);
    }
     
    public boolean hasConnection(Pin p){
        return connections.contains(p);
    }
    
    public boolean isConnected(){
        return !connections.isEmpty();
    }
    
    public boolean isWire(){
        for(Pin p: connections){
            if(p.getParent() instanceof Wire){
                return true;
            }
        }
        return false;
    }
    
    public void setActive(boolean isActive){
        this.isActive = isActive;
    }
    
    public boolean isActive(){
        return isActive;
    }

    @Override
    public void draw(Graphics2D g2) {
        if(isActive){
            Stroke def = g2.getStroke();
            g2.setStroke(UIConstants.CONNECTED_POINT_STROKE);
            g2.drawRect(x-3, y-3, 7, 7); 
            g2.setStroke(def);
            isActive = false;
        }
        
        if(UIConstants.SHOW_CONNECTION_POINTS){
                g2.drawOval(x-1, y-1, 3, 3);
                g2.fillOval(x-1, y-1, 3, 3);
        }
    }
    
}
