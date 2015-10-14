package ui.error;

import ui.command.CommandStage;

/**
 *
 * @author matt
 */
public interface ErrorListener {
    public void reportError(Error error);    
    public void statusChange(CommandStage stage, Object value);    
}
