package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

/**
 *
 * @author Matt
 */
public class UIConstants {
    
    public static final Color CIRCUIT_BACKGROUND_COLOUR = Color.WHITE;
    public static final Color GRID_DOT_COLOUR = Color.GRAY;
    
    public static final int GRID_DOT_SPACING = 10; // Changing this value will require the substantial changes to the component images and classes
    public static final boolean SNAP_TO_GRID = true;

    public static Color SELECTION_BOX_COLOUR = Color.BLACK;
    public static Stroke SELECTION_BOX_STROKE = new BasicStroke(1.0f, // line width
              /* cap style */BasicStroke.CAP_BUTT,
              /* join style, miter limit */BasicStroke.JOIN_BEVEL, 1.0f,
              /* the dash pattern */new float[] { 8.0f, 8.0f },
              /* the dash phase */0.0f); /* on 8, off 8*/
    
    public static final boolean SHOW_CONNECTION_POINTS = false;
    public static final Color CONNECTION_POINT_COLOUR = Color.RED;
    public static Stroke CONNECTED_POINT_STROKE = new BasicStroke(2.0f); 
    
    public static final boolean DO_SYSTEM_BEEP = true;
    public static void beep(){
        java.awt.Toolkit.getDefaultToolkit ().beep();
    }
    
    public static boolean SHOW_WIRE_WAYPOINTS = true;
    public static Color WIRE_WAYPOINT_COLOUR = Color.GREEN;
    public static int WIRE_HOVER_THICKNESS = 2;
    public static final Color DEFAULT_WIRE_COLOUR = Color.BLACK;
    public static final Color ACTIVE_WIRE_COLOUR = Color.BLUE;
    public static final Color HOVER_WIRE_COLOUR = Color.RED;
    
    

}

