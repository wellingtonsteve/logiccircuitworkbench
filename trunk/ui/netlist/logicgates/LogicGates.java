package ui.netlist.logicgates;

import ui.netlist.*;

/**
 *
 * @author matt
 */
public class LogicGates extends Netlist{

    @Override
    protected void setClassMappings() {
        putClass("Logic Gates.2 Input.AND",   ui.netlist.logicgates.AndGate2Input.class);
        putClass("Logic Gates.2 Input.OR",    ui.netlist.logicgates.OrGate2Input.class);
        putClass("Logic Gates.2 Input.NAND",  ui.netlist.logicgates.NandGate2Input.class);
        putClass("Logic Gates.2 Input.NOR",   ui.netlist.logicgates.NorGate2Input.class);
        putClass("Logic Gates.3 Input.AND",   ui.netlist.logicgates.AndGate3Input.class);
        
    }

    @Override
    protected void setImageMappings() {
        // Active Images
        putImage("Logic Gates.2 Input.AND.default",   "build/classes/ui/images/components/default_2in_and.png");
        putImage("Logic Gates.2 Input.OR.default",    "build/classes/ui/images/components/default_2in_or.png");
        putImage("Logic Gates.2 Input.NAND.default",  "build/classes/ui/images/components/default_2in_nand.png");
        putImage("Logic Gates.2 Input.NOR.default",   "build/classes/ui/images/components/default_2in_nor.png");
        putImage("Logic Gates.3 Input.AND.default",   "build/classes/ui/images/components/default_3in_and.png");
        
        // Active Images
        putImage("Logic Gates.2 Input.AND.active",   "build/classes/ui/images/components/active_2in_and.png");
        putImage("Logic Gates.2 Input.OR.active",    "build/classes/ui/images/components/active_2in_or.png");
        putImage("Logic Gates.2 Input.NAND.active",  "build/classes/ui/images/components/active_2in_nand.png");
        putImage("Logic Gates.2 Input.NOR.active",   "build/classes/ui/images/components/active_2in_nor.png");
        putImage("Logic Gates.3 Input.AND.active",   "build/classes/ui/images/components/active_3in_and.png");
        
        // Selected Images
        putImage("Logic Gates.2 Input.AND.selected",   "build/classes/ui/images/components/selected_2in_and.png");
        putImage("Logic Gates.2 Input.OR.selected",    "build/classes/ui/images/components/selected_2in_or.png");
        putImage("Logic Gates.2 Input.NAND.selected",  "build/classes/ui/images/components/selected_2in_nand.png");
        putImage("Logic Gates.2 Input.NOR.selected",   "build/classes/ui/images/components/selected_2in_nor.png");
        putImage("Logic Gates.3 Input.AND.selected",   "build/classes/ui/images/components/selected_3in_and.png");
        
    }

}
