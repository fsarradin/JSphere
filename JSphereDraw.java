/* JSphereDraw.java -
 *
 */

package jsphere;

import jsphere.geom.*;
import jsphere.observer.*;
import jsphere.proj.*;
import jsphere.trans.*;
import jsphere.urban.*;

import java.io.File;

import java.awt.*;
import javax.swing.*;

// Main class for the spherical projection
public class JSphereDraw
    extends JPanel
{
    protected static double DEFAULT_RADIUS = 250.0;

    protected UrbanScene scene;
    protected Point3D location;
    protected UrbanSceneProjector usp;

    public JSphereDraw (UrbanScene scene, Point3D location)
    {
	this (scene, location, DEFAULT_RADIUS);
    }

    public JSphereDraw (UrbanScene scene, Point3D location, double scale)
    {
	this.scene = scene;
	this.location = location;
	this.usp = new UrbanSceneProjector (this.scene, scale);
    }

    public void paintComponent (Graphics g)
    {
	Graphics2D g2 = (Graphics2D) g;
	ObserverPosture posture = new ObserverPosture (location, 0.0, 0.0);

	debug ("Doing projection...");
	usp.project (g2, posture);
	debug ("...Projection end");
    }

    public Dimension getPreferredSize ()
    {
	int iscale = (int) Math.round (2.0 * usp.getScale ());
	return new Dimension (iscale, iscale);
    }

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

    public static Point3D parsePoint (String location)
    {
	Point3D point = new Point3D ();

	try {
	    String loc[] = location.split ("@");
	    point.setX (Double.parseDouble (loc[0]));
	    point.setY (Double.parseDouble (loc[1]));
	    point.setZ (Double.parseDouble (loc[2]));
	}
	catch (NumberFormatException e) {
	    error ("Malformed location object");
	    System.exit (1);
	}
	catch (IndexOutOfBoundsException e) {
	    error ("Malformed location object");
	    System.exit (1);
	}
	catch (Exception e) {
	    e.printStackTrace ();
	}
	return point;
    }

    public static void help ()
    {
	System.err.println ("java jsphere.JSphereDraw urban_scene x@y@z");
    }

    public static void createAndShow (UrbanScene scene, Point3D location)
    {
	JFrame frame = new JFrame ("Projection");
	JSphereDraw jsd = new JSphereDraw (scene, obs_point);

	frame.getContentPane ().add (jsd);

	frame.pack ();
	frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	frame.setVisible (true);
    }

    static UrbanScene a_scene = null;
    static Point3D obs_point = null;

    public static void main (String args[])
    {
	/* Arguments:
	 *   - URB ou XML file
	 *   - location (x@y@z)
	 */
	if (args.length != 2) {
	    error ("Bad number of argument (length: " + args.length + ")");
	    help ();
	    System.exit (1);
	}

	// get the .urb file
	String filename = args[0];
	File file = new File (filename);

        String location = args[1];

	// parse the file
	String ext = filename.substring (filename.length () - 3);
	if (ext.toLowerCase ().equals ("urb")) {
	    a_scene = new URBUrbanScene ();
	}
	else if (ext.equals ("xml")) {
	    a_scene = new XmlUrbanScene ();
	}
	else {
	    System.err.println ("Bad file name extension");
	    System.exit (1);
	}

	a_scene.parse (file);

	obs_point = parsePoint (location);

	// draw projections
	SwingUtilities.invokeLater (new Runnable () {
		public void run () {
		    createAndShow (a_scene, obs_point);
		}
	    });
    }
}

// End
