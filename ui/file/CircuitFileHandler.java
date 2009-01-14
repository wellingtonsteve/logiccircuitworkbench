package ui.file;

import java.awt.Point;
import java.io.*;
import java.util.Stack;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import ui.Editor;
import ui.UIConstants;
import ui.tools.*;

/**
 *
 * @author matt
 */
public class CircuitFileHandler extends DefaultHandler{

    private Stack<SelectableComponent> stack;
    private Editor editor;
    
    public CircuitFileHandler(Editor editor){
        this.editor = editor;
        stack = new Stack<SelectableComponent>();
    }
    
    public Stack<SelectableComponent> getStack(){
        return stack;
    }
    
    public Stack<SelectableComponent> loadFile(String filename){
        stack.clear();
        try {
	    File file = new File(filename);
	    InputSource src = new InputSource( new FileInputStream( file ) );

            XMLReader rdr = XMLReaderFactory.createXMLReader();
            rdr.setContentHandler( this );
            rdr.parse( src );
           
	}catch( Exception exc ) {
	    System.err.println( "Exception: " + exc );
            exc.printStackTrace();
	} 
        
        return stack;
                
    }
    
    @Override
    public void startElement (String uri, String localName, String qName,
			      Attributes attribs) throws SAXException {

        if(qName.equals("circuit")){
            if(!attribs.getValue("version").equals(UIConstants.FILE_FORMAT_VERSION)){
                // TODO: Cancel load
                ui.error.ErrorHandler.newError(new ui.error.Error("File Load Error","Invalid File Format: Version is incorrect..."+attribs.getValue("version")));
            }
        } else if(qName.equals("component")){
            
            int x = Integer.parseInt(attribs.getValue("x"));
            int y = Integer.parseInt(attribs.getValue("y"));
            Point p = new Point(x,y);
            
            double rotation = Double.parseDouble(attribs.getValue("rotation"));
            
            String type = attribs.getValue("type");
            // TODO Integrate with netlists
            
            if(type.equals("Input")){               stack.push(new netlist.standard.Input(editor.getActiveCircuit(), p)); }
            else if(type.equals("LED")){            stack.push(new netlist.standard.LED(editor.getActiveCircuit(), p)); }
            else if(type.equals("AndGate2Input")){  stack.push(new netlist.logicgates.AndGate2Input(editor.getActiveCircuit(), p)); }
            else if(type.equals("AndGate3Input")){  stack.push(new netlist.logicgates.AndGate3Input(editor.getActiveCircuit(), p)); }
            else if(type.equals("NandGate2Input")){ stack.push(new netlist.logicgates.NandGate2Input(editor.getActiveCircuit(), p)); }
            else if(type.equals("NorGate2Input")){  stack.push(new netlist.logicgates.NorGate2Input(editor.getActiveCircuit(), p)); }
            else if(type.equals("OrGate2Input")){   stack.push(new netlist.logicgates.OrGate2Input(editor.getActiveCircuit(), p)); }
            
            stack.peek().setRotation(rotation);
            stack.peek().translate(0, 0, true);
            
        } else if(qName.equals("wire")){
            
            int startx = Integer.parseInt(attribs.getValue("startx"));
            int starty = Integer.parseInt(attribs.getValue("starty"));
            int endx = Integer.parseInt(attribs.getValue("endx"));
            int endy = Integer.parseInt(attribs.getValue("endy"));

            netlist.standard.Wire w = new netlist.standard.Wire(editor.getActiveCircuit());
            
            w.setStartPoint(new Point(startx, starty));
            w.setEndPoint(new Point(endx, endy));
            w.translate(0, 0, true);
            
            stack.push(w);
                        
        } else if(qName.equals("attr")){    
            
            String attrName = attribs.getValue("name");
            String attrValue = attribs.getValue("value");
            
            SelectableComponent top = stack.peek();
            
            if(top instanceof netlist.standard.LED && attrName.equals("colour")){
                ((netlist.standard.LED) top).setColour(attrValue);
            } else if(top instanceof netlist.standard.Input && attrName.equals("value")){
                ((netlist.standard.Input) top).setIsOn(attrValue.equals("On"));
            } else {
                // TODO: Cancel load
                ui.error.ErrorHandler.newError(new ui.error.Error("File Load Error","Invalid File Format: The property \"" + attrName + "\" is not valid for a "+ top.getClass().getSimpleName() +"."));
            }
        
        } else if(qName.equals("waypoint")){    
            
            int x = Integer.parseInt(attribs.getValue("x"));
            int y = Integer.parseInt(attribs.getValue("y"));
            
            SelectableComponent top = stack.peek();
            
            if(top instanceof netlist.standard.Wire){
                ((netlist.standard.Wire) top).addWaypoint(new Point(x,y));
            } else {
                // TODO:  cancel load
                ui.error.ErrorHandler.newError(new ui.error.Error("File Load Error","Invalid File Format: The element \"waypoint\" is not valid for a "+ top.getClass().getSimpleName() +"."));
            }
        }
        
    }
    
    
}
