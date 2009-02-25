package ui.components.standard;

import java.awt.Graphics2D;
import sim.LogicState;
import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import ui.UIConstants;

/**
 *
 * @author Matt
 */
public class SevenSegmentDisplay extends VisualComponent implements sim.joinable.ValueListener{
    private BufferedImage A, B, C, D, E, F, G, DP;
    private boolean isOn;

    public SevenSegmentDisplay(ui.CircuitPanel parent, Point point, sim.SimItem simItem, netlist.properties.Properties properties) {
        super(parent, point, simItem,properties);
        setSpecialImage();
        logicalComponent.getPinByName("Input").addValueListener(this);
    }
    
    protected void setSpecialImage() {
        A = properties.getImage("A");
        B = properties.getImage("B");
        C = properties.getImage("C");
        D = properties.getImage("D");
        E = properties.getImage("E");
        F = properties.getImage("F");
        G = properties.getImage("G");
        DP = properties.getImage("DP");
    }
    
    @Override
    public String getName(){
        return "7-Segment Display";
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x+18,(int)getOrigin().getY()-getCentre().y+4,43,57);
        invalidArea = rotate(invalidArea);   
    }
    
    @Override
    public Point getCentre(){
        return new Point(40,40);
    }
 
    public void setValue(boolean isOn){
        this.isOn = isOn;
    }
    
    public boolean getValue(){
        return isOn;
    }
    
    public void valueChanged(sim.joinable.Pin pin, LogicState value) {
        setValue(value.equals(sim.LogicState.ON));
        parent.repaint(getBoundingBox());
    }

    @Override
    public void draw(Graphics2D g) {
        if(hasLabel()){
            g.setColor(UIConstants.LABEL_TEXT_COLOUR);
            g.drawString(getLabel(), 
                    getOrigin().x+UIConstants.LABEL_COMPONENT_X_OFFSET+4,
                    getOrigin().y+UIConstants.LABEL_COMPONENT_Y_OFFSET-8);
        }
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
        if(getLabel().contains("A")){g.drawImage(A, getOrigin().x, getOrigin().y, null);}
        if(getLabel().contains("B")){g.drawImage(B, getOrigin().x, getOrigin().y, null);}
        if(getLabel().contains("C")){g.drawImage(C, getOrigin().x, getOrigin().y, null);}
        if(getLabel().contains("D")){g.drawImage(D, getOrigin().x, getOrigin().y, null);}
        if(getLabel().contains("E")){g.drawImage(E, getOrigin().x, getOrigin().y, null);}
        if(getLabel().contains("F")){g.drawImage(F, getOrigin().x, getOrigin().y, null);}
        if(getLabel().contains("G")){g.drawImage(G, getOrigin().x, getOrigin().y, null);}
        if(getLabel().contains("P")){g.drawImage(DP, getOrigin().x, getOrigin().y, null);}        
        g.rotate(-rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
    }
    
    
}


