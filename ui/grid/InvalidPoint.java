package ui.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import ui.UIConstants;
import ui.components.SelectableComponent;

/**
 *
 * @author matt
 */
public class InvalidPoint extends GridObject {
    
    private SelectableComponent parent;
    
    public InvalidPoint(Point p, SelectableComponent parent){
        super(p);
        this.parent = parent;
    }

    @Override
    public void draw(Graphics2D g2) {
        if(UIConstants.SHOW_CONNECTION_POINTS){
            g2.drawLine(x-2, y-2, x+2, y+2);
            g2.drawLine(x-2, y+2, x+2, y-2);
        }
    }
    
    public SelectableComponent getParent(){
        return parent;
    }

}
