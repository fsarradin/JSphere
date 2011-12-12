/* Line.java - line representation
 *
 */

package jsphere.geom;

import jsphere.geom.Geometrical;
import jsphere.geom.Point3D;

public class Line implements Geometrical
{
    protected Point3D p1, p2;

    public Line (Point3D p1, Point3D p2)
    {
	this.p1 = p1;
	this.p2 = p2;
    }

    public Point3D getStart () { return p1; }
    public Point3D getEnd () { return p2; }

    /**
     * Get a point in the line from the parameter t.
     */
    public Point3D getPoint (double t)
    {
	return (p2.sub (p1)).mul (t).add (p1);
    }

    public double length ()
    {
	return p1.dist (p2);
    }

    public String toString ()
    {
	return "[" + p1 + ',' + p2 + ']';
    }
}

// End
