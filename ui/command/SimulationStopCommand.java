package ui.command;

import ui.Editor;

/**
 *
 * @author matt
 */
public class SimulationStopCommand extends Command {

    @Override
    protected void perform(Editor editor) {
        ui.error.ErrorHandler.newError(new ui.error.Error("Editor Error","Stop Simulation Action is not yet implemented"));
    }

    @Override
    public String toString() {
        return "Stop";
    }

}
