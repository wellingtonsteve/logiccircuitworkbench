package ui.components;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Map;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import ui.CircuitPanel;
import ui.UIConstants;

/**
 *
 * @author matt
 */
public abstract class VisualComponent extends SelectableComponent {
    
    protected BufferedImage defaultBi;
    protected BufferedImage selectedBi;
    protected BufferedImage activeBi;
      
    public VisualComponent(CircuitPanel parent, Point point, sim.SimItem logicalComponent){
        super(parent, point, logicalComponent);
        
        setDefaultImage();
        setSelectedImage();
        setActiveImage();
        
        setInvalidAreas();
        setBoundingBox();       
        
        setSelectionState(SelectionState.DEFAULT);
    }    

    protected void setDefaultImage(){        
        defaultBi = nl.getImage(keyName, "default");
    }
    
    protected void setSelectedImage(){
        selectedBi = nl.getImage(keyName, "selected");
    }
    
    protected void setActiveImage(){
        activeBi = nl.getImage(keyName, "active");
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

    @Override
    public void setLocalPins() {
        localPins = new LinkedList<Pin>();
        Map<String, Point> inpins = nl.getProperties(keyName).getInputPins();
        for(String k: inpins.keySet()){
            localPins.add(new Pin(inpins.get(k), logicalComponent.getPinByName(k)));
        }
        
        Map<String, Point> outpins = nl.getProperties(keyName).getOutputPins();
        for(String k: outpins.keySet()){
            localPins.add(new Pin(outpins.get(k), logicalComponent.getPinByName(k)));
        }        
    }
    
    @Override
    public void draw(Graphics2D g) {
        super.draw(g); // Draw labels
        g.rotate(rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);      
        g.translate(getOrigin().x, getOrigin().y);               
        g.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);
        for(Pin p: localPins){
            if(p.getJoinable() instanceof sim.joinable.InputPin){
                g.drawLine(p.x, p.y, p.x+(2*UIConstants.GRID_DOT_SPACING), p.y);
            } else if(p.getJoinable() instanceof sim.joinable.OutputPin){
                g.drawLine(p.x, p.y, p.x-(2*UIConstants.GRID_DOT_SPACING), p.y);
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
            
            // TODO: add attributes
            
            hd.endElement("", "", "component");
        } catch (SAXException ex) {
            ui.error.ErrorHandler.newError("XML Creation Error","Please refer to the system output below.",ex);
        }
    } 
}
