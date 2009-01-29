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
    
    public static void newError(String title, String message){
        newError(new Error(title, message));
    }
    
    public static void newError(String title, String message, Exception exception){
        newError(new Error(title, message, exception));
    }
    
    public static void changeStatus(String stage, Object value){
        if(!errorlisteners.isEmpty()){
            for(ErrorListener el: errorlisteners){
                el.statusChange(stage, value);
            }
        } 
    }
    
}
