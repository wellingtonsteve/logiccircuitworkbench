/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.command;

import java.awt.Point;
import netlist.standard.Input;
import netlist.standard.LED;
import netlist.standard.Wire;
import ui.CircuitPanel;
import ui.Editor;
import ui.error.ErrorHandler;
import ui.components.SelectableComponent;

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
                if(editor.isNetlistComponent((String) properties[0])){
                    sc = editor.getNetlistComponent((String) properties[0]).getConstructor(CircuitPanel.class, Point.class).newInstance(parentCircuit, (Point) properties[2]);
                } else {
                    sc = editor.getDefaultNetlistComponent((String) properties[0]);
                }
                sc.setRotation((Double) properties[1]);
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
                canUndo = true;
                
            } catch (Exception ex){
                ErrorHandler.newError("Component Creation Error","An error occured whilst creating a new component.", ex);
            }            
                        
            
        }
    }
    
    /**
     * Return the component created by this command.
     * 
     * @return
     */
    public SelectableComponent getComponent(){
        return sc;
    }
    
    @Override
    protected void undoEffect(Editor editor) {
        canUndo = false;
        parentCircuit.removeComponent(sc);
    }

    @Override
    public String toString() {
        return "Add Component to Circuit";
    }
    
    

}
