package ui.grid;

import java.awt.Point;
import sim.OutputValueListener;
import sim.State;
import ui.components.SelectableComponent;

/**
 *
 * @author matt
 */
public class Pin extends Point implements OutputValueListener {

    private SelectableComponent parent;
    private State value;

    public Pin(SelectableComponent parent, int x, int y){
        super(x,y);
        this.parent = parent;
    }   
   
    public SelectableComponent getParent(){
        return parent;        
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Pin){
            Pin other = (Pin) obj;
            return super.equals(obj) && this.getParent().equals(other.getParent());
        } else {
            return super.equals(obj);
        }
    }

    public void outputValueChanged(State value) {
        this.value = value;
    }    
}