/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.grid;

import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Matt
 */
public abstract class GridObject extends Point {
    //ConnectionPoint,
    //WireCrossover,
    //Invalid;
    public GridObject(Point p){
        super(p.x,  p.y);
    }
    
    public abstract void draw(Graphics2D g2);
}
