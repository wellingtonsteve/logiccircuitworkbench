package netlist;

/**
 *
 * @author matt
 */
public class Standard extends Netlist{

    @Override
    protected void setClassMappings() {
        putClass("Standard.Button Source",    ui.components.standard.Input.class);
        putClass("Standard.LED",              ui.components.standard.LED.class);  
    }  
    
    @Override
    protected void setImageMappings() {
        // Default Images
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
        putLogicClass("Standard.Button Source",    sim.componentLibrary.logicgates.AndGate2Input.class);
        putLogicClass("Standard.LED",              sim.componentLibrary.logicgates.AndGate2Input.class);
    }

}
