package ui.components;

import java.awt.Graphics2D;

/** Classes that implement this interface are then "Label-able", in that label strings
 * can easily be added, edited and removed from then.
 * 
 * @author matt
 */
public interface Labeled {    
    public void draw(Graphics2D g2);
                
    /** @return Return the label. */
    public String getLabel();

    /** Set the label to specified String
     * @param label The new label to change this objects label to.*/
    public void setLabel(String label);
    
    /** Clear the label associated with this object. hasLabel() should return true if
     * performed immediately after this method. */
    public void removeLabel();
    
    /** Return true if, and only if this component does not have a label initialised 
     * of if it has been removed.
     * @return Whether this object has a label. */
    public boolean hasLabel();
}
