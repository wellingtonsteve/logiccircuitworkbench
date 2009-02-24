package netlist;

import java.awt.Point;
import netlist.properties.Properties;

/**
 *
 * @author Matt
 */
public class LogicGates extends Netlist{
    public LogicGates() {
        netlist.put("Logic Gates.2 Input.AND", And2Props.class);
        netlist.put("Logic Gates.2 Input.OR", Or2Props.class);
        netlist.put("Logic Gates.2 Input.NAND", Nand2Props.class);
        netlist.put("Logic Gates.2 Input.NOR", Nor2Props.class);   
        netlist.put("Logic Gates.2 Input.NOT", NotProps.class);   
        netlist.put("Logic Gates.2 Input.XOR", Xor2Props.class);   
        netlist.put("Logic Gates.3 Input.AND", And3Props.class);   
        netlist.put("Logic Gates.3 Input.OR", Or3Props.class);   
    }
    
    ////////////////////////
    // And Gate (2 Input)
    ////////////////////////
    public class And2Props extends Properties{
        public And2Props(String key){
            super(key);
            setLogicalComponentClass(sim.componentLibrary.logicgates.AndGate2Input.class);
            setVisualComponentClass(ui.components.logicgates.DefaultLogicGate.class);

            addImage("default", "/ui/images/components/default_2in_and.png");
            addImage("selected", "/ui/images/components/selected_2in_and.png");
            addImage("active", "/ui/images/components/active_2in_and.png");

            addInputPin("Input 1", new Point(10,20));
            addInputPin("Input 2", new Point(10,40));

            addOutputPin("Output", new Point(60,30));
        }
    }
    
    ////////////////////////
    // Or Gate (2 Input)
    ////////////////////////
    public class Or2Props extends Properties{
        public Or2Props(String key){
            super(key);
            setLogicalComponentClass(sim.componentLibrary.logicgates.OrGate2Input.class);
            setVisualComponentClass(ui.components.logicgates.DefaultLogicGate.class);

            addImage("default", "/ui/images/components/default_2in_or.png");
            addImage("selected", "/ui/images/components/selected_2in_or.png");
            addImage("active", "/ui/images/components/active_2in_or.png");

            addInputPin("Input 1", new Point(10,20));
            addInputPin("Input 2", new Point(10,40));

            addOutputPin("Output", new Point(60,30));
        }
    }
    
    ////////////////////////
    // Nand Gate (2 Input)
    ////////////////////////
    public class Nand2Props extends Properties{
        public Nand2Props(String key){
            super(key);
            setLogicalComponentClass(sim.componentLibrary.logicgates.NandGate2Input.class);
            setVisualComponentClass(ui.components.logicgates.DefaultLogicGate.class);

            addImage("default", "/ui/images/components/default_2in_nand.png");
            addImage("selected", "/ui/images/components/selected_2in_nand.png");
            addImage("active", "/ui/images/components/active_2in_nand.png");

            addInputPin("Input 1", new Point(10,20));
            addInputPin("Input 2", new Point(10,40));

            addOutputPin("Output", new Point(60,30));
        }
    }
   
    ////////////////////////
    // Nor Gate (2 Input)
    ////////////////////////
    public class Nor2Props extends Properties{
        public Nor2Props(String key){
            super(key);
            setLogicalComponentClass(sim.componentLibrary.logicgates.NorGate2Input.class);
            setVisualComponentClass(ui.components.logicgates.DefaultLogicGate.class);

            addImage("default", "/ui/images/components/default_2in_nor.png");
            addImage("selected", "/ui/images/components/selected_2in_nor.png");
            addImage("active", "/ui/images/components/active_2in_nor.png");

            addInputPin("Input 1", new Point(10,20));
            addInputPin("Input 2", new Point(10,40));

            addOutputPin("Output", new Point(60,30));
        }
    }
    
    ////////////////////////
    // And Gate (3 Input)
    ////////////////////////
    public class And3Props extends Properties{
        public And3Props(String key){
            super(key);
            setLogicalComponentClass(sim.componentLibrary.logicgates.AndGate2Input.class);
            setVisualComponentClass(ui.components.logicgates.DefaultLogicGate.class);

            addImage("default", "/ui/images/components/default_2in_and.png");
            addImage("selected", "/ui/images/components/selected_2in_and.png");
            addImage("active", "/ui/images/components/active_2in_and.png");

            addInputPin("Input 1", new Point(10,20));
            addInputPin("Input 2", new Point(10,30));
            addInputPin("Input 2", new Point(10,40));

            addOutputPin("Output", new Point(60,30));
        }
    }

    ////////////////////////
    // Not Gate
    ////////////////////////    
    public class NotProps extends Properties{
        public NotProps(String key){
            super(key);
            setLogicalComponentClass(sim.componentLibrary.logicgates.AndGate2Input.class);
            setVisualComponentClass(ui.components.logicgates.DefaultLogicGate.class);

            addImage("default", "/ui/images/components/default_not.png");
            addImage("selected", "/ui/images/components/selected_not.png");
            addImage("active", "/ui/images/components/active_not.png");

            addInputPin("Input 1", new Point(10,30));
            addOutputPin("Output", new Point(60,30));
        }
    }
    

    ////////////////////////
    // Xor Gate
    ////////////////////////  
    public class Xor2Props extends Properties{
        public Xor2Props(String key){
            super(key);
            setLogicalComponentClass(sim.componentLibrary.logicgates.AndGate2Input.class);
            setVisualComponentClass(ui.components.logicgates.DefaultLogicGate.class);

            addImage("default", "/ui/images/components/default_xor.png");
            addImage("selected", "/ui/images/components/selected_xor.png");
            addImage("active", "/ui/images/components/active_xor.png");

            addInputPin("Input 1", new Point(10,20));
            addInputPin("Input 2", new Point(10,40));

            addOutputPin("Output", new Point(60,30));
        }
    }
    
    ////////////////////////
    // Or Gate (3 Input)
    ////////////////////////  
    public class Or3Props extends Properties{
        public Or3Props(String key){
            super(key);
            setLogicalComponentClass(sim.componentLibrary.logicgates.AndGate2Input.class);
            setVisualComponentClass(ui.components.logicgates.DefaultLogicGate.class);

            addImage("default", "/ui/images/components/default_2in_or.png");
            addImage("selected", "/ui/images/components/selected_2in_or.png");
            addImage("active", "/ui/images/components/active_2in_or.png");

            addInputPin("Input 1", new Point(10,20));
            addInputPin("Input 2", new Point(10,30));
            addInputPin("Input 2", new Point(10,40));

            addOutputPin("Output", new Point(60,30));
        }
    }
}