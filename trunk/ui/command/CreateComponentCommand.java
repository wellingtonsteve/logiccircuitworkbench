package ui.command;

import java.awt.Graphics2D;
import java.awt.Point;
import java.lang.reflect.Constructor;
import netlist.Netlist;
import netlist.properties.Properties;
import sim.SimItem;
import ui.CircuitPanel;
import ui.Editor;
import ui.UIConstants;
import ui.error.ErrorHandler;
import ui.components.SelectableComponent;
import ui.components.SelectionState;
import ui.components.VisualComponent;

/**
 *
 * @author matt
 */
public class CreateComponentCommand extends Command {
    private SelectableComponent sc;
    private CircuitPanel parentCircuit;
    private Object[] properties;
    private String key;
        /* Properties Format
         *      properties[0] = componentName
         *      properties[1] = rotation
         *      properties[2] = point
         *      properties[3] = label
         *      properties[4] = LED Colour
         *      properties[5] = Input On/Off
         *      properties[6] = activeCircuit
         */

    public CreateComponentCommand(Object[] properties){
        this.properties = properties;
    }
    
    @Override
    protected void perform(Editor editor) {
        key = (String) properties[0];
        if(properties.length < 7 || properties[6] == null){
            parentCircuit = activeCircuit;
        } else {
            parentCircuit = (CircuitPanel) properties[6];
        }
        //Remove "Components." from begining
        if(key.length() > 11 && key.subSequence(0, 11).equals("Components.")){
            key = key.substring(11); 
        }   
        if(parentCircuit != null){
            try {                
                if(properties[0].equals("Wire")){                    
                    sc = new ui.components.Wire(parentCircuit);
                    if(properties[2] != null){
                        sc.setOrigin((Point) properties[2]);
                    }
                }else if(editor.isValidComponent(key)){
                    
                    Properties props = editor.getNetlistWithKey(key).getProperties(key);
                    SimItem simItem = props.getLogicalComponentClass().getConstructor().newInstance();
                
                    if(props.getVisualComponentClass() != null && editor.getNetlistWithKey(key).containsLogicKey(key)){
                            sc = props.getVisualComponentClass().getConstructor(
                                CircuitPanel.class,
                                Point.class, 
                                SimItem.class,
                                Properties.class)
                                    .newInstance(
                                        parentCircuit, 
                                        (Point) properties[2], 
                                        simItem,
                                        props);
                    } else { 
                        sc = new ImageSelectableComponentImpl(parentCircuit, (Point) properties[2], simItem, props);
                    }                     
                    sc.setProperties(props);
                } else {
                    throw new Exception();
                }                            
                
                sc.setRotation((Double) properties[1], true);                             
                if(properties[3] != null && !properties[3].equals("")) {
                    sc.setLabel((String) properties[3]);
                }

                parentCircuit.addComponent(sc);
                editor.setComponent(sc);
                
            } catch (Exception ex){
                editor.clearComponentSelection();
                ErrorHandler.newError("Component Creation Error",
                        "An error occured whilst creating a new component.", ex);
            }       
        } 
    }
    
    /**
     * @return the component created by this command.
     */
    public SelectableComponent getComponent(){
        return sc;
    }

    @Override
    public String getName() {
        return "Add Component to Circuit";
    }
    
     /** This class used to create a drawable component when only details of the 
     * logical component are available */
    private class ImageSelectableComponentImpl extends VisualComponent {
        private int spacing = 2*UIConstants.GRID_DOT_SPACING;
        
        public ImageSelectableComponentImpl(CircuitPanel parent, Point point, sim.SimItem simItem, netlist.properties.Properties properties) {
            super(parent, point, simItem,properties);            
        }
        
        @Override
        public void setLocalPins() {
            localPins.clear();
            spacing = 2*UIConstants.GRID_DOT_SPACING;
            int inputPinNo = logicalComponent.getInputs().size();
            int outputPinNo = logicalComponent.getOutputs().size();

            for (int i = 0; i < inputPinNo; i++) {
                Pin p = new Pin(0, (i + 1) * spacing, logicalComponent.getPinByName("Input" + ((inputPinNo>1)?" " +(i+1):"")));
                localPins.add(p);
            }

            for (int i = 0; i < outputPinNo; i++) {
                Pin p = new Pin(getWidth() + spacing, (i + 1) * spacing, logicalComponent.getPinByName("Output" + ((outputPinNo>1)?" " +(i+1):"")));
                localPins.add(p);
            }
        }
        
        @Override
        protected void setBoundingBox(){
            boundingBox = new java.awt.Rectangle(getOrigin().x-getCentre().x,
                    getOrigin().y-getCentre().y,
                    getWidth()+10,
                    getHeight());
            boundingBox = rotate(boundingBox);
        }
        
        @Override
        public int getWidth(){
            return 50;
        }

        @Override
        public int getHeight(){
            return (Math.max(logicalComponent.getInputs().size(), logicalComponent.getOutputs().size()) + 1) * spacing;
        }
        
        @Override
        protected void setInvalidAreas() {
            invalidArea = new java.awt.Rectangle(getOrigin().x-getCentre().x+9, 
                    getOrigin().y-getCentre().y-1, 
                    getWidth()-spacing+2, 
                    getHeight()+2);
            invalidArea = rotate(invalidArea);
        }

        @Override
        public Point getCentre() {
            return new Point(30, 30);
        }

        @Override
        public void draw(Graphics2D g) {
           if(hasLabel()){
                g.setColor(UIConstants.LABEL_TEXT_COLOUR);
                g.drawString(getLabel(), getOrigin().x, getOrigin().y-2);
            }
            
            g.rotate(rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
            g.translate(getOrigin().x, getOrigin().y);
              
            if(getSelectionState().equals(SelectionState.ACTIVE)){
                g.setColor(UIConstants.ACTIVE_COMPONENT_COLOUR);
                g.drawRect(10, 0, getWidth()-spacing, getHeight());
            } else if(getSelectionState().equals(SelectionState.HOVER)){
                g.setColor(UIConstants.CIRCUIT_BACKGROUND_COLOUR);
                g.fillRect(10, 0, getWidth()-spacing, getHeight());
                g.setColor(UIConstants.HOVER_COMPONENT_COLOUR);
                g.drawRect(10, 0, getWidth()-spacing, getHeight());
            } else {
                g.setColor(UIConstants.CIRCUIT_BACKGROUND_COLOUR);
                g.fillRect(10, 0, getWidth()-spacing, getHeight());             
                g.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);
                g.drawRect(10, 0, getWidth()-spacing, getHeight());
            }            
            
            g.setColor(UIConstants.DEFAULT_COMPONENT_COLOUR);
            String name = logicalComponent.getShortName();
            g.drawString(name.substring(0, Math.min(6,name.length()-1)), 10, 10);
            
            for(Pin p: localPins){
                if(p.getJoinable() instanceof sim.joinable.InputPin){
                    g.drawLine(p.x, p.y, p.x+(2*UIConstants.GRID_DOT_SPACING), p.y);
                } else if(p.getJoinable() instanceof sim.joinable.OutputPin){
                    g.drawLine(p.x, p.y, p.x-(2*UIConstants.GRID_DOT_SPACING), p.y);
                }
            }      

            g.translate(-getOrigin().x, -getOrigin().y);
            g.rotate(-rotation, getOrigin().x + getCentre().x, getOrigin().y + getCentre().y);
        }

        @Override
        protected void setKeyName() {
            keyName = key;
        }

        @Override
        protected void setDefaultImage() {
            defaultBi = null;
        }

        @Override
        protected void setSelectedImage() {
            selectedBi = null;
        }

        @Override
        protected void setActiveImage() {
            activeBi = null;
        }
    }   

}
