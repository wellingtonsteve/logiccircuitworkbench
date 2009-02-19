package netlist;

import java.awt.Point;
import netlist.properties.BooleanAttribute;
import netlist.properties.Properties;
import netlist.properties.SelectionAttribute;

/**
 *
 * @author Matt
 */
public class Standard extends Netlist{

    public Standard(){
        ////////////////////////
        // Input (Button Source)
        ////////////////////////
        Properties buttonProps = new Properties("Standard.Button Source", sim.componentLibrary.standard.Input.class){     
            // Further Options
            {
                setVisualComponentClass(ui.components.standard.Input.class);
                
                addAttribute(new BooleanAttribute("Is On?", false));
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
    }

    
}
