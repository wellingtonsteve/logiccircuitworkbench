/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.tools;

import ui.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import sim.Component;

/**
 *
 * @author Matt
 */
public abstract class SelectableComponent {
    
    private Component component;
    private int x;
    private int y;
    private SelectionType selectionType = SelectionType.DEFAULT;
    protected BufferedImage defaultBi;
    protected BufferedImage selectedBi;
    protected BufferedImage activeBi;
    private Rectangle area = null;

    public SelectableComponent(Component component, int x, int y){
        this.component = component;
        this.x = x;
        this.y = y;
        setDefaultImage();
        setSelectedImage();
        setActiveImage();
        this.area = new Rectangle(x,y,getDefaultImage().getWidth(),getDefaultImage().getHeight());
    }    
    
    public SelectionType getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(SelectionType selectiontype) {
        this.selectionType = selectiontype;
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
        
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
        this.area = new Rectangle(x,y,getDefaultImage().getWidth(),getDefaultImage().getHeight());
    }
    
    public String getName(){
        return component.getType();
    }
    
    public Rectangle getArea(){
        return area;
    }
    
    public int getWidth(){
        return getDefaultImage().getWidth();
    };
    
    public int getHeight(){
        return getDefaultImage().getHeight();
    }

    public BufferedImage getActiveImage(){
        return activeBi;
    }

    public BufferedImage getSelectedImage(){
        return selectedBi;
    }
    
    public BufferedImage getDefaultImage(){
        return defaultBi;   
    }
    
    public BufferedImage getCurrentImage(){
        switch(getSelectionType()){
            case ACTIVE:
                return getActiveImage();
            case SELECTED:
                return getSelectedImage();            
            default:
                return getDefaultImage();            
        }    
    }

    protected abstract void setDefaultImage();
    protected abstract void setSelectedImage();
    protected abstract void setActiveImage();
}
