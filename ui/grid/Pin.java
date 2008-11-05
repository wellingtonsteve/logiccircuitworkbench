/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.grid;

import java.awt.Point;
import ui.tools.SelectableComponent;

/**
 *
 * @author matt
 */
public class Pin extends Point {

    private SelectableComponent parent;
    
    public Pin(SelectableComponent parent, int x, int y){
        super(x,y);
        this.parent = parent;
    }
    
    public SelectableComponent getParent(){
        return parent;        
    }
        
}
