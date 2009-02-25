package netlist;

import java.awt.Point;
import netlist.properties.Properties;

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
    // And Gate (2 Input)
    ////////////////////////
    public class DTypeProps extends Properties{
        public DTypeProps(String key){
            super(key);
            setLogicalComponentClass(sim.componentLibrary.logicgates.AndGate2Input.class);
            setVisualComponentClass(ui.components.flipflops.DefaultFlipFlop.class);

            addImage("default", "/ui/images/components/default_d_flipflop.png");
            addImage("selected", "/ui/images/components/selected_d_flipflop.png");
            addImage("active", "/ui/images/components/active_d_flipflop.png");

            addInputPin("Input 1", new Point(5,20));
            addInputPin("Input 2", new Point(5,50));

            addOutputPin("Output", new Point(55,20));
            addOutputPin("Output", new Point(55,20));
        }
    }
    
    public class JKTypeProps extends Properties{
        public JKTypeProps(String key){
            super(key);
            setLogicalComponentClass(sim.componentLibrary.logicgates.AndGate2Input.class);
            setVisualComponentClass(ui.components.flipflops.DefaultFlipFlop.class);

            addImage("default", "/ui/images/components/default_jk_flipflop.png");
            addImage("selected", "/ui/images/components/selected_jk_flipflop.png");
            addImage("active", "/ui/images/components/active_jk_flipflop.png");

            addInputPin("Input 1", new Point(5,20));
            addInputPin("Input 1", new Point(5,35));
            addInputPin("Input 2", new Point(5,50));

            addOutputPin("Output", new Point(55,20));
            addOutputPin("Output", new Point(55,20));
        }
    }
    
    public class SRProps extends Properties{
        public SRProps(String key){
            super(key);
            setLogicalComponentClass(sim.componentLibrary.logicgates.AndGate2Input.class);
            setVisualComponentClass(ui.components.flipflops.DefaultLatch.class);

            addImage("default", "/ui/images/components/default_sr_latch.png");
            addImage("selected", "/ui/images/components/selected_sr_latch.png");
            addImage("active", "/ui/images/components/active_sr_latch.png");

            addInputPin("Input 1", new Point(15,20));
            addInputPin("Input 2", new Point(15,40));

            addOutputPin("Output", new Point(65,20));
            addOutputPin("Output", new Point(65,20));
        }
    }
}