/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.tools;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import sim.Component;

/**
 *
 * @author matt
 */
public abstract class ImageSelectableComponent extends SelectableComponent {

    public ImageSelectableComponent(Component component, Point point){
        super(component,point);
        
        setDefaultImage();
        setSelectedImage();
        setActiveImage();
        
        setBoundingBox();
    }    
    
    public int getWidth(){
        return getDefaultImage().getWidth();
    };
    
    public int getHeight(){
        return getDefaultImage().getHeight();
    }

    protected BufferedImage getActiveImage(){
        return activeBi;
    }

    protected BufferedImage getSelectedImage(){
        return selectedBi;
    }
    
    protected BufferedImage getDefaultImage(){
        return defaultBi;   
    }
    
    protected BufferedImage getCurrentImage(){
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
    
    @Override
    public void mouseDragged(MouseEvent arg0) {
        setSelectionType(SelectionType.ACTIVE);
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        if(!getSelectionType().equals(SelectionType.ACTIVE)){
             setSelectionType(SelectionType.SELECTED);
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        if(!getSelectionType().equals(SelectionType.ACTIVE)){
             setSelectionType(SelectionType.SELECTED);
        }
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        if(!getSelectionType().equals(SelectionType.SELECTED)){
             setSelectionType(SelectionType.DEFAULT);
        }
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        if(getSelectionType().equals(SelectionType.ACTIVE)){
             setSelectionType(SelectionType.SELECTED);
        } else {
             setSelectionType(SelectionType.ACTIVE);
        } 
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
           
    }

    @Override
    public void draw(Graphics g, javax.swing.JComponent parent) {
        g.drawImage(getCurrentImage(), (int)getOrigin().getX(), (int)getOrigin().getY(), parent);
    }
    
    @Override
    public boolean containsPoint(Point point) {
        return this.getBoundingBox().contains(point);
    }
}
