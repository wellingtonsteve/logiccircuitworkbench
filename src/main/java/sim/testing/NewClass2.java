package sim.testing;


import netlist.properties.*;
import sim.LogicState;
import sim.SimItem;
import sim.Simulator;
import sim.SimulatorState;
import sim.SimulatorStateListener;
import sim.componentLibrary.Circuit;
import sim.joinable.Pin;
import sim.joinable.Wire;
import sim.componentLibrary.standard.Input;
import sim.joinable.Joinable;
import sim.joinable.ValueListener;
/**
 *
 * @author Stephen
 */
public class NewClass2 {
    
    //Some false properties for externability, externa
    static class FalseProps extends Properties{
        TextAttribute labelProp = new TextAttribute("Label", "");
        BooleanAttribute externalProp = new BooleanAttribute("External?", false);
        SpinnerAttribute delayProp = new SpinnerAttribute("Propagation delay (ns)",5, 1, 1000, 1);

        public FalseProps(){ super("");}

        public Attribute getAttribute(String name) {
            if(name.equals("Label")) return labelProp;
            else if(name.equals("External?")) return externalProp;
            else return delayProp;
        }
    }

    public static void main(String[] args){
        //Make a circuit that we'll add components to
        Circuit circuit = new Circuit();
        //Create a simulator and pass the circuit to it
        Simulator sim = new Simulator(circuit);

        //Create some components
        SimItem and1 = new sim.componentLibrary.logicgates.AndGate2Input();
        SimItem in1 = new sim.componentLibrary.standard.Input();  
        SimItem in2 = new sim.componentLibrary.standard.Input();
        SimItem out = new sim.componentLibrary.standard.Output();

        //Give them the false properties
        and1.setProperties(new FalseProps());  
        in1.setProperties(new FalseProps());
        in2.setProperties(new FalseProps());
        out.setProperties(new FalseProps());

        //Add components to the circuit
        circuit.addSimItem(and1);
        circuit.addSimItem(in1);
        circuit.addSimItem(in2);
        circuit.addSimItem(out);

        //Create some wires to connect the Inputs and Output to the AND
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
        Wire wire3 = new Wire();

        //Connect every thing up
        Joinable.connect(in1.getPinByName("Output"), wire1);
        Joinable.connect(and1.getPinByName("Input 1"), wire1);
        Joinable.connect(in2.getPinByName("Output"), wire2);
        Joinable.connect(and1.getPinByName("Input 2"), wire2);
        Joinable.connect(out.getPinByName("Input"), wire3);
        Joinable.connect(and1.getPinByName("Output"), wire3);

        final Input in1copy = (Input) in1;
        final Input in2copy = (Input) in2;

        //Listen to state of simulator
        sim.addStateListener(new SimulatorStateListener() {
                public void SimulatorStateChanged(SimulatorState state) { }
                public void SimulationRateChanged(int rate) { }

                public void SimulationTimeChanged(long time) {
                    System.out.println("Simulator at time " + time);
                    if(time % 300000000 == 0){
                        //Toggle input 1 every 0.3 seconds
                        if(in1copy.getPinByName("Output").getValue() == LogicState.ON){
                            in1copy.setValue(LogicState.OFF);
                        }
                        else{
                            in1copy.setValue(LogicState.ON);
                        }
                        System.out.println("Input 1 changed to " + in1copy.getPinByName("Output").getValue());
                    } else if(time % 700000000 == 0) {
                        //Toggle input 2 every 0.7 seconds
                        if(in2copy.getPinByName("Output").getValue() == LogicState.ON){
                            in2copy.setValue(LogicState.OFF);
                        }
                        else{
                            in2copy.setValue(LogicState.ON);
                        }
                        System.out.println("Input 2 changed to " + in2copy.getPinByName("Output").getValue());
                    }
                }
            });

        //Listen to state of output
        out.getPinByName("Input").addValueListener(new ValueListener() {
            public void valueChanged(Pin pin, LogicState value) {
                System.out.println("Output changed to " + value.toString());
            }
        });

        sim.play();

    }
}
