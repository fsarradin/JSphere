/* Polyline.java - plan poly-line representation
 *
 */

package jsphere.geom;

import jsphere.geom.Geometrical;
import jsphere.geom.Point3D;
import jsphere.geom.Line;

import java.util.ArrayList;

public class Polyline implements Geometrical
{
    protected ArrayList p_list;

    public Polyline () { p_list = new ArrayList (); }

    public void addPoint (Point3D p) { p_list.add (p); }

    public Point3D getPoint (int i) { return (Point3D) p_list.get (i); }

    public Line getLine (int i)
    {
	return new Line (getPoint (i), getPoint ((i+1) % p_list.size ()));
    }

    public int size () { return p_list.size (); }
}

// End
