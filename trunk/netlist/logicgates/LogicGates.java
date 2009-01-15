package netlist.logicgates;

import netlist.*;

/**
 *
 * @author matt
 */
public class LogicGates extends Netlist{

    @Override
    protected void setClassMappings() {
        putClass("Logic Gates.2 Input.AND",   netlist.logicgates.AndGate2Input.class);
        putClass("Logic Gates.2 Input.OR",    netlist.logicgates.OrGate2Input.class);
        putClass("Logic Gates.2 Input.NAND",  netlist.logicgates.NandGate2Input.class);
        putClass("Logic Gates.2 Input.NOR",   netlist.logicgates.NorGate2Input.class);
        putClass("Logic Gates.3 Input.AND",   netlist.logicgates.AndGate3Input.class);
        
    }

    @Override
    protected void setImageMappings() {
        // Default Images
        putImage("Logic Gates.2 Input.AND.default",   "/ui/images/components/default_2in_and.png");
        putImage("Logic Gates.2 Input.OR.default",    "/ui/images/components/default_2in_or.png");
        putImage("Logic Gates.2 Input.NAND.default",  "/ui/images/components/default_2in_nand.png");
        putImage("Logic Gates.2 Input.NOR.default",   "/ui/images/components/default_2in_nor.png");
        putImage("Logic Gates.3 Input.AND.default",   "/ui/images/components/default_3in_and.png");
        
        // Active Images
        putImage("Logic Gates.2 Input.AND.active",   "/ui/images/components/active_2in_and.png");
        putImage("Logic Gates.2 Input.OR.active",    "/ui/images/components/active_2in_or.png");
        putImage("Logic Gates.2 Input.NAND.active",  "/ui/images/components/active_2in_nand.png");
        putImage("Logic Gates.2 Input.NOR.active",   "/ui/images/components/active_2in_nor.png");
        putImage("Logic Gates.3 Input.AND.active",   "/ui/images/components/active_3in_and.png");
        
        // Selected Images
        putImage("Logic Gates.2 Input.AND.selected",   "/ui/images/components/selected_2in_and.png");
        putImage("Logic Gates.2 Input.OR.selected",    "/ui/images/components/selected_2in_or.png");
        putImage("Logic Gates.2 Input.NAND.selected",  "/ui/images/components/selected_2in_nand.png");
        putImage("Logic Gates.2 Input.NOR.selected",   "/ui/images/components/selected_2in_nor.png");
        putImage("Logic Gates.3 Input.AND.selected",   "/ui/images/components/selected_3in_and.png");
        
    }

    @Override
    protected void setLogicMappings() {
        putLogicClass("Logic Gates.2 Input.AND",   sim.componentLibrary.AndGate2Input.class);
        putLogicClass("Logic Gates.2 Input.XOR",   sim.componentLibrary.AndGate2Input.class);
    }

}
