package ui.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import sim.Component;

/**
 *
 * @author matt
 */
public abstract class ImageSelectableComponent extends SelectableComponent {
    
    public ImageSelectableComponent(Point point){
        super(point);
        
        rotation = Math.PI * 0 / 2;
        
        setDefaultImage();
        setSelectedImage();
        setActiveImage();
        
        setInvalidAreas();
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
    
    public BufferedImage getDefaultImage(){
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
    public void draw(Graphics2D g, javax.swing.JComponent parent) {
        super.draw(g); // Draw labels
        
        g.rotate(rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
        g.drawImage(getCurrentImage(), (int)getOrigin().getX(), (int)getOrigin().getY(), parent);
        g.rotate(-rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
    }
            
    @Override
    public boolean containsPoint(Point point) {
        return this.getInvalidAreas().contains(point);
    }
    
    public void createXML(TransformerHandler hd) {
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "", "type", "CDATA", this.getClass().getSimpleName());
            atts.addAttribute("", "", "x", "CDATA", String.valueOf(getOrigin().x));
            atts.addAttribute("", "", "y", "CDATA", String.valueOf(getOrigin().y));
            atts.addAttribute("", "", "rotation", "CDATA", String.valueOf(rotation));
            
            hd.startElement("", "", "component", atts);
            hd.endElement("", "", "component");
        } catch (SAXException ex) {
            Logger.getLogger(ImageSelectableComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public SelectableComponent copy() {        
       SelectableComponent copy = null;
        try {
            copy = getClass().getConstructor(Point.class).newInstance((Point) getOrigin().clone());
        } catch (InstantiationException ex) {
            Logger.getLogger(ImageSelectableComponent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ImageSelectableComponent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ImageSelectableComponent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ImageSelectableComponent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(ImageSelectableComponent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ImageSelectableComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
           
       copy.setSelectionState(selectionState);
       copy.setLabel(getLabel());
       copy.setRotation(rotation);
       
       return copy;
    }
    
}
