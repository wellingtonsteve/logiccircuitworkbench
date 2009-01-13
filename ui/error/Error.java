/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.error;

/**
 *
 * @author matt
 */
public class Error {
    private String title;
    private String message;

    public Error(String title, String message){
        this.title = title;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }
}
