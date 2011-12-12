/* Point3D.java - 3D point
 *
 */

package jsphere.geom;

import jsphere.geom.Geometrical;

public class Point3D
    implements Geometrical
{
    private double x, y, z;

    /**
     * Origin point.
     */
    public Point3D () { x = 0.0; y = 0.0; z = 0.0; }

    public Point3D (double ix, double iy, double iz)
    {
	x = ix;
	y = iy;
	z = iz;
    }

    public Point3D (Point3D p)
    {
	x = p.x;
	y = p.y;
	z = p.z;
    }

    // getters and setters

    public double getX () { return x; }
    public double getY () { return y; }
    public double getZ () { return z; }
    public void setX (double ix) { x = ix; }
    public void setY (double iy) { y = iy; }
    public void setZ (double iz) { z = iz; }

    // arithmetic

    public Point3D add (Point3D p)
    {
	return new Point3D (x + p.x, y + p.y, z + p.z);
    }

    public Point3D sub (Point3D p)
    {
	return new Point3D (x - p.x, y - p.y, z - p.z);
    }

    public Point3D neg ()
    {
	return new Point3D (-x, -y, -z);
    }

    public Point3D mul (double i)
    {
	return new Point3D (x * i, y * i, z * i);
    }

    public Point3D div (double i)
    {
	return new Point3D (x / i, y / i, z / i);
    }

    // scalar product
    public double dot (Point3D p)
    {
	return x * p.x + y * p.y + z * p.z;
    }

    public double abs ()
    {
	return Math.sqrt (x*x + y*y + z*z);
    }

    // vectorial product
    public Point3D cross (Point3D p)
    {
	return new Point3D (y * p.z - z * p.y,
			    z * p.x - x * p.z,
			    x * p.y - y * p.x);
    }

    public double dist (Point3D p)
    {
	return sub (p).abs ();
    }

    public boolean isOrigin ()
    {
	return (x == 0.0) && (y == 0.0) && (z == 0.0);
    }

    public String toString ()
    {
	return "(" + x + "," + y + "," + z + ")";
    }

    public static void main (String args[])
    {
	Point3D p1 = new Point3D (1.0, 1.0, 1.0);
	Point3D p2 = new Point3D (5.0, 5.0, -5.0);

	System.out.println (p1.sub (p2));
	System.out.println (p1.sub (p2).abs ());
    }
}

// End
