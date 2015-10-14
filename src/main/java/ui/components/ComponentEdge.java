package ui.components;

import ui.error.ErrorHandler;

/**
 *
 * @author matt
 */
public enum ComponentEdge {
    North, East, South, West;
    
    public static ComponentEdge convertStringToEdge(String str){
        if(str.equals("North")){
            return North;
        } else if(str.equals("East")){
            return East;
        } else if(str.equals("South")){
            return South;
        } else if(str.equals("West")){
            return West;
        } else {
            ErrorHandler.newError("Component Creation Error", "Invalid external pin edge location: " + str);
            return null;
        }
    }
}
