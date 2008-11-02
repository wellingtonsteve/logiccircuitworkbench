package ui.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import ui.UIConstants;

/**
 *
 * @author matt
 */
public class WireJoin extends ConnectionPoint {
    
    public WireJoin(Point p){
        super(p);
    }
    
    @Override
    public void draw(Graphics2D g2) {
         g2.setColor(UIConstants.DEFAULT_WIRE_COLOUR);
         g2.drawOval(x-2, y-2, 5, 5);
         g2.fillOval(x-2, y-2, 5, 5);
         super.draw(g2);
    }
    
}
