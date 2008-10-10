/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import sim.Component;

/**
 *
 * @author Matt
 */
public class SelectableComponent {
    
    private Component component;
    private int x;
    private int y;
    private BufferedImage bi;
    private SelectionType selectionType = SelectionType.DEFAULT;
    private Rectangle area;

    public SelectionType getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(SelectionType selectiontype) {
        this.selectionType = selectiontype;
    }

    public BufferedImage getBufferedImage() {
        return bi;
    }

    public Component getComponent() {
        return component;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getWidth(){
        return bi.getWidth();
    }
    
    public int getHeight(){
        return bi.getHeight();
    }
    
    public SelectableComponent(Component component, int x, int y, BufferedImage bi){
        this.component = component;
        this.x = x;
        this.y = y;
        this.bi = bi;
        this.area = new Rectangle(x, y, bi.getWidth(), bi.getHeight());
    }
    
    public Rectangle getArea() {
        return area;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
        this.area = new Rectangle(x, y, bi.getWidth(), bi.getHeight());
    }
    

}
