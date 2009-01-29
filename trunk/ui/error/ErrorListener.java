package ui.error;

/**
 *
 * @author matt
 */
public interface ErrorListener {
    
    public void reportError(Error error);
    
    public void statusChange(String stage, Object value);
    
}
