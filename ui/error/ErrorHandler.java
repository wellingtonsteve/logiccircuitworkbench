package ui.error;

import java.util.LinkedList;

/**
 * An error handler records any error listeners who want to report errors. When 
 * no error listeners have been registered, unreported errors are cached until the
 * first listener is registered. Errors can be reported by any class and they are 
 * all routed through this singleton class.
 * 
 * @author matt
 */
public class ErrorHandler {
    private static LinkedList<ErrorListener> errorlisteners = new LinkedList<ErrorListener>();
    private static LinkedList<Error> unreportedErrors = new LinkedList<Error>();
    
    /** Register an error listener */
    public static void addErrorListener(ErrorListener el){
        if(errorlisteners.isEmpty()){
            for(Error e: unreportedErrors){
                el.reportError(e);
            }
        }
        unreportedErrors.clear();
        errorlisteners.add(el);
    }
    
    /** Report an error */
    public static void newError(Error e){
        if(!errorlisteners.isEmpty()){
            for(ErrorListener el: errorlisteners){
                el.reportError(e);
            }
        } else {
            unreportedErrors.add(e);
        }        
    }
    
    /** Report an error */
    public static void newError(String title, String message){
        newError(new Error(title, message));
    }
    
    /** Report an error */
    public static void newError(String title, String message, Exception exception){
        newError(new Error(title, message, exception));
    }
}
