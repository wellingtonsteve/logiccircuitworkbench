package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

/**
 * This class simply contains all constants that are used in the UI part of the 
 * application. Options exist for debugging, startup, the grid, selection box,
 * file format, drawing components, drawing labels, drawing wires and the Pin 
 * Logger  
 * 
 * @author Matt
 */
public class UIConstants {    
    // Debugging
    public static final boolean SHOW_GRID_OBJECTS = false;
    public static final boolean SHOW_WIRE_HOVER_BOXES = false;
    public static final boolean SHOW_INVALID_AREA_BOXES = true;
    public static final boolean SHOW_BOUNDING_BOXES = true;
    public static final boolean SHOW_WIRE_WAYPOINTS = false;
    
    // Startup Options
    public static final boolean DO_OFFSCREEN_DRAWING_TEST = true;
    
    // Grid Options
    public static final Color CIRCUIT_BACKGROUND_COLOUR = Color.WHITE;
    public static final Color CIRCUIT_PLAYING_BACKGROUND_COLOUR = new Color(242,255,255);
    public static final Color SUBCIRCUIT_BACKGROUND_COLOUR = new Color(255,255,235);
    public static final Color GRID_DOT_COLOUR = Color.GRAY;    
    public static final int GRID_DOT_SPACING = 5; // Recommended only 2, 5 or 10
    public static final boolean SNAP_TO_GRID = true;
    public static boolean DRAW_GRID_DOTS = false;
    public static boolean DRAW_PIN_LOGIC_VALUES = true;
    public static boolean DRAW_WIRE_CROSSOVERS = false;
    public static final Color CONNECTION_POINT_COLOUR = Color.RED;
    public static final Stroke CONNECTED_POINT_STROKE = new BasicStroke(2.0f); 
    public static final boolean DO_SYSTEM_BEEP = true;
    public static final void beep(){
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    // Mouse Drag Selection box options
    public static final Color SELECTION_BOX_COLOUR = Color.BLACK;
    public static final Stroke SELECTION_BOX_STROKE = new BasicStroke(1.0f, // line width
              /* cap style */BasicStroke.CAP_BUTT,
              /* join style, miter limit */BasicStroke.JOIN_BEVEL, 1.0f,
              /* the dash pattern */new float[] { 8.0f, 8.0f },
              /* the dash phase */0.0f); /* on 8, off 8*/
       
    /** File Format
     *  LCW v1.0  -  Limited Component support. Case analysis on string.
     *  LCW v2.0  -  Netlist support, Component names match keys in netlists. Not backwards compatible with v1.0
     *  LCW v2.1  -  As 2.0, "Standard.Wire" moved to "Wire", backwards compatible with renaming of wire
     *  LCW v3.0  -  Attributes use netlist properties, no more special cases, not backwards compatible
     *  LCW v3.1  -  As 3.0, "with Orientation-Position pairs for dynamically created components
     */
    public static String FILE_FORMAT_VERSION = "LCW v3.1";
    public static String FILE_EXTENSION = ".xml";
    
    // Component Options
    public static final Color DEFAULT_COMPONENT_COLOUR = Color.BLACK;
    public static final Color ACTIVE_COMPONENT_COLOUR = Color.BLUE;
    public static final Color HOVER_COMPONENT_COLOUR = Color.RED;
    
    // Component Label Options
    public static int LABEL_CONNECTION_POINT_X_OFFSET = -6;
    public static int LABEL_CONNECTION_POINT_Y_OFFSET = -2;
    public static int LABEL_COMPONENT_X_OFFSET = 10;
    public static int LABEL_COMPONENT_Y_OFFSET = 0;
    public static Color LABEL_TEXT_COLOUR = Color.BLACK;

    // Wire Options
    public static int WIRE_HANDLE_LENGTH = 40;  
    public static Color WIRE_WAYPOINT_COLOUR = Color.GREEN;
    public static int WIRE_HOVER_THICKNESS = 2;
    public static final Stroke ACTIVE_WIRE_STROKE = new BasicStroke(5.0f);
    public static final Color HOVER_WIRE_COLOUR = Color.GRAY;    
    
    // Logger Options
    public static final int LOGGER_VIEWER_MARGIN = 20;
    public static final float LOGGER_Y_AXIS_SCALE = 0.1f; // Width of 1 nanosecond on the log graph
    public static final int LOGGER_HEIGHT = 30; // Height of the graph (i.e. difference between high and low values)
    public static final Color LOGGER_BACKGROUND_COLOUR = Color.WHITE;
    public static final Color LOGGER_GRID_COLOUR = new Color(230,230,230);
    public static final Color LOGGER_GRAPH_COLOR = new Color(46,139,87);
    
}

