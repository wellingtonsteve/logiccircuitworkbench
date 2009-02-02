package ui.command;

import ui.Editor;

/**
 *
 * @author matt
 */
public class SimulationStopCommand extends Command {
    
    @Override
    protected void perform(Editor editor) {
        activeCircuit.getSimulator().stop();
    }

    @Override
    public String getName() {
        return "Stop";
    }

}
