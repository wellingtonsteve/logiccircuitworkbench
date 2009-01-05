package ui.netlist;

/**
 *
 * @author matt
 */
public class Standard extends Netlist{

    @Override
    protected void setMappings() {
        put("Components.Standard.Button Source",    ui.tools.UITool.Input);
        put("Components.Standard.LED",              ui.tools.UITool.LED);
        put("Components.Logic Gates.2 Input.AND",   ui.tools.UITool.AndGate2Input);
        put("Components.Logic Gates.2 Input.OR",    ui.tools.UITool.OrGate2Input);
        put("Components.Logic Gates.2 Input.NOR",   ui.tools.UITool.NorGate2Input);
        put("Components.Logic Gates.2 Input.NAND",  ui.tools.UITool.NandGate2Input);
        put("Components.Logic Gates.3 Input.AND",   ui.tools.UITool.AndGate3Input);
        
    }    

}
