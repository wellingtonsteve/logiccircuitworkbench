package ui.command;

import ui.Editor;

/**
 *
 * @author matt
 */
public class SimulationStartCommand extends Command {
        
    public SimulationStartCommand(CommandHistory cmdHist) {
        super(cmdHist);
    }

    @Override
    protected void perform(Editor editor) {
       activeCircuit.getSimulator().play();
    }

    @Override
    public String getName() {
        return "Simulation Started";
    }

}
