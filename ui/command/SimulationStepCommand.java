package ui.command;

import ui.Editor;

/**
 *
 * @author matt
 */
public class SimulationStepCommand extends Command {

    public SimulationStepCommand(CommandHistory cmdHist) {
        super(cmdHist);
    }

    @Override
    protected void perform(Editor editor) {
        activeCircuit.getSimulator().stepthrough();
    }

    @Override
    public String getName() {
        return "Step";
    }

}
