package ui.netlist;

/**
 *
 * @author matt
 */
public class LogicGates extends Netlist{

    @Override
    protected void setMappings() {
//        put("Logic Gates.2 Input.AND",   ui.tools.AndGate2Input.class);
//        put("Logic Gates.2 Input.OR",    ui.tools.OrGate2Input.class);
//        put("Logic Gates.2 Input.NAND",  ui.tools.NandGate2Input.class);
//        put("Logic Gates.2 Input.NOR",   ui.tools.NorGate2Input.class);
//        put("Logic Gates.3 Input.AND",   ui.tools.AndGate3Input.class);
        put("Logic Gates.2 Input.AND",   "ui.tools.AndGate2Input");
        put("Logic Gates.2 Input.OR",    "ui.tools.OrGate2Input");
        put("Logic Gates.2 Input.NAND",  "ui.tools.NandGate2Input");
        put("Logic Gates.2 Input.NOR",   "ui.tools.NorGate2Input");
        put("Logic Gates.3 Input.AND",   "ui.tools.AndGate3Input");
        
    }    

}
