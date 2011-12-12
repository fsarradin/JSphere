/*
 * XmlObserverPath.java
 *
 * Created on 14 avril 2004, 16:07
 */

package jsphere.observer;

import jsphere.geom.Point3D;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * Produces an ObserverPath from a XML file.
 *
 * @author  François Sarradin
 */
public class XmlObserverPath extends ObserverPath {
    /** Creates a new instance of XmlObserverPath */
    public XmlObserverPath(String uri) {
        super ();
        Document document = null;
        try {
            DocumentBuilderFactory factory
		= DocumentBuilderFactory.newInstance ();
            DocumentBuilder builder = factory.newDocumentBuilder ();
            document = builder.parse (uri);
        }
        catch (IOException e) { System.err.println ("I/O error"); }
        catch (SAXException e) { System.err.println ("Syntax error"); }
        catch (Exception e) {}
        Element root = document.getDocumentElement();
	if (root.hasAttribute ("step")) {
	    try {
		pref_step = Double.parseDouble (root.getAttribute ("step"));
	    }
	    catch (Exception e) {}
	}
        parseNodeList (root.getChildNodes());
    }

    public void parseNodeList(NodeList node_list)
    {
        double x, y, z;
        Point3D p;
        
        for (int i = 0; i < node_list.getLength(); i++) {
            if (node_list.item (i).getNodeName ().equals("point")) {
                Element p_elt = (Element) node_list.item (i);
                try {
                    x = Double.parseDouble (p_elt.getAttribute("x"));
                    y = Double.parseDouble (p_elt.getAttribute("y"));
                    z = Double.parseDouble (p_elt.getAttribute("z"));
                    p = new Point3D (x, y, z);
                    this.addPoint(p);
                }
                catch (Exception e) {}
            }
        }
    }
}

// End
