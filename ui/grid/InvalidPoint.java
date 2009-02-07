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
    
    public InvalidPoint(Grid grid, Point p, SelectableComponent parent){
        super(grid, p);
        this.parent = parent;
    }

    @Override
    public void draw(Graphics2D g2) {
        if(UIConstants.SHOW_GRID_OBJECTS){
            g2.drawLine(x-2, y-2, x+2, y+2);
            g2.drawLine(x-2, y+2, x+2, y-2);
        }
    }
    
    public SelectableComponent getParent(){
        return parent;
    }
    
    @Override
    public boolean hasParent(SelectableComponent sc){
        return sc.equals(parent);
    }

}
