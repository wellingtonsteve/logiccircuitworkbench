package ui.components.standard;

import java.awt.Graphics2D;
import sim.LogicState;
import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import ui.UIConstants;

/**
 *
 * @author Matt
 */
public class SevenSegmentDisplay extends ImageSelectableComponent implements sim.pin.ValueListener{
    private BufferedImage A, B, C, D, E, F, G, DP;
    private boolean isOn;

    public SevenSegmentDisplay(ui.CircuitPanel parent, Point point, sim.SimItem simItem) {
        super(parent, point, simItem);
        setSpecialImage();
        logicalComponent.getPinByName("Input").addValueListener(this);
    }

    @Override
    protected void setNetlist() {
        nl = new netlist.Standard();
    }
    
    @Override
    protected void setComponentTreeName() {
        componentTreeName = "Standard.7 Segment Display";
    }
    
    protected void setSpecialImage() {
        try {
            A = ImageIO.read(getClass().getResource("/ui/images/components/default_7seg_A.png"));
            B = ImageIO.read(getClass().getResource("/ui/images/components/default_7seg_B.png"));
            C = ImageIO.read(getClass().getResource("/ui/images/components/default_7seg_C.png"));
            D = ImageIO.read(getClass().getResource("/ui/images/components/default_7seg_D.png"));
            E = ImageIO.read(getClass().getResource("/ui/images/components/default_7seg_E.png"));
            F = ImageIO.read(getClass().getResource("/ui/images/components/default_7seg_F.png"));
            G = ImageIO.read(getClass().getResource("/ui/images/components/default_7seg_G.png"));
            DP = ImageIO.read(getClass().getResource("/ui/images/components/default_7seg_DP.png"));
        } catch (IOException ex) {
             ui.error.ErrorHandler.newError(new ui.error.Error("Initialisation Error", "Could not load \"LED On\" image.", ex));    
        }
    }
    
    @Override
    public String getName(){
        return "7-Segment Display";
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x+15,(int)getOrigin().getY()-getCentre().y,45,65);
        invalidArea = rotate(invalidArea);   
    }
    
    @Override
    public Point getCentre(){
        return new Point(40,40);
    }

    @Override
    public void setLocalPins() {
        localPins.clear();
        Pin in1 = new Pin(10, 10, logicalComponent.getPinByName("Input"));             
        localPins.add(in1);        
    }
        
    public void setValue(boolean isOn){
        this.isOn = isOn;
    }
    
    public boolean getValue(){
        return isOn;
    }
    
    public void valueChanged(sim.pin.Pin pin, LogicState value) {
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
            if(p.getJoinable() instanceof sim.pin.InputPin){
                g.drawLine(p.x, p.y, p.x+(2*UIConstants.GRID_DOT_SPACING), p.y);
            } else if(p.getJoinable() instanceof sim.pin.OutputPin){
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


