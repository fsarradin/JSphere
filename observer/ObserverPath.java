/* ObserverPath.java - path followed by an observer
 *
 */

package jsphere.observer;

import jsphere.observer.ObserverPosture;
import jsphere.geom.Point3D;
import jsphere.geom.Line;

import java.util.ArrayList;

/**
 * Parametrised path of an observer in an urban scene.
 *
 * This class only manage multi-linear paths of the observer.  Future
 * works should almost consider linear and/or curve paths.
 */
public class ObserverPath
{
    protected final double DEFAULT_STEP = 0.01;

    protected ArrayList<Point3D> p_list;
    protected double pref_step = DEFAULT_STEP;

    public ObserverPath () { p_list = new ArrayList<Point3D> (); }

    public double getPreferredStep () { return pref_step; }

    public void addPoint (Point3D p) { p_list.add (p); }

    /**
     * Get the observer location and angles according to the parameter t.
     */
    public ObserverPosture getPosture (double t)
    {
	int p_len = p_list.size ();  // number of points in the list

	if (p_len <= 1)
	    // there is only one or no point
	    return null;

	Point3D p1 = null;
	Point3D p2 = p_list.get (0);
	double loc = length () * t;  // length from the path start
	double old_loc = loc;

	// find the segment containing the observer location
	for (int i = 1; (i < p_len) && (loc >= 0.0); i++) {
	    p1 = p2;
	    p2 = p_list.get (i);
	    old_loc = loc;
	    loc -= p1.dist (p2);
	}

	Line line = new Line (p1, p2);  // current segment
	Point3D tmp = p2.sub (p1);
	Point3D obs_loc = line.getPoint (old_loc / line.length ());

	double r = obs_loc.dist (p1);
	double a_xy = -Math.atan (tmp.getY () / tmp.getX ());
	double a_xz = r == 0.0 ? Math.acos (tmp.getZ () / r): Math.PI / 2.0;
	
	return new ObserverPosture (obs_loc, a_xy, a_xz);
    }

    /**
     * Get the length of the path.
     */
    public double length ()
    {
	int p_len = p_list.size ();  // number of points in the list

	if (p_len <= 1)
	    // there is only one or no point
	    return 0.0;

	// there is more than one point
	Point3D p1 = p_list.get (0);
	Point3D p2;
	double len = 0.0;

	for (int i = 1; i < p_len; i++) {
	    // get the end point of the current segment
	    p2 = p_list.get (i);
	    // add the length of the current segment
	    len += p1.dist (p2);
	    p1 = p2;
	}

	return len;
    }

    public static void main (String args[])
    {
	ObserverPath path = new ObserverPath ();

	path.addPoint (new Point3D (-50.0, 5.0, 1.70));
	path.addPoint (new Point3D (60.0, 5.0, 1.70));
//	path.addPoint (new Point3D (3.0, 3.0, 0.0));

	System.out.println ("length: " + path.length ());
	System.out.println ("p(.25) : " + path.getPosture (.25));
    }
}

// End

