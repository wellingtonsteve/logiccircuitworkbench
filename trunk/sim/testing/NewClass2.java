package sim.testing;

import java.awt.Point;
import java.util.LinkedList;
import sim.LogicState;
import sim.SimItem;
import sim.Simulator;
import sim.SimulatorState;
import sim.SimulatorStateListener;
import sim.componentLibrary.Circuit;
import sim.joinable.Wire;
import sim.componentLibrary.standard.Input;
import ui.CircuitFrame;
import ui.CircuitPanel;
import ui.components.standard.PinLogger;
import ui.components.standard.log.ViewerFrame;
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
//    wire1.connect(in1.getPinByName("Output"));
//    wire1.connect(and1.getPinByName("Input 1"));
//    Wire wire2 = new Wire();
//    wire2.connect(in2.getPinByName("Output"));
//    wire2.connect(and1.getPinByName("Input 2"));
//    Wire wire3 = new Wire();
//    wire3.connect(out.getPinByName("Input"));
//    wire3.connect(and1.getPinByName("Output"));
     
    final Input in1copy = (Input) in1;
    final Input in2copy = (Input) in2;
    sim.addStateListener(new SimulatorStateListener() {

            public void SimulatorStateChanged(SimulatorState state) {
            }

            public void SimulationTimeChanged(long time) {
                if(time % 300000000 == 0){
                    if(in1copy.getPinByName("Output").getValue() == LogicState.ON){
                        in1copy.setValue(LogicState.OFF);
                    }
                    else{
                        in1copy.setValue(LogicState.ON);
                    }
                } else if(time % 700000000 == 0){
                    if(in2copy.getPinByName("Output").getValue() == LogicState.ON){
                     in2copy.setValue(LogicState.OFF);
                    }
                    else{
                        in2copy.setValue(LogicState.ON);
                    }
                }             
                
                if(time == 3000000000l)  {
                    in2copy.setValue(LogicState.OFF);                   
                }
            }
            
            @Override
            public void SimulationRateChanged(int rate){}  

        });
    
    sim.play();
//    PinLogger pl1 = new PinLogger(null, new Point(0,0), out);
//    PinLogger pl2 = new PinLogger(null, new Point(0,0), in1);
//    PinLogger pl3 = new PinLogger(null, new Point(0,0), in2);
//    LinkedList<PinLogger> coll = new LinkedList<PinLogger>();
//    coll.add(pl1);
//    coll.add(pl2);
//    coll.add(pl3);
//    ViewerFrame vw = new ViewerFrame((CircuitPanel)null);
//    sim.addStateListener(vw.getSimStateListener());
//    vw.addPinLoggers(coll);
//    vw.setVisible(true);
//        
}
    
}
