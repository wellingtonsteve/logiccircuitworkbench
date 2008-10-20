/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import ui.tools.SelectableComponent;

/**
 *
 * @author Matt
 */
public class ConnectionPoint {
    private SelectableComponent component;
    private int x;
    private int y;
    private ConnectionPointType type;
    private List<ConnectionPoint> connections = new LinkedList<ConnectionPoint>();
    private boolean isActive = false;

    public ConnectionPoint(SelectableComponent component, int x, int y){
        this.component = component;
        this.x = x;
        this.y = y;
    }
    
    public Point getLocation(){
        return new Point(component.getOrigin().x - component.getCentre().x + x,
                component.getOrigin().y - component.getCentre().y + y);
    }
    
    public SelectableComponent getParent(){
        return component;
    }
    
    public ConnectionPointType getType(){
        return type;
    }
    
    public void addConnection(ConnectionPoint cp){
        connections.add(cp);
    }
    
    public boolean removeConnection(ConnectionPoint cp){
        return connections.remove(cp);
    }
    
    public boolean isConnected(){
        return !connections.isEmpty();
    }
    
    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }
    
    public boolean isActive(){
        return isActive;
    }
    
}
