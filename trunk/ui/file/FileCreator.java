package ui.file;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.sax.*;
import ui.UIConstants;
import ui.tools.SelectableComponent;
/**
 *
 * @author matt
 */
public class FileCreator {
    
    private PrintWriter out = null;
    private TransformerHandler hd = null;
    
    public FileCreator(String filename){
        
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            out = new PrintWriter(fos);
            StreamResult streamResult = new StreamResult(out);
            SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            tf.setAttribute("indent-number", 4);
            
            // SAX2.0 ContentHandler.
            hd = tf.newTransformerHandler();
            Transformer serializer = hd.getTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "circuit.dtd");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");

            hd.setResult(streamResult);
            hd.startDocument();
            
            
            AttributesImpl atts = new AttributesImpl();
            // "circuit" tag
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMM yyyy',' HH:mm");
            atts.addAttribute("", "", "description", "CDATA",  "get some value here");
            atts.addAttribute("", "", "author", "CDATA",  System.getProperty("user.name"));
            atts.addAttribute("", "", "createdOn", "CDATA",  sdf.format(cal.getTime()));
            atts.addAttribute("", "", "name", "CDATA", "test1");
            atts.addAttribute("", "", "version", "CDATA", UIConstants.FILE_FORMAT_VERSION);
            hd.startElement("", "", "circuit", atts);        

            // "components" tag
            atts.clear();
            hd.startElement("", "", "components", atts);       
            
        } catch (SAXException ex) {
            Logger.getLogger(FileCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(FileCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileCreator.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void add(SelectableComponent sc) {
        sc.createXML(hd);     
    }

    public void write() {
        try {
            hd.endElement("", "", "components"); 
            hd.endElement("", "", "circuit");
            hd.endDocument();
            out.close();
        } catch (SAXException ex) {
            Logger.getLogger(FileCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

