/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.grid;

import java.awt.Point;
import sim.OutputValueListener;
import sim.State;
import ui.tools.SelectableComponent;

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
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pin other = (Pin) obj;
        if (this.parent != other.parent && (this.parent == null || !this.parent.equals(other.parent))) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 59 * hash + (this.parent != null ? this.parent.hashCode() : 0);
        return hash;
    }

    public void outputValueChanged(State value) {
        this.value = value;
    }    
}

