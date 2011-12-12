/* JSphere.java - main class for spherical projection
 *
 */

package jsphere;

import jsphere.geom.*;
import jsphere.observer.*;
import jsphere.proj.*;
import jsphere.trans.*;
import jsphere.urban.*;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import java.util.Map;
import java.util.TreeMap;

/**
 * Main class for the spherical projection.
 * From a urban scene and a path, this program produce spherical projections.
 * The spherical projections are PNG pictures with a radius of 250 pixels.
 */
public class JSphere
{
    protected static final double DEFAULT_RADIUS = 250.0;
    
    /*    protected UrbanScene scene;
    protected ObserverPath path;
    protected UrbanSceneProjector usp;

    public JSphere (UrbanScene scene, ObserverPath path)
    {
	this (scene, path, DEFAULT_RADIUS);
    }

    public JSphere (UrbanScene scene, ObserverPath path, double scale)
    {
	this.scene = scene;
	this.path = path;
	this.usp = new UrbanSceneProjector (this.scene, scale);
    }

    public BufferedImage getProjectionAt (double t)
    {
	return usp.project (path.getPosture (t));
	} */

/* --- STATIC --- */

    public static void error (String s)
    {
	System.err.println ("(**) " + s);
    }

    public static void debug (String s)
    {
	System.err.println ("-- " + s);
    }

    public static String getName (int i)
    {
	String st = Integer.toString (i);
	String zero = "00000";
	zero = zero.substring (0, 5 - st.length ());
	return "image-" + zero + st + ".png";
    }

    public static void makeProjections (UrbanScene scene,
					ObserverPosture posture,
					double radius)
    {
	System.err.println ( "radius = " + radius );

	UrbanSceneProjector usp
	    = new UrbanSceneProjector ( scene, radius );
	BufferedImage image = null;

	image = usp.project ( posture );
	// save the projection in a file
	try {
	    ImageIO.write ( image, "png", new File ( "image.png" ) );
	}
	catch (Exception e) { e.printStackTrace (); }
    }

    public static int parseArgs (String args[],
				 Map<String, Object> map,
				 Integer offset)
    {
	int i = offset;
	int pos;
	String key, value;
	// double dvalue;

	while ((i < args.length) && (args[i].startsWith ("--"))) {
	    pos = args[i].indexOf ("=");
	    if (pos >= 0) {
		key = args[i].substring (2, pos);
		value = args[i].substring (pos+1);
		try {
		    // dvalue = Double.parseDouble (value);
		    map.put (key, new Double (value));
		}
		catch (Exception e) {
		    map.put (key, value);
		}
	    }
	    else {
		key = args[i].substring (2);
		map.put (key, new Boolean (true));
	    }

	    i++;
	}

	return i;
    }

    public static Point3D getPoint ( String st )
    {
	int pos1 = st.indexOf ( '@' );
	int pos2 = st.indexOf ( '@', pos1 + 1 );
	Point3D p = null;
	
	String st_x = st.substring ( 0, pos1 );
	String st_y = st.substring ( pos1 + 1, pos2 );
	String st_z = st.substring ( pos2 + 1 );

	try {
	    double x = Double.parseDouble ( st_x );
	    double y = Double.parseDouble ( st_y );
	    double z = Double.parseDouble ( st_z );

	    p = new Point3D ( x, y, z );
	}
	catch (Exception e) {
	    error ( "Bad coordinate format (" + st + ")" );
	    System.exit ( 1 );
	}

	return p;
    }

    public static void main (String args[])
    {
	Map<String,Object> map = new TreeMap<String,Object> ();

	map.put ("radius", new Double (DEFAULT_RADIUS));

	int offset = parseArgs (args, map, 0);

	if (args.length != (offset+3)) {
	    error ("Bad number of argument (length: " + args.length + ")");
	    System.err.println ("JSphere [options] x@y@z xy_angle urban_scene");
	    System.exit (1);
	}

	Point3D location = getPoint ( args[offset] );
	double angle_xy = 0.0;
	try {
	    angle_xy = Double.parseDouble ( args[offset+1] );
	}
	catch (Exception e) {
	    error ( "Bad angle format" );
	    System.exit (1);
	}
	angle_xy = Math.PI * angle_xy / 180.0;
	ObserverPosture posture
	    = new ObserverPosture ( location, angle_xy, 0.0 );

	// get the .urb file
	String filename = args[offset+2];
	File file = new File (filename);

	// parse the file
	UrbanScene scene = null;
	String ext = filename.substring (filename.length () - 3);
	if (ext.toLowerCase ().equals ("urb")) {
	    scene = new URBUrbanScene ();
	}
	else if (ext.equals ("xml")) {
	    scene = new XmlUrbanScene ();
	}
	else {
	    System.err.println ("Bad file name extension");
	    System.exit (1);
	}

	debug ("parse file: " + file);
	scene.parse (file);

	debug ("nb of faces: " + scene.size ());

	// compute projections
	makeProjections (scene,
			 posture,
			 (Double) (map.get ("radius")));
    }
}

// End
