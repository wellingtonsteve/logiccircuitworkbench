package ui.grid;

import java.awt.Graphics;
import java.awt.Point;
import ui.Labeled;
import ui.UIConstants;

/**
 *
 * @author Matt
 */
public abstract class GridObject extends Point implements Labeled {
    private String label = new String();

    public GridObject(Point p){
        super(p.x,  p.y);
    }   
    
    public void draw(Graphics g){
        if(hasLabel()){
            g.drawString(getLabel(), x+UIConstants.LABEL_CONNECTION_POINT_X_OFFSET, y+UIConstants.LABEL_CONNECTION_POINT_Y_OFFSET);
        }
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
