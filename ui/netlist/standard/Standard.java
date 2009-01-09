package ui.netlist.standard;

import ui.netlist.*;

/**
 *
 * @author matt
 */
public class Standard extends Netlist{

    @Override
    protected void setClassMappings() {
        putClass("Standard.Button Source",    ui.netlist.standard.Input.class);
        putClass("Standard.Wire",             ui.netlist.standard.Wire.class);
        putClass("Standard.LED",              ui.netlist.standard.LED.class);
       
    }  
    
    @Override
    protected void setImageMappings() {
        // Active Images
        putImage("Standard.Button Source.default",   "build/classes/ui/images/components/default_input_off.png");
        putImage("Standard.LED.default",             "build/classes/ui/images/components/default_led.png");
        
        // Active Images
        putImage("Standard.Button Source.active",   "build/classes/ui/images/components/default_input_on.png");
        putImage("Standard.LED.active",             "build/classes/ui/images/components/default_led_on_red.png");
        
        // Selected Images
        putImage("Standard.Button Source.selected",   "build/classes/ui/images/components/default_input_off.png");
        putImage("Standard.LED.selected",             "build/classes/ui/images/components/default_led.png");
        
    }

}
