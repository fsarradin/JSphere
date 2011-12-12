/* XmlUrbanScene.java - .xml file parser
 *
 */

package jsphere.urban;

import jsphere.geom.Point3D;
import jsphere.geom.Face;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.*;
import java.io.File;

public class XmlUrbanScene
    extends AbstractUrbanScene
{
    public void parse (File file)
    {
	try {
	    SAXParserFactory factory = SAXParserFactory.newInstance ();
	    SAXParser parser = factory.newSAXParser ();
	    
	    parser.parse (file, new XmlUrbanSceneHandler (this));
	}
	catch (Exception e) {
	    e.printStackTrace ();
	}
    }

    public void output (File file) {}

    public static void main (String args[])
    {
	XmlUrbanScene scene = new XmlUrbanScene ();
	
	scene.parse (new File ("te_10.xml"));
    }

    public void setID (String id) { sceneID = id; }
}

class XmlUrbanSceneHandler
    extends org.xml.sax.helpers.DefaultHandler
{
    XmlUrbanScene scene;
    Face current_face;

    public XmlUrbanSceneHandler (XmlUrbanScene scene)
    {
	super ();
	this.scene = scene;
    }

    private double getDouble (Attributes attrs, String name)
    {
	double value = 0.0;

	if (attrs.getIndex (name) >= 0) {
	    try {
		value = Double.parseDouble (attrs.getValue (name));
	    }
	    catch (Exception e) {
		e.printStackTrace ();
	    }
	}

	return value;
    }

    private void handlePoint (Attributes attrs)
	throws SAXException
    {
	double x, y, z;
	
	x = getDouble (attrs, "x");
	y = getDouble (attrs, "y");
	z = getDouble (attrs, "z");

	Point3D point = new Point3D (x, y, z);
	current_face.addPoint (point);
    }

    public void startElement (String uri, String localName,
			      String qName, Attributes attributes)
	throws SAXException
    {
	if (qName.equals ("urban-scene")) {
	    scene.setID (attributes.getValue ("id"));
	}
	else if (qName.equals ("face")) {
	    current_face = new Face ();
	}
	else if (qName.equals ("point")) {
	    handlePoint (attributes);
	}
    }

    public void endElement (String uri, String localName, String qName)
	throws SAXException
    {
	if (qName.equals ("face")) {
	    scene.addFace (current_face);
	}
    }
}

// End
