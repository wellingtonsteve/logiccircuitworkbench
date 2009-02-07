package ui.components.standard;

import java.awt.Graphics2D;
import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;
import ui.UIConstants;

/**
 *
 * @author Matt
 */
public class Logger extends ImageSelectableComponent{
    private Pin in1;

    public Logger(ui.CircuitPanel parent, Point point, sim.SimItem simItem) {
        super(parent, point, simItem);
        parent.addLogger(simItem.getPinByName("Input"), parent.getSimulator());
    }

    @Override
    protected void setNetlist() {
        nl = new netlist.Standard();
    }
    
    @Override
    protected void setComponentTreeName() {
        componentTreeName = "Standard.Output Logger";
    }
    
    
    @Override
    public String getName(){
        return "Output Logger";
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x+9,(int)getOrigin().getY()-getCentre().y+8,46,43);
        invalidArea = rotate(invalidArea);   
    }
    
    @Override
    public Point getCentre(){
        return new Point(30,30);
    }

    @Override
    public void setLocalPins() {
        localPins.clear(); 
        in1 = new Pin(30, 70, logicalComponent.getPinByName("Input"));
        localPins.add(in1);        
    }
            
    @Override
    public void draw(Graphics2D g) {
         if(hasLabel()){
            g.setColor(UIConstants.LABEL_TEXT_COLOUR);
            g.drawString(getLabel(), 
                    getOrigin().x+UIConstants.LABEL_COMPONENT_X_OFFSET,
                    getOrigin().y+UIConstants.LABEL_COMPONENT_Y_OFFSET);
        }
        g.rotate(rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);      
        g.translate(getOrigin().x, getOrigin().y);               
        g.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);
        g.drawLine(30, 70, 30, 48);
        g.translate(-getOrigin().x, -getOrigin().y);
        g.drawImage(getCurrentImage(), getOrigin().x, getOrigin().y, null);
        g.rotate(-rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
    }
    
}
