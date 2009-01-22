package ui.components;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import ui.CircuitPanel;
import netlist.Netlist;
import ui.UIConstants;

/**
 *
 * @author matt
 */
public abstract class ImageSelectableComponent extends SelectableComponent {
    
    protected Netlist nl;
    protected BufferedImage defaultBi;
    protected BufferedImage selectedBi;
    protected BufferedImage activeBi;
      
    public ImageSelectableComponent(CircuitPanel parent, Point point){
        super(parent, point);
        
        setComponentTreeName();
        setNetlist();
        
        setDefaultImage();
        setSelectedImage();
        setActiveImage();
        
        setInvalidAreas();
        setBoundingBox();       
        
        setSelectionState(SelectionState.DEFAULT);
    }    
    
    public int getWidth(){
        return getDefaultImage().getWidth();
    }
    
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

    protected void setDefaultImage(){        
        defaultBi = nl.getImage(getComponentTreeName()+".default");
    }
    
    protected void setSelectedImage(){
        selectedBi = nl.getImage(getComponentTreeName()+".selected");
    }
    
    protected void setActiveImage(){
        activeBi = nl.getImage(getComponentTreeName()+".active");
    }
    
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
    public void draw(Graphics2D g) {
        super.draw(g); // Draw labels
        g.rotate(rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
        g.drawImage(getCurrentImage(), getOrigin().x, getOrigin().y, null);
        g.rotate(-rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
    }
            
    @Override
    public boolean containsPoint(Point point) {
        return getBoundingBox().contains(point);
    }
    
    public void createXML(TransformerHandler hd) {
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "", "type", "CDATA", getComponentTreeName());
            atts.addAttribute("", "", "x", "CDATA", String.valueOf(getOrigin().x));
            atts.addAttribute("", "", "y", "CDATA", String.valueOf(getOrigin().y));
            atts.addAttribute("", "", "rotation", "CDATA", String.valueOf(rotation));
            
            hd.startElement("", "", "component", atts);
            hd.endElement("", "", "component");
        } catch (SAXException ex) {
            ui.error.ErrorHandler.newError("XML Creation Error","Please refer to the system output below.",ex);
        }
    }
        
    protected abstract void setNetlist();
    
}