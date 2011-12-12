/* Projection3D2D.java -
 *
 * Copyright (C) 2003 - Fran�ois Sarradin
 *
 */

package jsphere.proj;

import jsphere.geom.Point2D;
import jsphere.geom.Point3D;

public interface Projection3D2D
{
    public Point2D project (Point3D p);
}

// End
