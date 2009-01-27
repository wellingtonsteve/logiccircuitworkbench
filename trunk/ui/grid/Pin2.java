package ui.grid;

import java.awt.Point;
import sim.OutputValueListener;
import sim.State;
import ui.components.SelectableComponent;

/**
 *
 * @author matt
 */
public class Pin2 extends Point implements OutputValueListener {

    private SelectableComponent parent;
    private State value;
    private String name;

    public Pin2(SelectableComponent parent, int x, int y){
        super(x,y);
        this.parent = parent;
    }   
        
    public Pin2(SelectableComponent parent, String name, int x, int y){
        super(x,y);
        this.parent = parent;
        this.name = name;
    }   
   
    public SelectableComponent getParent(){
        return parent;        
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Pin2){
            Pin2 other = (Pin2) obj;
            return super.equals(obj) && this.getParent().equals(other.getParent());
        } else {
            return super.equals(obj);
        }
    }

    public void outputValueChanged(State value) {
        this.value = value;
    }    
}