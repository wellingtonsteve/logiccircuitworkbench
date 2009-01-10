package ui.command;

import ui.Editor;

/**
 *
 * @author matt
 */
public class InsertSubcomponentCommand extends Command {

    @Override
    protected void perform(Editor editor) {
        ui.error.ErrorHandler.newError(new ui.error.Error("Editor Error","Insert subcomponent action is not yet implemented"));
    }

    @Override
    public String toString() {
        return "Insert subcomponent";
    }

}
