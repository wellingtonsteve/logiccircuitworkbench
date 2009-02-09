package ui.command;

import java.awt.Point;
import sim.SimItem;
import ui.CircuitPanel;
import ui.Editor;
import ui.error.ErrorHandler;
import ui.components.SelectableComponent;
import ui.components.standard.*;

/**
 *
 * @author matt
 */
public class CreateComponentCommand extends Command {
    private SelectableComponent sc;
    private CircuitPanel parentCircuit;
    private Object[] properties;
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
        if(properties.length < 7 || properties[6] == null){
            parentCircuit = activeCircuit;
        } else {
            parentCircuit = (CircuitPanel) properties[6];
        }
        if(parentCircuit != null){
            try {
                if(editor.isDrawableComponent((String) properties[0])){
                    SimItem simItem = editor.getLogicalComponent((String) properties[0]);                 
                    sc = editor.getNetlistComponent((String) properties[0]).getConstructor(
                            CircuitPanel.class,
                            Point.class, 
                            SimItem.class)
                                .newInstance(parentCircuit, (Point) properties[2], simItem);
                } else if(properties[0].equals("Wire")){
                    sc = new ui.components.Wire(parentCircuit);
                } else if(editor.isValidComponent((String) properties[0])) { 
                    sc = editor.getDefaultNetlistComponent((String) properties[0]);
                    sc.setOrigin((Point) properties[2]);
                }
                sc.setRotation((Double) properties[1], true);
                sc.setLabel((String) properties[3]);
                
                // Display component specific layout options
                if(sc instanceof LED){
                    if(properties[4] != null){
                        ((LED) sc).setColour((String) properties[4]);
                    }
                }
                if(sc instanceof Input){
                    if(properties[5] != null){
                        ((Input) sc).setIsOn((Boolean) properties[5]);
                    }
                }

                parentCircuit.addComponent(sc);
                editor.getOptionsPanel().setComponent(sc);
                
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
    
    

}
