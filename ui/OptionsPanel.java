package ui;

import ui.components.SelectableComponent;

/**
 *
 * @author  matt
 */
public interface OptionsPanel {
    /**
     * Reset the value of the Component Label Textbox
     */
    public void resetLabel();
    
    /**
     * Return the current value of the Component Label Textbox
     * 
     * @return The String of labelTextbox
     */
    public String getCurrentLabel();
    
    /**
     * @return a copy of the component which this options panel displays.
     */
    public SelectableComponent getSelectableComponent();
    
    /**
     * Set the component which this panel shows. This component has already been
     * created and will normally come from a selection in the circuit workarea.
     * 
     * @param sc
     */
    public void setComponent(SelectableComponent sc);

    /**
     * Rotate the component that is displayed in this options panel, also force
     * repaints as necessary. Changes to the component here are reflected in the 
     * unfixed component in the workarea.
     * 
     * @param d
     */
    public void setComponentRotation(double d);
    
    /**
     * @return the current rotation of the component displayed in the panel.
     */
    public Double getComponentRotation();
}
