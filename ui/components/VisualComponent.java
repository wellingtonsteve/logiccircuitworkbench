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
import ui.command.SubcircuitOpenCommand.SubcircuitComponent;

/**
 *
 * @author matt
 */
public abstract class VisualComponent extends SelectableComponent {
    
    protected BufferedImage defaultBi;
    protected BufferedImage selectedBi;
    protected BufferedImage activeBi;
      
    public VisualComponent(CircuitPanel parent, Point point, sim.SimItem logicalComponent,netlist.properties.Properties properties){
        super(parent, point, logicalComponent, properties);
        
        setDefaultImage();
        setSelectedImage();
        setActiveImage();
        
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
        return getDefaultImage().getWidth();
    }
    
    @Override
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
    protected void setLocalPins() {
        localPins = new LinkedList<Pin>();
        Map<String, Point> inpins = properties.getInputPins();
        for(String k: inpins.keySet()){
            localPins.add(new Pin(inpins.get(k), logicalComponent.getPinByName(k)));
        }
        
        Map<String, Point> outpins = properties.getOutputPins();
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
            ui.error.ErrorHandler.newError("XML Creation Error","Please refer to the system output below.",ex);
        }
    }

    public void addLogicalComponentToCircuit(){
       if(logicalComponent!=null){
            this.parent.getLogicalCircuit().addSimItem(logicalComponent);
        } else {
           System.out.println("no logical component");
        }
    }
}
