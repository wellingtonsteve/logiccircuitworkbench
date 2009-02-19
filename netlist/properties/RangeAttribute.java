package netlist.properties;

import javax.swing.JSlider;

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
        JSlider js = new JSlider(minValue, maxValue, defaultValue); 
        jcomponent = js;
    }
}
