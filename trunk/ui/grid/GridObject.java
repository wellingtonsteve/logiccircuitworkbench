package ui.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collection;
import ui.components.Labeled;
import ui.UIConstants;
import ui.components.SelectableComponent;

/**
 *
 * @author Matt
 */
public abstract class GridObject extends Point implements Labeled, Cloneable {
    private String label = new String();

    public GridObject(Point p){
        super(p.x,  p.y);
    }   
    
    public void draw(Graphics2D g2){
        if(hasLabel()){
            g2.drawString(getLabel(), x+UIConstants.LABEL_CONNECTION_POINT_X_OFFSET, y+UIConstants.LABEL_CONNECTION_POINT_Y_OFFSET);
        }
    }
    
    public abstract boolean hasParent(SelectableComponent sc);
    
    public boolean hasParentInCollection(Collection<SelectableComponent> collection) {
        for(SelectableComponent sc: collection){
            if(hasParent(sc)){
                return true;
            }
        }
        return false;
    }
    
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    public void removeLabel(){
        this.label = null;
    }
    
    public boolean hasLabel(){
        return !label.isEmpty() || !label.equals("");
    }

 

}
