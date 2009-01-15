package ui.error;

import java.util.LinkedList;

/**
 *
 * @author matt
 */
public class ErrorHandler {

    private static LinkedList<ErrorListener> errorlisteners = new LinkedList<ErrorListener>();
    private static LinkedList<Error> unreportedErrors = new LinkedList<Error>();
    
    public static void addErrorListener(ErrorListener el){
        if(errorlisteners.isEmpty()){
            for(Error e: unreportedErrors){
                el.reportError(e);
            }
        }
        unreportedErrors.clear();
        errorlisteners.add(el);
    }
    
    public static void newError(Error e){
        if(!errorlisteners.isEmpty()){
            for(ErrorListener el: errorlisteners){
                el.reportError(e);
            }
        } else {
            unreportedErrors.add(e);
        }        
    }
    
}