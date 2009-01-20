package ui.command;

import ui.Editor;

/**
 *
 * @author matt
 */
public class NewCircuitCommand extends Command{
    
    @Override
    protected void perform(Editor editor) {
        editor.createBlankCircuit();
    }

    @Override
    public String toString() {
        return "Create blank circuit";
    }

}