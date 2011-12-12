/* Point2D.java - 2D point
 *
 */

package jsphere.geom;

import jsphere.geom.Geometrical;

public class Point2D
    implements Geometrical
{
    private double x, y;

    public Point2D () { x = 0.0; y = 0.0; }

    /**
     * Origin point.
     */
    public Point2D (double ix, double iy)
    {
	x = ix;
	y = iy;
    }

    public Point2D (Point2D p)
    {
	x = p.x;
	y = p.y;
    }

    // getters and setters

    public double getX () { return x; }
    public double getY () { return y; }
    public void setX (double ix) { x = ix; }
    public void setY (double iy) { y = iy; }

    // arithmetic

    public Point2D add (Point2D p)
    {
	return new Point2D (x + p.x, y + p.y);
    }

    public Point2D sub (Point2D p)
    {
	return new Point2D (x - p.x, y - p.y);
    }

    public Point2D neg ()
    {
	return new Point2D (-x, -y);
    }

    public Point2D mul (double i)
    {
	return new Point2D (x * i, y * i);
    }

    public Point2D div (double i)
    {
	return new Point2D (x / i, y / i);
    }

    public double dot (Point2D p)
    {
	return x * p.x + y * p.y;
    }

    public double abs ()
    {
	return Math.sqrt (x*x + y*y);
    }

    public Point3D cross (Point2D p)
    {
	return new Point3D (0.0, 0.0, x * p.y - y * p.x);
    }

    public double dist (Point2D p)
    {
	return sub (p).abs ();
    }

    public boolean isOrigin ()
    {
	return (x == 0.0) && (y == 0.0);
    }

    public java.awt.geom.Point2D toJava ()
    {
	return new java.awt.geom.Point2D.Double (x, y);
    }

    public String toString ()
    {
	return "(" + x + "," + y + ")";
    }

    public static void main (String args[])
    {
	Point2D p1 = new Point2D (366, 366);
	Point2D p2 = new Point2D (335, 335);

	System.out.println ("p1: " + p1);
	System.out.println ("p2: " + p2);
	System.out.println ("p2-p1: " + p2.sub (p1));
	System.out.println ("dist = " + p1.dist (p2));
	System.out.println ("p1 * 200: " + p1.mul (200.0));
    }
}

// End
