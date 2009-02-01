/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim.testing;

import sim.LogicState;
import sim.SimItem;
import sim.Simulator;
import sim.SimulatorState;
import sim.SimulatorStateListener;
import sim.componentLibrary.Circuit;
import sim.componentLibrary.Wire;
import sim.componentLibrary.standard.Input;
import sim.pin.InputPin;
import sim.pin.OutputPin;
import sim.pin.Pin;
import sim.pin.ValueListener;
import ui.log.PinLogger;
import ui.log.ViewerWindow;
/**
 *
 * @author Stephen
 */
public class NewClass2 {

public static void main(String[] args){
    Circuit circuit = new Circuit();
    SimItem and1 = new sim.componentLibrary.logicgates.OrGate2Input();
    SimItem in1 = new sim.componentLibrary.standard.Input();    
    SimItem in2 = new sim.componentLibrary.standard.Input();
    SimItem out = new sim.componentLibrary.standard.Output();
    
    circuit.addSimItem(and1);
    circuit.addSimItem(in1);
    circuit.addSimItem(in2);
    circuit.addSimItem(out);
    Simulator sim = new Simulator(circuit);
        
    Wire wire1 = new Wire();
    wire1.connectPin(in1.getPinByName("Output"));
    wire1.connectPin(and1.getPinByName("Input 1"));
    Wire wire2 = new Wire();
    wire2.connectPin(in2.getPinByName("Output"));
    wire2.connectPin(and1.getPinByName("Input 2"));
    Wire wire3 = new Wire();
    wire3.connectPin(out.getPinByName("Input"));
    wire3.connectPin(and1.getPinByName("Output"));
    
    
    //((InputPin)(and1.getPinByName("Input 1"))).connectToOutput((OutputPin)(in1.getPinByName("Output")));
    //((InputPin)(and1.getPinByName("Input 1"))).connectToOutput((OutputPin)(in1.getPinByName("Output")));
    //((InputPin)(out.getPinByName("Input"))).connectToOutput((OutputPin)(and1.getPinByName("Output")));
     
    final Input in1copy = (Input) in1;
    final Input in2copy = (Input) in2;
    sim.addStateListener(new SimulatorStateListener() {

            public void SimulatorStateChanged(SimulatorState state) {
                System.out.println(state.toString());
            }

            public void SimulationTimeChanged(long time) {
                if(time % 300000000 == 0){
                    if(in1copy.getPinByName("Output").getValue() == LogicState.ON){
                        in1copy.setValue(LogicState.OFF);
                    }
                    else{
                        in1copy.setValue(LogicState.ON);
                    }
                }
                
                if(time == 3000000000l)  
                    in2copy.setValue(LogicState.OFF);
            }
        });
    
    if(sim.play()){
        System.out.println("Yes");
    }
    else{
        System.out.println("No");
    }
        
            PinLogger pl = new PinLogger(out.getPinByName("Input"), sim);
        new ViewerWindow(pl).setVisible(true);
    
    
}
    
}
