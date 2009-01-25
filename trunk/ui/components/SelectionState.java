package ui.components;

/**
 * SelectionState.DEFAULT - The logicalComponent is neither selected nor active
 * and is drawn in its default view
 * 
 * SelectionState.HOVER - The logicalComponent is only in this state when the 
 * mouse as been positioned inside the logicalComponent's bounding box and 
 * the logicalComponent is not already active
 * 
 * SelectionState.ACTIVE - The logicalComponent has been selected (clicked) and
 * marked as active so that it's properties can be changed in the options 
 * panel or so that it can be moved.
 *
 * @author Matt
 */
public enum SelectionState {
    HOVER, DEFAULT, ACTIVE;
}
