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
    
    public static final int GRID_DOT_SPACING = 10; // Changing this value will require substantial changes to the component images and classes
    public static final boolean SNAP_TO_GRID = true;
    public static final int GRID_STANDARD_WIDTH = 70;
    public static final int GRID_STANDARD_HEIGHT = 60;
    public static boolean DRAW_GRID_DOTS = true;

    public static Color SELECTION_BOX_COLOUR = Color.BLACK;
    public static Stroke SELECTION_BOX_STROKE = new BasicStroke(1.0f, // line width
              /* cap style */BasicStroke.CAP_BUTT,
              /* join style, miter limit */BasicStroke.JOIN_BEVEL, 1.0f,
              /* the dash pattern */new float[] { 8.0f, 8.0f },
              /* the dash phase */0.0f); /* on 8, off 8*/
    
    public static final boolean SHOW_CONNECTION_POINTS = true;
    public static final Color CONNECTION_POINT_COLOUR = Color.RED;
    public static Stroke CONNECTED_POINT_STROKE = new BasicStroke(2.0f); 
    public static final boolean DO_SYSTEM_BEEP = true;
    
    /** File Format
     *  LCW v1.0  -  Limited Component support. Case analysis on string.
     *  LCW v2.0  -  Netlist support, Component names match keys in netlists. Not backwards compatible
     */
    public static String FILE_FORMAT_VERSION = "LCW v2.0";
    
    
    
    public static int LABEL_CONNECTION_POINT_X_OFFSET = -3;
    public static int LABEL_CONNECTION_POINT_Y_OFFSET = -3;
    public static int LABEL_COMPONENT_X_OFFSET = 10;
    public static int LABEL_COMPONENT_Y_OFFSET = 10;
    public static Color LABEL_TEXT_COLOUR = Color.BLACK;
    public static boolean DO_OFFSCREEN_DRAWING_TEST = true;
    public static int WIRE_HANDLE_LENGTH = 40;


    public static void beep(){
        java.awt.Toolkit.getDefaultToolkit ().beep();
    }

    public static final Color DEFAULT_COMPONENT_COLOUR = Color.BLACK;
    public static final Color ACTIVE_COMPONENT_COLOUR = Color.BLUE;
    public static final Color HOVER_COMPONENT_COLOUR = Color.RED;
    
    
    public static boolean SHOW_WIRE_WAYPOINTS = true;
    public static Color WIRE_WAYPOINT_COLOUR = Color.GREEN;
    public static int WIRE_HOVER_THICKNESS = 2;
    public static final Stroke ACTIVE_WIRE_STROKE = new BasicStroke(5.0f);
    public static final Color HOVER_WIRE_COLOUR = Color.GRAY;
    
    
    public static final int LOG_VIEWER_MARGIN = 20;
    public static final float Y_AXIS_SCALE = 0.1f; // Width of 1 nanosecond on the log graph
    public static final int LOG_HEIGHT = 30; // Height of the graph (i.e. difference between high and low values)
    //public static final 

}

