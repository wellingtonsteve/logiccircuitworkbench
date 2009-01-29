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
        ui.error.ErrorHandler.newError(new ui.error.Error("Editor Error","Step Simulation Action is not yet implemented"));
    }

    @Override
    public String getName() {
        return "Step";
    }

}