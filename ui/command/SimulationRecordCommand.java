package ui.command;

import ui.Editor;
import ui.components.standard.log.ViewerWindow;

/**
 *
 * @author matt
 */
public class SimulationRecordCommand extends Command {
    private ViewerWindow logger;

    @Override
    protected void perform(Editor editor) {
        this.logger = activeCircuit.getLoggerWindow();
        logger.setLocationRelativeTo(activeCircuit);
        logger.setVisible(!logger.isVisible());
    }

    @Override
    public String getName() {
        return "Record";
    }

}
