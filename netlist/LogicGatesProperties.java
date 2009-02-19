package netlist;

import java.awt.Point;
import netlist.properties.Properties;

/**
 *
 * @author Matt
 */
public class LogicGatesProperties extends NetlistProperties{

    public LogicGatesProperties(){
        ////////////////////////
        // And Gate (2 Input)
        ////////////////////////
        Properties buttonProps = new Properties("Logic Gates.2 Input.AND", sim.componentLibrary.logicgates.AndGate2Input.class){     
            // Further Options
            {
                setVisualComponentClass(ui.components.logicgates.AndGate2Input.class);
                
                addImage("default", "/ui/images/components/default_2in_and.png");
                addImage("selected", "/ui/images/components/selected_2in_and.png");
                addImage("active", "/ui/images/components/active_2in_and.png");
                
                addInputPin("Input 1", new Point(10,20));         
                addInputPin("Input 2", new Point(10,40));         
                
                addOutputPin("Output", new Point(60,30));                
            }            
        };      
        netlist.put("Standard.Button Source", buttonProps);
        
    }

    
}
