package ui.command;

import ui.Editor;

/**
 *
 * @author matt
 */
public class SelectionPasteCommand extends Command {

    @Override
    protected void perform(Editor editor) {
        activeCircuit.addComponentList(editor.getLastClipboardItem());
    }

    @Override
    public String toString() {
        return "Paste";
    }

}
