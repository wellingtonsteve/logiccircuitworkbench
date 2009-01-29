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
        ui.error.ErrorHandler.newError(new ui.error.Error("Editor Error","Pause Simulation Action is not yet implemented"));
    }

    @Override
    public String getName() {
        return "Pause";
    }

}
