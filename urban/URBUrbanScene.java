/* URBUrbanScene.java - .urb file parser
 *
 */

package jsphere.urban;

import jsphere.geom.*;

import java.io.*;
import java.util.ArrayList;

public class URBUrbanScene
    extends AbstractUrbanScene
{
    private String buffer;
    private BufferedReader in;
    private Point3D point_list[];
    private int line_number;
    private File file;

    public URBUrbanScene ()
    {
	super ();
	face_list = new ArrayList ();
    }

    /* jump to the next line in the file */
    private void _nextLine ()
    {
	try {
	    buffer = in.readLine ();
	    line_number++;
	} catch (Exception e) { e.printStackTrace (); }
    }

    private void _debug (String st)
    {
	System.err.println ("(" + file + ":" + line_number + ") " + st);
    }

    /* jump the blank characters */
    private void _passBlank ()
    {
	int i = 0;
	
	if (buffer.length () == 0)
	    return;
	
	char c = buffer.charAt (i);

	while ((i < buffer.length () - 1)
	       && ((c == ' ') || (c == '\t'))) {
	    i++;
	    c = buffer.charAt (i);
	}
	if (i > 0)
	    buffer = buffer.substring (i);
    }

    /* read a word */
    private String _readWord ()
    {
	int i = 0;
	
	if (buffer.length () == 0)
	    return null;

	char c = buffer.charAt (i);
	while ((i < buffer.length () - 1)
	       && ((c >= 'A') && (c <= 'Z'))
	       || ((c >= 'a') && (c <= 'z'))
	       || (c == '-')) {
	    i++;
	    c = buffer.charAt (i);
	}
	if (i == buffer.length () - 1) i++;
	if (i > 0) {
	    String st = buffer.substring (0, i);
	    buffer = buffer.substring (i);
	    return st;
	}
	return null;
    }

    /* read an integer */
    private Integer _readInt ()
    {
	int i = 0;

	if (buffer.length () == 0)
	    return null;

	char c = buffer.charAt (i++);
	while ((i < buffer.length ())
	       && ((c >= '0') && (c <= '9'))
	       || (c == '-')) {
	    c = buffer.charAt (i++);
	}
	if (i < buffer.length ()) i--;
	if (i > 0) {
	    String st = buffer.substring (0, i);
	    buffer = buffer.substring (i);
	    try {
		return new Integer (st);
	    } catch (NumberFormatException e) { return null; }
	}
	return null;
    }

    /* read a double */
    private Double _readReal ()
    {
	int i = 0;

	if (buffer.length () == 0)
	    return null;

	char c = buffer.charAt (i++);
	while ((i < buffer.length ())
	       && ((c >= '0') && (c <= '9'))
	       || (c == '-') || (c == '.')
	       || (c == 'e')) {
	    c = buffer.charAt (i++);
	}
	if (i < buffer.length ()) i--;
	if (i > 0) {
	    String st = buffer.substring (0, i);
	    buffer = buffer.substring (i);
	    try {
		return new Double (st);
	    } catch (NumberFormatException e) { return null; }
	}
	return null;
    }

    /* read the head line of the file */
    private boolean _readPolis ()
    {
	return buffer.indexOf ("polis-section") >= 0;
    }

    /* read a point */
    private void _readPoints ()
    {
	int nb_points;
	int p_num;
	double x, y, z;

	_passBlank ();

	nb_points = _readInt ().intValue ();
	point_list = new Point3D[nb_points];

	for (int i = 0; i < nb_points; i++) {
	    _nextLine ();

	    _passBlank ();
	    p_num = _readInt ().intValue ();
	    _passBlank ();
	    x = _readReal ().doubleValue ();
	    _passBlank ();
	    y = _readReal ().doubleValue ();
	    _passBlank ();
	    z = _readReal ().doubleValue ();

	    point_list[i] = new Point3D (x, y, z);
	}
    }

    /* read faces in the file */
    private void _readFaces ()
    {
	int nb_faces;  // number of faces
	int nb_points; // number of points
	int face_num;  // face number
	int point_num; // point number
	Face face;     // a face

	// initialise the face list
	face_list = new ArrayList ();
	// get the number of faces
	_passBlank ();
	nb_faces = _readInt ().intValue ();

	// read each face
	for (int i = 0; i < nb_faces; i++) {
	    // get the face number
	    _nextLine ();
	    _passBlank ();
	    face_num = _readInt ().intValue ();
	    // initialise a new face
	    face = new Face ();
	    // get the number of points
	    _passBlank ();
	    nb_points = _readInt ().intValue ();
	    // get each point
	    for (int j = 0; j < nb_points; j++) {
		// get point id
		_passBlank ();
		point_num = _readInt ().intValue ();
		// add the corresponding point in the face
		face.addPoint (point_list[point_num]);
	    }
	    // add a new face to the scene
	    addFace (face);
	}
    }

    /**
     * Parse a .urb file
     */
    public void parse (File f)
    {
	String st;
	file = f;
	try {
	    in = new BufferedReader (new FileReader (file));
	    line_number = 0;
	    
	    _nextLine ();
	    _readPolis ();
	    _nextLine ();
	    while (buffer != null) {
		_passBlank ();
		st = _readWord ();
		if (st != null) {
		    if (st.equals ("POINTS")) {
			_nextLine ();
			_readPoints ();
		    }
		    else if (st.equals ("FACES")) {
			_nextLine ();
			_readFaces ();
		    }
		}
		_nextLine ();
	    }
	}
	catch (Exception e) { e.printStackTrace (); }
    }

    public void output (File file)
    {
	System.err.println ("URBUrbanScene.output() not implemented");
    }

    public Point3D getPoint (int i) { return point_list[i]; }

    public static void main (String args[])
    {
	if (args.length != 2) {
	    System.err.println ("Bad number of arguments");
	    System.exit (1);
	}

	URBUrbanScene scene = new URBUrbanScene ();

	scene.parse (new File (args[0]));
	try {
	    FileOutputStream f_out = new FileOutputStream (args[1]);
	    scene.toXML (f_out);
	    f_out.close ();
	}
	catch (Exception e) { e.printStackTrace (); }
    }
}

// End
