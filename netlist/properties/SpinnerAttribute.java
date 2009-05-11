package netlist.properties;

import java.awt.Dimension;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** This specific attribute allows any numeric value between minValue and 
 * maxValue and is represented by a Spinner component in the user interface.
 * @author Matt */
public class SpinnerAttribute extends Attribute{
    private int defaultValue;
    private int stepValue;
    private int minValue;
    private int maxValue;
    
    public SpinnerAttribute(String name, int defaultValue, int minValue,
            int maxValue, int stepValue){
        super(name, defaultValue);
        this.defaultValue = defaultValue;
        this.stepValue = stepValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        setJComponent();
    }

    @Override
    protected void setJComponent() {
        final SpinnerNumberModel model = new SpinnerNumberModel(defaultValue,
                minValue, maxValue, stepValue);
        final JSpinner js = new javax.swing.JSpinner();
        model.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                js.setValue(model.getNumber().intValue());
                SpinnerAttribute.this.changeValue(model.getNumber().intValue());
            }
        });
        js.setModel(model);
        js.setPreferredSize(new Dimension(50, 25));
        jcomponent = js;
    }
    
    @Override
    public void setValue(Object val) {
        if(val instanceof String){
            val = Integer.parseInt((String) val);
        }
        if(validate(val)){
            ((JSpinner) jcomponent).setValue(val);
        }
        super.setValue(val);        
    }
}
