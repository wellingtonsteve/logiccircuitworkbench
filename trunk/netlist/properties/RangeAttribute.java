package netlist.properties;

import java.awt.Dimension;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Matt
 */
public class RangeAttribute extends Attribute{
    private int defaultValue;
    private int minValue;
    private int maxValue;
    
    public RangeAttribute(String name, int defaultValue, int minValue, int maxValue){
        super(name, defaultValue);
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    protected void setJComponent() {
        final JSlider js = new JSlider(minValue, maxValue, defaultValue); 
        js.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                 setValue(js.getValue());
            }
        });
        js.setMaximumSize(new Dimension(100, 25));
        js.setMinorTickSpacing(1);
        js.setMajorTickSpacing(10);
        
        jcomponent = js;
    }
}