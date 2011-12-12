/* Face.java - geometrical face representation
 *
 * Copyright (C) 2003 - Fran�ois Sarradin
 *
 */

package jsphere.geom;

import jsphere.geom.Geometrical;
import jsphere.geom.Polyline;
import jsphere.geom.Point3D;

public class Face extends Polyline
{
    public Face () { super (); }

    private double _sign (double x)
    {
	return x > 0.0 ? 1.0 : x < 0.0 ? -1.0 : 0.0;
    }

    private double _angle (Point3D p)
    {
	Point3D q = p.div (p.abs ());
	return _sign (Math.asin (p.getZ ())) * Math.acos (p.getX ());
    }

//     public void addPoint (Point3D p)
//     {
// 	int len = p_list.size ();

// 	if (len < 2)
// 	    p_list.add (p);
// 	else {
// 	    int add_at = 0;
// 	    for (int i = 0; i < len; i++) {
// 		Point3D p1 = (Point3D) p_list.get (i);
// 		double a1 = _angle (p.sub (p1));
// 		Point3D p2 = (Point3D) p_list.get ((i+1) % len);
// 		double a2 = _angle (p2.sub (p1));
// 		if (a1 < a2) {
// 		    add_at = i+1;
// 		    System.err.println ("add at: " + add_at
// 					+ " (len=" + len
// 					+ ", angle=" + a1 + ")");
// 		    break;
// 		}
// 	    }
// 	    if (add_at > 0) {
// 		p_list.add (add_at, p);
// 	    }
// 	    else {
// 		System.err.println ("add at: " + len
// 				    + " (len = " + len + ")");
// 		p_list.add (len, p);
// 	    }
// 	}
//     }
    
    public String toString ()
    {
	String st = "[";
	for (int i = 0; i < p_list.size (); i++) {
	    st += p_list.get (i);
	}
	return st + "]";
    }
    
    public static void main (String args[])
    {
	Face f = new Face ();

	f.addPoint (new Point3D (0.0, 0.0, 0.0));
	System.out.println (f);
	f.addPoint (new Point3D (1.0, 0.0, 0.0));
	System.out.println (f);
	f.addPoint (new Point3D (0.5, 0.0, -1.0));
	System.out.println (f);
	f.addPoint (new Point3D (1.0, 0.0, -1.0));
	System.out.println (f);
	f.addPoint (new Point3D (0.5, 0.0, 1.0));
	System.out.println (f);
    }
}

// End
