package ui.command;

import ui.Editor;

/**
 *
 * @author matt
 */
public class SimulationRecordCommand extends Command {

    public SimulationRecordCommand(CommandHistory cmdHist) {
        super(cmdHist);
    }

    @Override
    protected void perform(Editor editor) {
        ui.error.ErrorHandler.newError(new ui.error.Error("Editor Error","Record Simulation Action is not yet implemented"));
    }

    @Override
    public String getName() {
        return "Record";
    }

}
