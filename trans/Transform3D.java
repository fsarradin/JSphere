/* Transform3D.java - 
 *
 * Copyright (C) 2003 - Fran�ois Sarradin
 *
 */

package jsphere.trans;

import jsphere.geom.Point3D;

public interface Transform3D
{
    public Point3D transform (Point3D p);
}

// End
