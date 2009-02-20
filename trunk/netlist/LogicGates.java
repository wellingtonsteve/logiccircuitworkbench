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

        ////////////////////////
        // Nand Gate (2 Input)
        ////////////////////////
        Properties nand2Props = new Properties("Logic Gates.2 Input.NAND", sim.componentLibrary.logicgates.NandGate2Input.class){
            // Further Options
            {
                setVisualComponentClass(ui.components.logicgates.NandGate2Input.class);

                addImage("default", "/ui/images/components/default_2in_nand.png");
                addImage("selected", "/ui/images/components/selected_2in_nand.png");
                addImage("active", "/ui/images/components/active_2in_nand.png");

                addInputPin("Input 1", new Point(10,20));
                addInputPin("Input 2", new Point(10,40));

                addOutputPin("Output", new Point(60,30));
            }
        };
        netlist.put("Logic Gates.2 Input.NAND", nand2Props);

        ////////////////////////
        // Nor Gate (2 Input)
        ////////////////////////
        Properties nor2Props = new Properties("Logic Gates.2 Input.NOR", sim.componentLibrary.logicgates.NorGate2Input.class){
            // Further Options
            {
                setVisualComponentClass(ui.components.logicgates.NorGate2Input.class);

                addImage("default", "/ui/images/components/default_2in_nor.png");
                addImage("selected", "/ui/images/components/selected_2in_nor.png");
                addImage("active", "/ui/images/components/active_2in_nor.png");

                addInputPin("Input 1", new Point(10,20));
                addInputPin("Input 2", new Point(10,40));

                addOutputPin("Output", new Point(60,30));
            }
        };
        netlist.put("Logic Gates.2 Input.NOR", nor2Props);

        ////////////////////////
        // And Gate (3 Input)
        ////////////////////////

        ////////////////////////
        // Not Gate
        ////////////////////////

        ////////////////////////
        // Xor Gate
        ////////////////////////
        
    }

    
}
