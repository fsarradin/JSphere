/* AbstractUrbanScene.java - urban scene representation
 *
 */

package jsphere.urban;

import jsphere.geom.*;

import java.util.ArrayList;
import java.io.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.w3c.dom.*;

public abstract class AbstractUrbanScene
    implements UrbanScene
{
    protected String sceneID = null;
    protected ArrayList face_list;

    public AbstractUrbanScene () { face_list = new ArrayList (); }

    public String getID () { return sceneID; }

    public void addFace (Face face) { face_list.add (face); }
    public Face getFace (int i) { return (Face) face_list.get (i); }
    public void clearFaces () { face_list.clear (); }
    public int size () { return face_list.size (); }

    public abstract void parse (File file);

    //    public abstract void output (File file);

    public void toXML (OutputStream out)
    {
	try {
	    PrintStream pout = new PrintStream (out);
	    pout.println ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	    pout.println ("<urban-scene>");

	    for (int i = 0; i < size (); i++) {
		pout.println ("  <face>");
		Face face = getFace (i);
		for (int j = 0; j < face.size (); j++) {
		    Point3D point = face.getPoint (j);
		    pout.println ("    <point x=\""
				  + String.valueOf (point.getX ()) + "\" y=\""
				  + String.valueOf (point.getY ()) + "\" z=\""
				  + String.valueOf (point.getZ ()) + "\" />");
		}
		pout.println ("  </face>");
	    }

	    pout.println ("</urban-scene>");
	}
	catch (Exception e) { e.printStackTrace (); }
    }
}

// End
