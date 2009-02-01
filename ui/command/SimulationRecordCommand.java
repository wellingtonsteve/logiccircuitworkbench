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
        ui.log.ViewerWindow logger = new ui.log.ViewerWindow();
        logger.setLocationRelativeTo(editor);
        logger.setVisible(true);
    }

    @Override
    public String getName() {
        return "Record";
    }

}
