package ui.error;

/**
 * Data class relating to an error as reported by an error handler to an 
 * error listener.
 * @author matt
 */
public class Error {
    private String title;
    private String message;
    private Exception exception = null;

    public Error(String title, String message){
        this.title = title;
        this.message = message;
    }
    
    public Error(String title, String message, Exception exception){
        this.title = title;
        this.message = message;
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }
    
    public boolean hasException(){
        return exception != null;
    }
    
    public Exception getException() {
        return exception;
    }
}
