package ui.command;

import ui.Editor;

/**
 *
 * @author matt
 */
public class SelectionPasteCommand extends Command {

    @Override
    protected void perform(Editor editor) {
        ui.error.ErrorHandler.newError(new ui.error.Error("Editor Error","Paste Action is not yet implemented"));
    }

    @Override
    public String toString() {
        return "Paste";
    }

}
