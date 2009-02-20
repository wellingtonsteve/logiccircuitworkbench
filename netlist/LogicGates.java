package netlist;

import java.awt.Point;
import netlist.properties.Properties;

/**
 *
 * @author Matt
 */
public class LogicGates extends Netlist{

    public LogicGates(){
        ////////////////////////
        // And Gate (2 Input)
        ////////////////////////
        Properties and2Props = new Properties("Logic Gates.2 Input.AND", sim.componentLibrary.logicgates.AndGate2Input.class){
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
        netlist.put("Logic Gates.2 Input.AND", and2Props);

        ////////////////////////
        // Or Gate (2 Input)
        ////////////////////////
        Properties or2Props = new Properties("Logic Gates.2 Input.OR", sim.componentLibrary.logicgates.OrGate2Input.class){
            // Further Options
            {
                setVisualComponentClass(ui.components.logicgates.OrGate2Input.class);

                addImage("default", "/ui/images/components/default_2in_or.png");
                addImage("selected", "/ui/images/components/selected_2in_or.png");
                addImage("active", "/ui/images/components/active_2in_or.png");

                addInputPin("Input 1", new Point(10,20));
                addInputPin("Input 2", new Point(10,40));

                addOutputPin("Output", new Point(60,30));
            }
        };
        netlist.put("Logic Gates.2 Input.OR", or2Props);
        
    }

    
}
