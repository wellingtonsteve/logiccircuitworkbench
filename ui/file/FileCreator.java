package ui.file;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import org.xml.sax.helpers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.sax.*;
import ui.CircuitPanel;
import ui.UIConstants;
import ui.components.SelectableComponent;
/**
 *
 * @author matt
 */
public class FileCreator {
    
    private static PrintWriter out = null;
    private static TransformerHandler hd = null;
   
    public static void write(CircuitPanel circuitPanel, LinkedList<SelectableComponent> drawnComponents) {
         try {            
            FileOutputStream fos = new FileOutputStream(circuitPanel.getFilename());
            out = new PrintWriter(fos);
            StreamResult streamResult = new StreamResult(out);
            SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            tf.setAttribute("indent-number", 4);
            
            // SAX2.0 ContentHandler.
            hd = tf.newTransformerHandler();
            Transformer serializer = hd.getTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            //serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "circuit.dtd");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");

            hd.setResult(streamResult);
            hd.startDocument();            
            
            AttributesImpl atts = new AttributesImpl();
            // "circuit" tag
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMM yyyy',' HH:mm");
            atts.addAttribute("", "", "author", "CDATA",  System.getProperty("user.name"));
            atts.addAttribute("", "", "modifiedOn", "CDATA",  sdf.format(cal.getTime()));
            atts.addAttribute("", "", "version", "CDATA", UIConstants.FILE_FORMAT_VERSION);
            hd.startElement("", "", "circuit", atts);        

            // "components" tag
            atts.clear();
            hd.startElement("", "", "components", atts); 
            
            // Add description, title and image url from circuit Attributes
            atts.clear();
            atts.addAttribute("", "", "name", "CDATA", "Title");
            atts.addAttribute("", "", "value", "CDATA", (String) circuitPanel.getProperties().getAttribute("Title").getValue());
            hd.startElement("", "", "attr", atts);
            hd.endElement("","","attr");
            atts.clear();
            atts.addAttribute("", "", "name", "CDATA", "Description");
            atts.addAttribute("", "", "value", "CDATA", (String) circuitPanel.getProperties().getAttribute("Description").getValue());
            hd.startElement("", "", "attr", atts);
            hd.endElement("","","attr");
            atts.clear();
            atts.addAttribute("", "", "name", "CDATA", "Subcircuit Image");
            atts.addAttribute("", "", "value", "CDATA", (String) circuitPanel.getProperties().getAttribute("Subcircuit Image").getValue());
            hd.startElement("", "", "attr", atts);
            hd.endElement("","","attr");
            
            // Create xml for each component
            for (SelectableComponent sc : drawnComponents) {
                if (sc.isFixed()) {
                    sc.createXML(hd);   
                }
            }        
            
            hd.endElement("", "", "components"); 
            hd.endElement("", "", "circuit");
            hd.endDocument();
            out.close();
            
        } catch (FileNotFoundException ex) {
            ui.error.ErrorHandler.newError("File Not Found","Please refer to the system output below.",ex);
        } catch (Exception ex) {
            ui.error.ErrorHandler.newError("File Creation Error","Please refer to the system output below.",ex);
        } 
    }
}