/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.tools;

import java.awt.Graphics;
import java.awt.Point;
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
        
        setSelectionState(SelectionState.DEFAULT);
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
        switch(getSelectionState()){            
            case ACTIVE:
                return getActiveImage();
            case HOVER:
                return getSelectedImage();            
            default:
                return getDefaultImage();            
        }    
    }

    protected abstract void setDefaultImage();
    protected abstract void setSelectedImage();
    protected abstract void setActiveImage();
    
    @Override
    public void mouseDragged(MouseEvent e) {
        setSelectionState(SelectionState.ACTIVE);
    }

    public void mouseDraggedDropped(MouseEvent e) {
        setSelectionState(selectionState.ACTIVE);
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        if(!isFixed() && !getSelectionState().equals(SelectionState.ACTIVE)){
             setSelectionState(SelectionState.DEFAULT);
        } 
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(isFixed()){
            if(getSelectionState().equals(SelectionState.ACTIVE)){
                 setSelectionState(SelectionState.HOVER);
                 
            } else {
                 setSelectionState(SelectionState.ACTIVE);
            } 
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");  
    }
        
    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setSelectionState(SelectionState.ACTIVE);
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
