package ui.error;

/**
 *
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
}
