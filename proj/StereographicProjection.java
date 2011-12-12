/* StereographicProjection.java - project a sphere on a plane according to
 *                                the stereographical projection
 */

package jsphere.proj;

import jsphere.proj.Projection3D2D;
import jsphere.geom.Point2D;
import jsphere.geom.Point3D;

public class StereographicProjection
    implements Projection3D2D
{
    public StereographicProjection () {}

    public Point2D project (Point3D p)
    {
	return new Point2D (p.getX () / (1.0 + p.getZ ()),
			    p.getY () / (1.0 + p.getZ ()));
    }
}

// End
