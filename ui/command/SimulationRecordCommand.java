package ui.command;

import ui.Editor;
import ui.log.ViewerWindow;

/**
 *
 * @author matt
 */
public class SimulationRecordCommand extends Command {
    ViewerWindow logger;

    public SimulationRecordCommand(CommandHistory cmdHist, ViewerWindow loggerWindow) {
        super(cmdHist);
        logger = loggerWindow;
        logger.setLocationRelativeTo(activeCircuit);        
    }

    @Override
    protected void perform(Editor editor) {        
        if(logger.isShowing()){
            logger.setVisible(false);
            logger.dispose();
        } else {
            logger.setLocationRelativeTo(activeCircuit);   
            logger.setVisible(true);
        }
    }

    @Override
    public String getName() {
        return "Record";
    }

}
