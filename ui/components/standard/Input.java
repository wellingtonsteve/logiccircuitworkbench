package ui.components.standard;

import ui.components.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import netlist.properties.Properties;
import sim.SimulatorState;

/**
 *
 * @author Matt
 */
public class Input extends VisualComponent{
    private boolean isOn = false;
    private BufferedImage specialBi;

    public Input(ui.CircuitPanel parent, Point point, sim.SimItem simItem, Properties properties) {
        super(parent, point, simItem, properties);
        setSpecialImage();
    }
   
    @Override
    public String getName(){
        return "Button Source [On/Off]";
    }

    @Override
    protected void setInvalidAreas(){
        invalidArea = new Rectangle((int)getOrigin().getX()-getCentre().x-1,(int)getOrigin().getY()-getCentre().y-1,22,22);
        invalidArea = rotate(invalidArea);   
    }
    
    @Override
    public Point getCentre(){
        return new Point(10,10);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if(e != null){
            if(isOn){
                ((sim.componentLibrary.standard.Input) logicalComponent).setValue(sim.LogicState.OFF);
            } else {
                ((sim.componentLibrary.standard.Input) logicalComponent).setValue(sim.LogicState.ON);
            }
        }
        isOn = !isOn; 
    }
    
    @Override
    protected BufferedImage getCurrentImage(){
        if(isOn){
            return specialBi;    
        } else {
            return super.getCurrentImage();            
        }    
    }
    
    protected void setSpecialImage() {
        specialBi = properties.getImage("default_on");
    }
    
    public boolean isOn() {
        return isOn;
    }

    public void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }
    
    @Override
    public void SimulatorStateChanged(SimulatorState state) {
        if(state.equals(SimulatorState.STOPPED)){
            isOn = false;
        } else if(state.equals(SimulatorState.PLAYING)){
            isOn = true;
        }
        resetDefaultState();
    }

}
