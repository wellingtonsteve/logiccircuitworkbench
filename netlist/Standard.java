package netlist;

/**
 * Netlist of standard components. Inputs, Outputs, Pin Loggers  etc.
 * @author matt
 */
public class Standard extends NetlistImp{

    @Override
    protected void setDrawableMappings() {
        putDrawableClass("Standard.Button Source",    ui.components.standard.Input.class);
        putDrawableClass("Standard.Oscillator",       ui.components.standard.Oscillator.class);
        putDrawableClass("Standard.LED",              ui.components.standard.LED.class);  
        putDrawableClass("Standard.Output Logger",    ui.components.standard.PinLogger.class);
        putDrawableClass("Standard.Buzzer",           ui.components.standard.Buzzer.class);
        putDrawableClass("Standard.7 Segment Display",    ui.components.standard.SevenSegmentDisplay.class);
    }  
    
    @Override
    protected void setImageMappings() {
        // Default Images
        putImage("Standard.Button Source.default",   "/ui/images/components/default_input_off.png");
        putImage("Standard.Oscillator.default",     "/ui/images/components/default_oscillator.png");
        putImage("Standard.LED.default",             "/ui/images/components/default_led.png");
        putImage("Standard.Output Logger.default",   "/ui/images/components/default_logger.png");
        putImage("Standard.Buzzer.default",          "/ui/images/components/default_buzzer.png");
        putImage("Standard.7 Segment Display.default",   "/ui/images/components/default_7seg.png");
        
        // Active Images
        putImage("Standard.Button Source.active",   "/ui/images/components/active_input_off.png");
        putImage("Standard.Oscillator.active", "/ui/images/components/active_oscillator.png");
        putImage("Standard.LED.active",             "/ui/images/components/active_led.png");
        putImage("Standard.Output Logger.active",   "/ui/images/components/active_logger.png");
        putImage("Standard.Buzzer.active",          "/ui/images/components/active_buzzer.png");
        putImage("Standard.7 Segment Display.active",   "/ui/images/components/active_7seg.png");
        
        // Selected Images
        putImage("Standard.Button Source.selected",   "/ui/images/components/selected_input_off.png");
        putImage("Standard.Oscillator.selected",     "/ui/images/components/selected_oscillator.png");
        putImage("Standard.LED.selected",             "/ui/images/components/selected_led.png");        
        putImage("Standard.Output Logger.selected",   "/ui/images/components/selected_logger.png");
        putImage("Standard.Buzzer.selected",          "/ui/images/components/selected_buzzer.png");
        putImage("Standard.7 Segment Display.selected",   "/ui/images/components/selected_7seg.png");
    }

    @Override
    protected void setLogicMappings() {
        putLogicClass("Standard.Button Source",    sim.componentLibrary.standard.Input.class);
        putLogicClass("Standard.Oscillator",       sim.componentLibrary.standard.Input.class);
        putLogicClass("Standard.LED",              sim.componentLibrary.standard.Output.class);
        putLogicClass("Standard.Output Logger",    sim.componentLibrary.standard.Output.class);
        putLogicClass("Standard.Buzzer",           sim.componentLibrary.standard.Output.class);
        putLogicClass("Standard.7 Segment Display",    sim.componentLibrary.standard.Output.class);
    }

}
