package netlist;

import netlist.properties.Properties;
import netlist.properties.TextAttribute;
import ui.components.ComponentEdge;

/**
 *
 * @author Matt
 */
public class FlipFlops extends Netlist{
    public FlipFlops() {
        netlist.put("Flip Flops.D Type", DTypeProps.class);
        netlist.put("Flip Flops.JK Type", JKTypeProps.class);
        netlist.put("Latches.SR Latch", SRProps.class);
    }
    
    ////////////////////////
    // D-Type Flip Flop
    ////////////////////////
    public class DTypeProps extends Properties{
        public DTypeProps(String key){
            super(key);
            setLogicalComponentClass(sim.componentLibrary.flipflops.DType.class);
            setVisualComponentClass(ui.components.flipflops.DefaultFlipFlop.class);

            addAttribute(new TextAttribute("Label", ""));
            
            addImage("default", "/ui/images/components/default_d_flipflop.png");
            addImage("selected", "/ui/images/components/selected_d_flipflop.png");
            addImage("active", "/ui/images/components/active_d_flipflop.png");

            addInputPin("D", ComponentEdge.West, 1);
            addInputPin("Clock", ComponentEdge.West, 7);
            
            addInputPin("Set", ComponentEdge.North, 2);
            addInputPin("Reset", ComponentEdge.South, 2);

            addOutputPin("Q", ComponentEdge.East, 1);
            addOutputPin("NotQ", ComponentEdge.East, 7);
        }
    }
    
    public class JKTypeProps extends Properties{
        public JKTypeProps(String key){
            super(key);
            setLogicalComponentClass(sim.componentLibrary.logicgates.AndGate2Input.class);
            setVisualComponentClass(ui.components.flipflops.DefaultFlipFlop.class);

            addAttribute(new TextAttribute("Label", ""));
                        
            addImage("default", "/ui/images/components/default_jk_flipflop.png");
            addImage("selected", "/ui/images/components/selected_jk_flipflop.png");
            addImage("active", "/ui/images/components/active_jk_flipflop.png");

//            addInputPin("Input 1", new Point(5,20));
//            addInputPin("Input 1", new Point(5,35));
//            addInputPin("Input 2", new Point(5,50));
//
//            addOutputPin("Output", new Point(55,20));
//            addOutputPin("Output", new Point(55,20));
        }
    }
    
    public class SRProps extends Properties{
        public SRProps(String key){
            super(key);
            setLogicalComponentClass(sim.componentLibrary.logicgates.AndGate2Input.class);
            setVisualComponentClass(ui.components.flipflops.DefaultLatch.class);
            
            addAttribute(new TextAttribute("Label", ""));
            
            addImage("default", "/ui/images/components/default_sr_latch.png");
            addImage("selected", "/ui/images/components/selected_sr_latch.png");
            addImage("active", "/ui/images/components/active_sr_latch.png");

//            addInputPin("Input 1", new Point(15,20));
//            addInputPin("Input 2", new Point(15,40));
//
//            addOutputPin("Output", new Point(65,20));
//            addOutputPin("Output", new Point(65,20));
        }
    }
}