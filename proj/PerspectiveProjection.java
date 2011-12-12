/* PerspectiveProjection.java -
 *
 * Copyright (C) 2003 - Fran�ois Sarradin
 *
 */

package jsphere.proj;

import jsphere.proj.Projection3D2D;
import jsphere.geom.Point2D;
import jsphere.geom.Point3D;

public class PerspectiveProjection implements Projection3D2D
{
    private double focal;
    private Point3D center;

    public PerspectiveProjection ()
    {
	this (new Point3D (0.0, 0.0, 0.0), 1.0);
    }

    public PerspectiveProjection (double f)
    {
	this (new Point3D (0.0, 0.0, 0.0), f);
    }

    public PerspectiveProjection (Point3D c, double f)
    {
	focal = f;
	center = c;
    }
    
    public Point2D project (Point3D p)
    {
	Point3D tmp = p.sub (center);
	return new Point2D (focal * tmp.getY () / tmp.getX (),
			    focal * tmp.getZ () / tmp.getX ());
    }

}

// End
