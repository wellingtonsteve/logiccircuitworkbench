package netlist;

import netlist.properties.Properties;
import netlist.properties.SpinnerAttribute;
import netlist.properties.TextAttribute;
import ui.components.ComponentEdge;

/**
 *
 * @author Matt
 */
public class Latches extends Netlist{
    public Latches() {
        netlist.put("Latches.SR Latch", SRProps.class);
    }

    public class SRProps extends Properties{
        public SRProps(String key){
            super(key);
            setLogicalComponentClass(sim.componentLibrary.latches.SRLatch.class);
            setVisualComponentClass(ui.components.flipflops.DefaultLatch.class);

            addAttribute(new TextAttribute("Label", ""));
            addAttribute(new SpinnerAttribute("Propagation delay (ns)",5, 1, 1000, 1));

            addImage("default", "/ui/images/components/default_sr_latch.png");
            addImage("selected", "/ui/images/components/selected_sr_latch.png");
            addImage("active", "/ui/images/components/active_sr_latch.png");

            addInputPin("Set", ComponentEdge.West, 1);
            addInputPin("Reset", ComponentEdge.West, 7);

            addOutputPin("Q", ComponentEdge.East, 1);
            addOutputPin("NotQ", ComponentEdge.East, 7);
        }
    }
}