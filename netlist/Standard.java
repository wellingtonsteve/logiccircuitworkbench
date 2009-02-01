package netlist;

/**
 *
 * @author matt
 */
public class Standard extends Netlist{

    @Override
    protected void setDrawableMappings() {
        putDrawableClass("Standard.Button Source",    ui.components.standard.Input.class);
        putDrawableClass("Standard.LED",              ui.components.standard.LED.class);  
        putDrawableClass("Standard.Output Logger",    ui.components.standard.Logger.class);
    }  
    
    @Override
    protected void setImageMappings() {
        // Default Images
        putImage("Standard.Button Source.default",   "/ui/images/components/default_input_off.png");
        putImage("Standard.LED.default",             "/ui/images/components/default_led.png");
        putImage("Standard.Output Logger.default",   "/ui/images/components/default_logger.png");
        
        // Active Images
        putImage("Standard.Button Source.active",   "/ui/images/components/default_input_on.png");
        putImage("Standard.LED.active",             "/ui/images/components/default_led_on_red.png");
        putImage("Standard.Output Logger.active",   "/ui/images/components/active_logger.png");
        
        // Selected Images
        putImage("Standard.Button Source.selected",   "/ui/images/components/default_input_off.png");
        putImage("Standard.LED.selected",             "/ui/images/components/default_led.png");        
        putImage("Standard.Output Logger.selected",   "/ui/images/components/selected_logger.png");
    }

    @Override
    protected void setLogicMappings() {
        putLogicClass("Standard.Button Source",    sim.componentLibrary.standard.Input.class);
        putLogicClass("Standard.LED",              sim.componentLibrary.standard.Output.class);
        putLogicClass("Standard.Output Logger",    sim.componentLibrary.standard.Output.class);
    }

}
