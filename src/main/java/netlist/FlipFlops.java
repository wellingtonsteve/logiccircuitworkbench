package netlist;

import netlist.properties.Properties;
import netlist.properties.SpinnerAttribute;
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
            addAttribute(new SpinnerAttribute("Propagation delay (ns)",5, 1, 1000, 1));
            
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
            setLogicalComponentClass(sim.componentLibrary.flipflops.JKType.class);
            setVisualComponentClass(ui.components.flipflops.DefaultFlipFlop.class);

            addAttribute(new TextAttribute("Label", ""));
            addAttribute(new SpinnerAttribute("Propagation delay (ns)",5, 1, 1000, 1));
                        
            addImage("default", "/ui/images/components/default_jk_flipflop.png");
            addImage("selected", "/ui/images/components/selected_jk_flipflop.png");
            addImage("active", "/ui/images/components/active_jk_flipflop.png");

            addInputPin("J", ComponentEdge.West, 1);
            addInputPin("Clock", ComponentEdge.West, 4);
            addInputPin("K", ComponentEdge.West, 7);

            addInputPin("Set", ComponentEdge.North, 2);
            addInputPin("Reset", ComponentEdge.South, 2);

            addOutputPin("Q", ComponentEdge.East, 1);
            addOutputPin("NotQ", ComponentEdge.East, 7);
        }
    }
}