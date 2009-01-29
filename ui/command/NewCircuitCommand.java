package ui.command;

import ui.Editor;

/**
 *
 * @author matt
 */
public class NewCircuitCommand extends Command{

    public NewCircuitCommand(CommandHistory cmdHist) {
        super(cmdHist);
    }
    
    @Override
    protected void perform(Editor editor) {
        editor.createBlankCircuit();
    }

    @Override
    public String getName() {
        return "Create blank circuit";
    }

}
