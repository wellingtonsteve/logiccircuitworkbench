package netlist;

import java.awt.Point;
import netlist.properties.BooleanAttribute;
import netlist.properties.Properties;
import netlist.properties.SelectionAttribute;
import netlist.properties.TextAttribute;

/**
 *
 * @author Matt
 */
public class Standard extends Netlist{

    public Standard(){
        ////////////////////////
        // Input (Oscillator)
        ////////////////////////
        Properties oscillatorProps = new Properties("Standard.Oscillator", sim.componentLibrary.standard.Oscillator.class){
            // Further Options
            {
                setVisualComponentClass(ui.components.standard.Oscillator.class);

                addAttribute(new TextAttribute("t1 (ms)","5"){
                    @Override
                    public boolean validate(Object value) {
                        return super.validate(value);
                    }                    
                });
                addAttribute(new TextAttribute("t2 (ms)","5"));

                addImage("default", "/ui/images/components/default_oscillator.png");
                addImage("selected", "/ui/images/components/selected_oscillator.png");
                addImage("active", "/ui/images/components/active_oscillator.png");
                
                addOutputPin("Output", new Point(30,10));
            }
        };
        netlist.put("Standard.Oscillator", oscillatorProps);

        ////////////////////////
        // Input (Button Source)
        ////////////////////////
        Properties buttonProps = new Properties("Standard.Button Source", sim.componentLibrary.standard.Input.class){
            // Further Options
            {
                setVisualComponentClass(ui.components.standard.Input.class);

                addAttribute(new BooleanAttribute("External?", false));

                addImage("default", "/ui/images/components/default_input_off.png");
                addImage("default_on", "/ui/images/components/default_input_on.png");
                addImage("selected", "/ui/images/components/selected_input_off.png");
                addImage("active", "/ui/images/components/active_input_off.png");

                addOutputPin("Output", new Point(30,10));
            }
        };
        netlist.put("Standard.Button Source", buttonProps);

        ////////////////////////
        // Output LED
        ////////////////////////
        Properties ledProps = new Properties("Standard.LED", sim.componentLibrary.standard.Output.class){
            // Further Options
            {
                setVisualComponentClass(ui.components.standard.LED.class);

                addAttribute(new SelectionAttribute("Colour", new String[]{"Yellow","Red","Green"}));

                addImage("default", "/ui/images/components/default_led.png");
                addImage("selected", "/ui/images/components/selected_led.png");
                addImage("active", "/ui/images/components/active_led.png");
                addImage("yellow", "/ui/images/components/default_led_on_yellow.png");
                addImage("red", "/ui/images/components/default_led_on_red.png");
                addImage("green", "/ui/images/components/default_led_on_green.png");

                addInputPin("Input", new Point(10,10));
            }
        };
        netlist.put("Standard.LED", ledProps);

        ////////////////////////
        // Pin Logger
        ////////////////////////
        Properties loggerProps = new Properties("Standard.Output Logger", sim.componentLibrary.standard.Output.class){
            // Further Options
            {
                setVisualComponentClass(ui.components.standard.PinLogger.class);

                addImage("default", "/ui/images/components/default_logger.png");
                addImage("selected", "/ui/images/components/selected_logger.png");
                addImage("active", "/ui/images/components/active_logger.png");

                addInputPin("Input", new Point(10,30));
            }
        };
        netlist.put("Standard.Output Logger", loggerProps);

        ////////////////////////
        // Seven Segment Display
        ////////////////////////
        Properties sevenSegProps = new Properties("Standard.7 Segment Display", sim.componentLibrary.standard.Output.class){
            // Further Options
            {
                setVisualComponentClass(ui.components.standard.SevenSegmentDisplay.class);

                addImage("default", "/ui/images/components/default_7seg.png");
                addImage("selected", "/ui/images/components/selected_7seg.png");
                addImage("active", "/ui/images/components/active_7seg.png");
                addImage("A", "/ui/images/components/default_7seg_A.png");
                addImage("B", "/ui/images/components/default_7seg_B.png");
                addImage("C", "/ui/images/components/default_7seg_C.png");
                addImage("D", "/ui/images/components/default_7seg_D.png");
                addImage("E", "/ui/images/components/default_7seg_E.png");
                addImage("F", "/ui/images/components/default_7seg_F.png");
                addImage("G", "/ui/images/components/default_7seg_G.png");
                addImage("DP", "/ui/images/components/default_7seg_DP.png");

                addInputPin("Input", new Point(10,30));
            }
        };
        netlist.put("Standard.7 Segment Display", sevenSegProps);

        ////////////////////////
        // Output Buzzer
        ////////////////////////
        Properties buzzerProps = new Properties("Standard.Buzzer", sim.componentLibrary.standard.Output.class){
            // Further Options
            {
                setVisualComponentClass(ui.components.standard.Buzzer.class);

                addImage("default", "/ui/images/components/default_buzzer.png");
                addImage("selected", "/ui/images/components/selected_buzzer.png");
                addImage("active", "/ui/images/components/active_buzzer.png");

                addInputPin("Input", new Point(10,10));
            }
        };
        netlist.put("Standard.Buzzer", buzzerProps);
    }

    
}
