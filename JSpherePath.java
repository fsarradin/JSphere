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
public class JSpherePath
{
    // Default radius used for spherical projections
    protected static final double DEFAULT_RADIUS = 250.0;

    // Urban scene representation
    protected UrbanScene scene;
    // Observer path representation
    protected ObserverPath path;
    // Urban scene projection surface
    protected UrbanSceneProjector usp;

    public JSpherePath ( UrbanScene scene, ObserverPath path )
    {
	this ( scene, path, DEFAULT_RADIUS );
    }

    public JSpherePath ( UrbanScene scene,
			 ObserverPath path,
			 double scale )
    {
	this.scene = scene;
	this.path = path;
	this.usp = new UrbanSceneProjector (this.scene, scale);
    }

    public BufferedImage getProjectionAt ( double t )
    {
	return usp.project ( path.getPosture ( t ));
    }

/* --- Static Methods --- */

    public static void error ( String s )
    {
	System.err.println ( "(**) " + s );
    }

    public static void debug ( String s )
    {
	System.err.println ( "-- " + s );
    }

    public static String getName ( int i )
    {
	String st = Integer.toString ( i );
	String zero = "00000";
	zero = zero.substring ( 0, 5 - st.length () );
	return "image-" + zero + st + ".png";
    }

    public static void makeProjections ( UrbanScene scene,
					 ObserverPath path,
					 double radius,
					 double step,
					 File directory )
    {
	debug ( "radius = " + radius );

	// Create a new urban scene projection surface
	UrbanSceneProjector usp
	    = new UrbanSceneProjector ( scene, radius );
	// Create a new observer (it is the responsability of the new
	// observer to treat the case where the step is closed to 0.0)
	Observer observer = new Observer ( path, step );
	BufferedImage image = null;

	debug ( "step = " + observer.getStepLength () );

	for  (int i = 0; !observer.atEnd (); observer.advance (), i++) {
	    debug ( "Projection #" + i );

	    // Project the scene on an image according to the observer
	    // posture
	    image = usp.project ( observer.getPosture () );
	    // Save the projection in a file
	    try {
		ImageIO.write ( image, "png", new File ( directory
							 + "/"
							 + getName ( i )));
	    }
	    // In the case of an exception, just print the runtime
	    // stack and continue
	    catch (Exception e) { e.printStackTrace (); }
	}
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

    public static void main (String args[])
    {
	/* Arguments:
	 *   - path
	 *   - URB ou XML file
	 *   - destination directory
	 */
	
	Map<String, Object> map = new TreeMap<String, Object> ();

	map.put ( "radius", new Double ( DEFAULT_RADIUS ));
	map.put ( "step", new Double ( 0.0 ));

	int offset = parseArgs ( args, map, 0 );

	if ((args.length < (offset+2)) || (args.length > (offset+3))) {
	    error ( "Bad number of argument (length: " + args.length + ")" );
	    System.err.println
		( "JSphere [options] path urban_scene [directory]" );
	    System.exit ( 1 );
	}

        String xml_path = args[offset];
	String filename = args[offset+1];
        String dirname = "./";
	File file = new File ( filename );

        if (args.length == (offset+2))
            dirname = filename.substring ( 0, filename.lastIndexOf ( '.' ));
        else
            dirname = args[offset+2];

	// Get the directory and test it
	File dir = new File (dirname);
	if (!dir.isDirectory ()) {
	    if (dir.exists ()) {
		error ( dir + " must be a directory" );
		System.exit ( 1 );
	    }
	    try {
		dir.mkdirs ();
	    }
	    catch (Exception e) { e.printStackTrace (); }
	}

	// Parse the file
	UrbanScene scene = null;
	String ext = filename.substring ( filename.length () - 3 );
	if (ext.toLowerCase ().equals ( "urb" )) {
	    scene = new URBUrbanScene ();
	}
	else if ( ext.equals ( "xml" )) {
	    scene = new XmlUrbanScene ();
	}
	else {
	    System.err.println ( "Bad file name extension" );
	    System.exit ( 1 );
	}

	debug ( "parse file: " + file );
	scene.parse ( file );

	debug ( "nb of faces: " + scene.size () );

	// Make path
	ObserverPath path = new XmlObserverPath ( xml_path );

	// Compute projections
	makeProjections ( scene, path,
			  (Double) ( map.get ( "radius" )),
			  (Double) ( map.get ( "step" )),
			  dir );
    }
}

// End
