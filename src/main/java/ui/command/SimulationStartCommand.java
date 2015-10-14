package ui.command;

import ui.Editor;

/**
 *
 * @author matt
 */
public class SimulationStartCommand extends Command {

    @Override
    protected void perform(Editor editor) {
       activeCircuit.getSimulator().play();
       activeCircuit.repaint();
    }

    @Override
    public String getName() {
        return "Simulation Started";
    }

}
