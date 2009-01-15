package netlist.standard;

import netlist.*;

/**
 *
 * @author matt
 */
public class Standard extends Netlist{

    @Override
    protected void setClassMappings() {
        putClass("Standard.Button Source",    netlist.standard.Input.class);
        putClass("Standard.LED",              netlist.standard.LED.class);
       
    }  
    
    @Override
    protected void setImageMappings() {
        // Active Images
        putImage("Standard.Button Source.default",   "/ui/images/components/default_input_off.png");
        putImage("Standard.LED.default",             "/ui/images/components/default_led.png");
        
        // Active Images
        putImage("Standard.Button Source.active",   "/ui/images/components/default_input_on.png");
        putImage("Standard.LED.active",             "/ui/images/components/default_led_on_red.png");
        
        // Selected Images
        putImage("Standard.Button Source.selected",   "/ui/images/components/default_input_off.png");
        putImage("Standard.LED.selected",             "/ui/images/components/default_led.png");
        
    }

    @Override
    protected void setLogicMappings() {
        putLogicClass("Standard.Button Source",    sim.componentLibrary.AndGate2Input.class);
        putLogicClass("Standard.LED",              sim.componentLibrary.AndGate2Input.class);
    }

}
