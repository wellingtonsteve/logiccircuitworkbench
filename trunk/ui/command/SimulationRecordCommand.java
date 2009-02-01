package ui.command;

import ui.Editor;

/**
 *
 * @author matt
 */
public class SimulationRecordCommand extends Command {
    ui.log.ViewerWindow logger;

    public SimulationRecordCommand(CommandHistory cmdHist) {
        super(cmdHist);
        logger = new ui.log.ViewerWindow(activeCircuit);
        logger.setLocationRelativeTo(activeCircuit);
    }

    @Override
    protected void perform(Editor editor) {        
        logger.setVisible(true);
    }

    @Override
    public String getName() {
        return "Record";
    }

}
