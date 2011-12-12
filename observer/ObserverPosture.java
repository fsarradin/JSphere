/* ObserverPosture.java - posture of an observer at a given time
 *
 */

package jsphere.observer;

import jsphere.geom.Point3D;

public class ObserverPosture
{
    private Point3D location; // observer's viewpoint
    private double angle_xy;  // horizontal angle
    private double angle_xz;  // vertical angle

    public ObserverPosture (Point3D location,
			    double angle_xy,
			    double angle_xz)
    {
	this.location = location;
	this.angle_xy = angle_xy;
	this.angle_xz = angle_xz;
    }

    public Point3D getLocation () { return location; }
    public double getAngleXY () { return angle_xy; }
    public double getAngleXZ () { return angle_xz; }

    public String toString ()
    {
	return "{" + location
	    + ", Axy=" + angle_xy
	    + ", Axz=" + angle_xz
	    + "}";
    }
}

// End
