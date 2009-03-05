package ui.command;

import java.awt.Graphics2D;
import java.awt.Point;
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
    private String key;
    private Point point;
    private double rotation;
    
    public CreateComponentCommand(CircuitPanel circuit, String type, double rotation, Point p) {
        this.key = type;
        this.rotation = rotation;
        this.point = p;
        this.parentCircuit = circuit;
    }
    
    @Override
    protected void perform(Editor editor) {
        if(parentCircuit == null){
            parentCircuit = activeCircuit;
        }        
        
        //Remove "Components." from begining
        if(key.length() > 11 && key.subSequence(0, 11).equals("Components.")){
            key = key.substring(11); 
        }   
        if(parentCircuit != null){
            try {                
                // Create a new wire
                if(key.equals("Wire")){                    
                    sc = new ui.components.Wire(parentCircuit);
                    if(point!=null){
                        sc.setOrigin(point);
                    }
                // The key corresponds to a valid key in a loaded netlist
                }else if(editor.isValidComponent(key)){
                    
                    Properties props = editor.getNetlistWithKey(key).getProperties(key);
                    SimItem simItem = props.getLogicalComponentClass().getConstructor().newInstance();
                
                    // Was there a visual component class specified
                    if(props.getVisualComponentClass() != null && editor.getNetlistWithKey(key).containsKey(key)){
                            sc = props.getVisualComponentClass().getConstructor(
                                CircuitPanel.class,
                                Point.class, 
                                SimItem.class,
                                Properties.class)
                                    .newInstance(
                                        parentCircuit, 
                                        point, 
                                        simItem,
                                        props);
                    // Create a default visual component from the logical properties of the component
                    } else { 
                        sc = new DefaultVisualComponent(parentCircuit, point, simItem, props);
                    }                     

                // Is this a subcircuit?
                } else if(parentCircuit.isSubcircuit()){
                        // TODO something
                } else {
                    throw new InvalidComponentException();
                }                            
                
                sc.setRotation(rotation, true);                             
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
    private class DefaultVisualComponent extends VisualComponent {
        private int spacing = 2*UIConstants.GRID_DOT_SPACING;
        
        public DefaultVisualComponent(CircuitPanel parent, Point point, sim.SimItem simItem, netlist.properties.Properties properties) {
            super(parent, point, simItem,properties);            
        }
        
        @Override
        protected void setLocalPins() {
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
