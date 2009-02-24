package netlist.properties;

import java.awt.Dimension;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Matt
 */
public class SpinnerAttribute extends Attribute{
    private int defaultValue;
    private int stepValue;
    private int minValue;
    private int maxValue;
    
    public SpinnerAttribute(String name, int defaultValue, int minValue, int maxValue, int stepValue){
        super(name, defaultValue);
        this.defaultValue = defaultValue;
        this.stepValue = stepValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        setJComponent();
    }

    @Override
    protected void setJComponent() {
        final SpinnerNumberModel model = new SpinnerNumberModel(defaultValue, minValue, maxValue, stepValue);
        final JSpinner js = new javax.swing.JSpinner();
        model.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                js.setValue(model.getNumber().intValue());
                SpinnerAttribute.this.setValue(model.getNumber().intValue());
            }
        });
        js.setModel(model);
        js.setPreferredSize(new Dimension(100, 25));
        js.setMaximumSize(new Dimension(100, 25));
        jcomponent = js;
    }
}
