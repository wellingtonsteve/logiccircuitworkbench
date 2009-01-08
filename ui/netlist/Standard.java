package ui.netlist;

/**
 *
 * @author matt
 */
public class Standard extends Netlist{

    @Override
    protected void setMappings() {
//        put("Standard.Button Source",    ui.tools.Input.class);
//        put("Standard.Wire",             ui.tools.Wire.class);
//        put("Standard.LED",              ui.tools.LED.class);
        put("Standard.Button Source",    "ui.tools.Input");
        put("Standard.Wire",             "ui.tools.Wire");
        put("Standard.LED",              "ui.tools.LED");
       
    }    

}
