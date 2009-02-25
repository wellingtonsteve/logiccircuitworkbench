package ui.file;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import ui.Editor;
import ui.UIConstants;
import ui.command.CreateComponentCommand;
import ui.error.ErrorHandler;
import ui.components.*;
import ui.components.standard.*;

/**
 *
 * @author matt
 */
public class FileLoader extends DefaultHandler{

    private Stack<SelectableComponent> stack;
    private Editor editor;
    private boolean successful = true;
    
    public FileLoader(Editor editor){
        this.editor = editor;
        stack = new Stack<SelectableComponent>();
    }
    
    /**
     * Open the File with the filename matching that in the argument. It is parsed
     * using the XMLReaderFactory class with elements realised by the startElement
     * method which follows.
     * 
     * @param filename The filename of the file to open.
     * @return Whether the load was successful or not.
     */
    public boolean loadFile(String filename){
        stack.clear();
        try {
	    File file = new File(filename);
	    InputSource src = new InputSource( new FileInputStream( file ) );

            XMLReader rdr = XMLReaderFactory.createXMLReader();
            rdr.setContentHandler( this );
            rdr.parse( src );
           
	}catch( Exception ex ) {
	    ErrorHandler.newError("File Load Error","Please see the system error below.", ex);
            successful = false;    
	} 
        
        return successful;
                
    }
    /**
     * This method is called when the start of a new XML tag is encountered.
     * The case analysis performs appropriate action for the creation or 
     * modification of the components on the stack.
     * 
     * @param uri
     * @param localName
     * @param qName
     * @param attribs
     * @throws org.xml.sax.SAXException
     */
    @Override
    public void startElement (String uri, String localName, String qName,
			      Attributes attribs) throws SAXException {
        // Check that an error has not already happened
        if(successful){
            // Check the file version and set the other circuit parameters <circuit>
            if(qName.equals("circuit")){
                if(!attribs.getValue("version").equals(UIConstants.FILE_FORMAT_VERSION)){
                    ErrorHandler.newError("File Load Error","Invalid File Format: Version is incorrect..."+attribs.getValue("version"));
                    successful = false;                
                }
            // Create a new component <component>
            } else if(qName.equals("component")){

                // Get co-ordinate attributes
                int x = Integer.parseInt(attribs.getValue("x"));
                int y = Integer.parseInt(attribs.getValue("y"));
                Point p = new Point(x,y);

                // Get rotation attribute
                double rotation = Double.parseDouble(attribs.getValue("rotation"));

                // Get textual attributes
                String type = attribs.getValue("type");
                String label = attribs.getValue("label");
                                
                // Create a new component with the desired attributes
                CreateComponentCommand ccc = new CreateComponentCommand(new Object[]{
                    type,                // properties[0] = componentName
                    rotation,            // properties[1] = rotation
                    p,                   // properties[2] = point
                    label,               // properties[3] = label
                    null,                // properties[4] = LED Colour
                    null                 // properties[5] = Input On/Off
                });
                
                // Perform the creation
                ccc.execute(editor);
                
                // fix it to the circuit
                ccc.getComponent().translate(0, 0, true);
                stack.push(ccc.getComponent());
                
                
            // Create a new wire <wire>
            } else if(qName.equals("wire")){

                // Get the coordinates attributes for the wire
                int startx = Integer.parseInt(attribs.getValue("startx"));
                int starty = Integer.parseInt(attribs.getValue("starty"));
                int endx = Integer.parseInt(attribs.getValue("endx"));
                int endy = Integer.parseInt(attribs.getValue("endy"));
                
                // Create a new component with the desired attributes
                CreateComponentCommand ccc = new CreateComponentCommand(new Object[]{
                    "Wire",                // properties[0] = componentName
                    0.0,           // properties[1] = rotation
                    new Point(startx, starty),                   // properties[2] = point
                    "",               // properties[3] = label
                    null,                // properties[4] = LED Colour
                    null                 // properties[5] = Input On/Off
                });
                
                // Perform the creation
                ccc.execute(editor);
                
                // Create the wire
                Wire w = (Wire) ccc.getComponent();

                // Set the attributes
                w.setEndPoint(new Point(endx, endy));
                stack.push(w);
                

            // Set the attributes of a previously created component <attr>
            } else if(qName.equals("attr")){    
                String attrName = attribs.getValue("name");
                String attrValue = attribs.getValue("value");
                SelectableComponent top = stack.peek();

                try{
                    top.getProperties().getAttribute(attrName).setValue(attrValue);
                } catch (Exception e){
                    successful = false;           
                    ErrorHandler.newError("File Load Error",
                            "Invalid File Format: The property \"" + attrName +
                            "\" is not valid for a "+ top.getKeyName() +".", e);
                }
            // Add a waypoint to a previously created wire <waypoint>
            } else if(qName.equals("waypoint")){    

                int x = Integer.parseInt(attribs.getValue("x"));
                int y = Integer.parseInt(attribs.getValue("y"));

                SelectableComponent top = stack.peek();

                if(top instanceof Wire){
                    ((Wire) top).addWaypoint(new Point(x,y));
                } else {
                    successful = false;           
                    ErrorHandler.newError("File Load Error",
                            "Invalid File Format: The element \"waypoint\" " +
                            "is not valid for a "+ top.getClass().getSimpleName() +".");
                }
            }
        }        
    }

    /**
     * Perform closing actions on XML tags.
     * 
     * @param uri
     * @param localName
     * @param qName
     * @throws org.xml.sax.SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // Fix the wire only after we've added all the waypoints
        if(successful && qName.equals("wire")){
            stack.peek().translate(0, 0, true);
        }
    }    
}
