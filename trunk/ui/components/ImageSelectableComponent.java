package ui.components;

import java.awt.Graphics2D;
import java.awt.Point;
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
      
    public ImageSelectableComponent(CircuitPanel parent, Point point, sim.SimItem logicalComponent){
        super(parent, point, logicalComponent);
        
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
    public void draw(Graphics2D g) {
        super.draw(g); // Draw labels
        g.rotate(rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);      
        g.translate(getOrigin().x, getOrigin().y);               
        g.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);
        for(Pin p: localPins){
            if(p.getJoinable() instanceof sim.pin.InputPin){
                g.drawLine(p.x, p.y, p.x+UIConstants.GRID_DOT_SPACING, p.y);
            } else if(p.getJoinable() instanceof sim.pin.OutputPin){
                g.drawLine(p.x, p.y, p.x-UIConstants.GRID_DOT_SPACING, p.y);
            }
        }        
        g.translate(-getOrigin().x, -getOrigin().y);
        g.drawImage(getCurrentImage(), getOrigin().x, getOrigin().y, null);
        g.rotate(-rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
    }
            
    @Override
    public boolean containsPoint(Point point) {
        return getInvalidArea().contains(point);
    }
    
    public void createXML(TransformerHandler hd) {
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "", "type", "CDATA", getComponentTreeName());
            atts.addAttribute("", "", "x", "CDATA", String.valueOf(getOrigin().x));
            atts.addAttribute("", "", "y", "CDATA", String.valueOf(getOrigin().y));
            atts.addAttribute("", "", "rotation", "CDATA", String.valueOf(rotation));
            atts.addAttribute("", "", "label", "CDATA", getLabel());
            
            hd.startElement("", "", "component", atts);
            hd.endElement("", "", "component");
        } catch (SAXException ex) {
            ui.error.ErrorHandler.newError("XML Creation Error","Please refer to the system output below.",ex);
        }
    }
       
    protected abstract void setNetlist();
 
}
