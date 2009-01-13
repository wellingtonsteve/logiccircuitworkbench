package ui.command;

import ui.Editor;

/**
 *
 * @author matt
 */
public class SelectionCutCommand extends Command {

    @Override
    protected void perform(Editor editor) {
        ui.error.ErrorHandler.newError(new ui.error.Error("Editor Error","Cut Action is not yet implemented"));
    }

    @Override
    public String toString() {
        return "Cut";
    }

}
