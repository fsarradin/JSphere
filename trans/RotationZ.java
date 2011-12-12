/* RotationZ.java -
 *
 * Copyright (C) 2003 - Fran�ois Sarradin
 *
 */

package jsphere.trans;

import jsphere.trans.Transform3D;
import jsphere.geom.Point3D;

public class RotationZ implements Transform3D
{
//    private double angle;
    private double ang_cos;
    private double ang_sin;

    public RotationZ (double a) {
//	angle = a;
	ang_cos = Math.cos (a);
	ang_sin = Math.sin (a);
    }
    
    public Point3D transform (Point3D p)
    {
	return new Point3D (p.getX () * ang_cos
			    + p.getY () * ang_sin,
			    p.getY () * ang_cos
			    - p.getX () * ang_sin,
			    p.getZ ());
    }
    
    public static void main (String args[])
    {
	SphericalProjection sph = new SphericalProjection
	    (new Point3D (5.0, 5.0, 0.0));
	RotationZ rot = new RotationZ (Math.PI / 4.0);
	//	RotationZ rot = new RotationZ ();
	StereographicProjection ste = new StereographicProjection ();
	Point3D p = new Point3D (10.0, 0.0, 0.0);
	Point2D q;
	
	System.out.println ("p                  : " + p);
	p = sph.transform (p);
	System.out.println ("sph(p)             : " + p);
	p = rot.transform (p);
	System.out.println ("rot (sph(p))       : " + p);
	q = ste.project (p);
	System.out.println ("ste (rot (sph (p))): " + q);
    }
}

// End
