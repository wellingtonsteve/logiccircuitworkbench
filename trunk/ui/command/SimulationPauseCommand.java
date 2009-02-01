package ui.command;

import ui.Editor;

/**
 *
 * @author matt
 */
public class SimulationPauseCommand extends Command {

    public SimulationPauseCommand(CommandHistory cmdHist) {
        super(cmdHist);
    }

    @Override
    protected void perform(Editor editor) {
        activeCircuit.getSimulator().pause();
    }

    @Override
    public String getName() {
        return "Pause";
    }

}
