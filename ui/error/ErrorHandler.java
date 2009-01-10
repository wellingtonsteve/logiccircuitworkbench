package ui.error;

import java.util.LinkedList;

/**
 *
 * @author matt
 */
public class ErrorHandler {

    private static LinkedList<ErrorListener> errorlisteners = new LinkedList<ErrorListener>();
    
    public static void addErrorListener(ErrorListener el){
        errorlisteners.add(el);
    }
    
    public static void newError(Error e){
        for(ErrorListener el: errorlisteners){
            el.reportError(e);
        }
    }
    
}
