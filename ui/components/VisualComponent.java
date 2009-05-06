package ui.components;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Map;
import javax.xml.transform.sax.TransformerHandler;
import netlist.properties.PinPosition;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import ui.CircuitPanel;
import ui.UIConstants;
import ui.command.SubcircuitOpenCommand.SubcircuitComponent;
import ui.error.ErrorHandler;

/** @author matt */
public abstract class VisualComponent extends SelectableComponent {    
    protected BufferedImage defaultBi = null;
    protected BufferedImage selectedBi = null;
    protected BufferedImage activeBi = null;
    private int width = 0, height = 0;
      
    public VisualComponent(CircuitPanel parent, Point point, sim.SimItem logicalComponent,netlist.properties.Properties properties){
        super(parent, point, logicalComponent, properties);        
        setDefaultImage(); // Retrieve images from properties
        setSelectedImage();// Retrieve images from properties
        setActiveImage();  // Retrieve images from properties
        setWidthAndHeight();        
        setInvalidAreas();
        setBoundingBox(); 
        setSelectionState(SelectionState.DEFAULT);
    }    

    protected void setDefaultImage(){        
        defaultBi = properties.getImage(getKeyName()+"."+"default");
    }
    
    protected void setSelectedImage(){
        selectedBi = properties.getImage(getKeyName()+"."+"selected");
    }
    
    protected void setActiveImage(){
        activeBi = properties.getImage(getKeyName()+"."+"active");
    }
    
    @Override
    public int getWidth(){
        return width;
    }
    
    @Override
    public int getHeight(){
        return height;
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
    protected void setLocalPins() {
        localPins = new LinkedList<Pin>();
        Map<String, PinPosition> inpins = properties.getInputPins();
        for(String k: inpins.keySet()){
            Point p = createPointFromPinPosition(inpins.get(k));
            localPins.add(new Pin(p, logicalComponent.getPinByName(k), inpins.get(k).getEdge()));
        }        
        Map<String, PinPosition> outpins = properties.getOutputPins();
        for(String k: outpins.keySet()){
            Point p = createPointFromPinPosition(outpins.get(k));
            localPins.add(new Pin(p, logicalComponent.getPinByName(k), outpins.get(k).getEdge()));
        }        
    }
    
    @Override
    public void draw(Graphics2D g) {
        super.draw(g); // Draw labels
        // Rotate the graphics context about the Centre Point
        g.rotate(rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);      
        // Translate to allow for local co-ordinates of pins
        g.translate(getOrigin().x, getOrigin().y);               
        g.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);
        // Draw all pin lines
        for(Pin p: localPins){
            switch(p.getEdge()){
                case North:
                    g.drawLine(p.x, p.y, p.x, p.y+(2*UIConstants.GRID_DOT_SPACING));
                    break;
                case South:
                    g.drawLine(p.x, p.y, p.x, p.y-(2*UIConstants.GRID_DOT_SPACING)); 
                    break;
                case West:
                    g.drawLine(p.x, p.y, p.x+(2*UIConstants.GRID_DOT_SPACING), p.y);
                    break;
                case East:
                    g.drawLine(p.x, p.y, p.x-(2*UIConstants.GRID_DOT_SPACING), p.y);                  
                    break;
            }
        }        
        g.translate(-getOrigin().x, -getOrigin().y); // Undo pin translation
        // Draw the current image with its origin at the correct place
        g.drawImage(getCurrentImage(), getOrigin().x, getOrigin().y, null);
        // Undo the rotation
        g.rotate(-rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
    }
            
    @Override
    public boolean containsPoint(Point point) {
        return getInvalidArea().contains(point);
    }
    
    @Override
    public final void createXML(TransformerHandler hd) {
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "", "type", "CDATA", getKeyName());
            atts.addAttribute("", "", "x", "CDATA", String.valueOf(getOrigin().x));
            atts.addAttribute("", "", "y", "CDATA", String.valueOf(getOrigin().y));
            atts.addAttribute("", "", "rotation", "CDATA", String.valueOf(rotation));
            atts.addAttribute("", "", "subcircuit", "CDATA", (this instanceof SubcircuitComponent)+"");            
            hd.startElement("", "", "component", atts);            
            properties.createXML(hd);            
            hd.endElement("", "", "component");
        } catch (SAXException ex) {
            ErrorHandler.newError("XML Creation Error","Please refer to the system output below.",ex);
        }
    }

    public void addLogicalComponentToCircuit(){
       if(logicalComponent!=null){
            this.parent.getLogicalCircuit().addSimItem(logicalComponent);
        } else {
           ErrorHandler.newError("Component Creation Error",
                   "The logical component was not specified for " + getKeyName());
        }
    }

    private void setWidthAndHeight() {
        // If there isn't a visual component, we must get the width and height 
        // from the number of pins we have
        if (properties.getVisualComponentClass() == null) {
            // Calculate the width and height from the position of the pins;
            Map<String, PinPosition> inpins = properties.getInputPins();
            for (String k : inpins.keySet()) {
                PinPosition pp = inpins.get(k);
                switch(pp.getEdge()){
                    case East:
                    case West:
                       if(pp.getPlace() > height){ height = pp.getPlace(); }
                       break;
                    case North:
                    case South:
                       if(pp.getPlace() > width){ width = pp.getPlace(); }
                       break;
                } 
            }
            Map<String, PinPosition> outpins = properties.getOutputPins();
            for (String k : outpins.keySet()) {
                PinPosition pp = outpins.get(k);
                switch(pp.getEdge()){
                    case East:
                    case West:
                       if(pp.getPlace() > height){ height = pp.getPlace(); }
                       break;
                    case North:
                    case South:
                       if(pp.getPlace() > width){ width = pp.getPlace(); }
                       break;
                }
            }
            
            width = (width + 2)*UIConstants.GRID_DOT_SPACING;
            height = (height + 2)*UIConstants.GRID_DOT_SPACING;
        // Otherwise, use the dimensions of the default image
        } else {
            width = getDefaultImage().getWidth();
            height = getDefaultImage().getHeight();
        }
    }
}
