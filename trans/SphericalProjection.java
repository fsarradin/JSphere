/* SphericalProjection.java - spherical projector
 *
 */

package jsphere.trans;

import jsphere.trans.Transform3D;
import jsphere.geom.Point3D;

public class SphericalProjection
    implements Transform3D
{
    private Point3D center;  // center of the projection
    private double radius;   // radius of the sphere

    public SphericalProjection () { this (new Point3D (), 1.0); }

    public SphericalProjection (double r) { this (new Point3D (), r); }

    public SphericalProjection (Point3D c) { this (c, 1.0); }

    public SphericalProjection (Point3D c, double r)
    {
	center = c;
	radius = r;
    }
    
    public Point3D getCenter () { return center; }
    public double getRadius () { return radius; }

    public Point3D transform (Point3D p)
    {
	// translation to the projection center
	Point3D q = p.sub (center);
	
	if (q.isOrigin ()) {
	    return new Point3D (0.0, 0.0, 1.0);
	}
	// spherical projection: radius * q / |q|
	return q.div (q.abs ()). mul (radius);
    }


    /* Test method
     *
     */
    public static void main (String args[])
    {
	SphericalProjection sph = new SphericalProjection
	    (new Point3D (5.0, 5.0, -5.0), 1.0);
	Point3D p = new Point3D (1.0, 1.0, 1.0);

	System.out.println (p + " --> " + sph.transform (p));
    }
}

// End
