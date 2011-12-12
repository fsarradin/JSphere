/* UrbanSceneProjector.java - spherical projection generator of an urba scene
 *
 */

package jsphere;

import jsphere.geom.*;
import jsphere.observer.ObserverPosture;
import jsphere.proj.StereographicProjection;
import jsphere.trans.SphericalProjection;
import jsphere.urban.UrbanScene;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

public class UrbanSceneProjector
{
    private UrbanScene scene;
    /* spherical projection radius */
    private double scale;

    private StereographicProjection ste;
    private SphericalProjection sph;

    private AffineTransform aft;

    public UrbanSceneProjector (UrbanScene scene, double scale)
    {
	this.scene = scene;
	this.scale = scale;

	ste = new StereographicProjection ();
	sph = new SphericalProjection ();

	aft = new AffineTransform ();
    }

    public UrbanScene getScene () { return scene; }
    public double getScale () { return scale; }

    /* convert a 3D point to a 2D point according to a stereographical
     * projection
     */
    private Point2D _project (Point3D p)
    {
	return ste.project (sph.transform (p));
    }

    /* recursive method to project a straight line
     * the projected line is stored in a Java2D GeneralPath data structure
     */
    private void _makePath (GeneralPath path, Line l, double t_s, double t_e)
    {
	if (Math.abs (t_s - t_e) < 1e-5)
	    return;

	// System.out.println ("ts = " + t_s + ", te = " + t_e);

	Point3D p3d_s = l.getPoint (t_s);
	Point3D p3d_e = l.getPoint (t_e);

	Point2D p_s = _project (p3d_s).mul (scale);
	Point2D p_e = _project (p3d_e).mul (scale);

	if (p_s.dist (p_e) < 1.0) {
	    Point2D p = _project (l.getPoint ((t_s + t_e) / 2.0))
		.mul (scale);
	    path.lineTo ((float) p.getX (), (float) p.getY ());
	    return;
	}

	double t = (t_s + t_e) / 2.0;
	Point3D p3d = l.getPoint (t);
	
	_makePath (path, l, t_s, t);
	_makePath (path, l, t, t_e);
    }

    private Face _treatFace (Face face)
    {
	Face f = new Face ();
	Point3D p1 = null;
	Point3D p2 = null;
	Point3D pi = null;
	double horizon = sph.getCenter ().getZ ();
	//	double horizon = 0.0;

	p1 = face.getPoint (0);
	if (p1.getZ () >= horizon) {
	    f.addPoint (p1);
	}
	for (int i = 1; i <= face.size (); i++) {
	    p2 = face.getPoint (i%face.size ());
	    if (p1.getZ () >= horizon) {
		if (p2.getZ () >= horizon) {
		    f.addPoint (p2);
		}
		else {
		    double t = (horizon - p1.getZ ())
			/ (p2.getZ () - p1.getZ ());
		    pi = (p2.sub (p1)).mul (t).add (p1);
		    f.addPoint (pi);
		}
	    }
	    else {
		if (p2.getZ () >= horizon) {
		    double t = (horizon - p1.getZ ())
			/ (p2.getZ () - p1.getZ ());
		    pi = (p2.sub (p1)).mul (t).add (p1);
		    f.addPoint (pi);
		    f.addPoint (p2);
		}
	    }
	    p1 = p2;
	}
	if (f.size () == 0)
	    return null;

	return f;
    }

    /* method to project a face
     * the projected face is stored in a Java2D GeneralPath data structure
     * as a polygon
     */
    private GeneralPath _face2Path (Face face)
    {
	GeneralPath path = new GeneralPath ();
	Face f = _treatFace (face);

	//	System.err.println ("from face:\n  " + face
	//			    + "\n  to face:\n  " + f);

	if (f == null) {
	    return null;
	}
	int f_len = f.size ();

	Point2D p = _project (f.getLine (0).getStart ()).mul (scale);

	path.moveTo ((float) p.getX (), (float) p.getY ());
	
	for (int i = 0; i < f_len; i++) {
	    Line l = f.getLine (i);
	    _makePath (path, l, 0.0, 1.0);

	    p = _project (l.getEnd ()).mul (scale);

	    path.lineTo ((float) p.getX (), (float) p.getY ());
	}

	path.closePath ();

	return path;
    }

    /* method to project all faces
     *
     */
    private GeneralPath[] _computeProjection (UrbanScene scene)
    {
	int nb_faces = scene.size ();
	GeneralPath path_list[] = new GeneralPath[nb_faces];

	int j = 0;
	for (int i = 0; i < path_list.length; i++) {
	    GeneralPath path = _face2Path (scene.getFace (i));
	    if (path != null) {
		path_list[j++] = path;
	    }
	    else {
		nb_faces--;
	    }
	}

	if (nb_faces <= 0)
	    return null;

	GeneralPath tmp[] = new GeneralPath[nb_faces];
	System.arraycopy (path_list, 0, tmp, 0, nb_faces);

	return tmp;
    }

    /**
     * Generate a spherical projection according to the observer posture.
     */
    public BufferedImage project (ObserverPosture posture)
    {
	BufferedImage im = _makeImage ();
	// getting an associated Graphics2D in order to draw on the image
	Graphics2D g2 = im.createGraphics ();

	project (g2, posture);

	// finalise the last draw actions
	im.flush ();

	return im;
    }

    /**
     * Generate a spherical projection according to the observer posture.
     */
    public void project (Graphics2D g2, ObserverPosture posture)
    {
	sph = new SphericalProjection (posture.getLocation ());
	// no anti-aliasing
	g2.setRenderingHint (RenderingHints.KEY_ANTIALIASING,
			     RenderingHints.VALUE_ANTIALIAS_OFF);
	// set a 2 pixels wide border
	g2.setStroke (new BasicStroke (2.0f,
				       BasicStroke.CAP_ROUND,
				       BasicStroke.JOIN_ROUND));
	g2.setBackground (Color.white);

	// save current paint context
	Paint p = g2.getPaint ();
	
	// draw the projection  background
	g2.setPaint (Color.white);
	g2.fill (new Rectangle2D.Double (0.0, 0.0,
					 2.0*scale, 2.0*scale));
	// set the origin in the middle of the image
	aft.setToTranslation (scale + 1.0,
			      scale + 1.0);
	// rotate the image according to the observer posture
	aft.rotate (posture.getAngleXY ());
	g2.transform (aft);

	// draw the projection disk
	g2.setPaint (Color.black);
	g2.fill (new Ellipse2D.Double (-scale, -scale,
				       2.0*scale - 2.0,
				       2.0*scale - 2.0));

	// draw each faces
	g2.setPaint (Color.white);

	GeneralPath path_list[] = _computeProjection (this.scene);

	if (path_list != null) {
	    for (int i = 0; i < path_list.length; i++) {
		GeneralPath path = path_list[i];
		g2.fill (path);
		g2.draw (path);
	    }
	    g2.setPaint (p);
	}
    }

    // generate an empty image
    private BufferedImage _makeImage ()
    {
	return new BufferedImage ((int) scale * 2, (int) scale * 2,
				  BufferedImage.TYPE_INT_RGB);
    }

    public static void error (String s)
    {
	System.err.println ("(**) " + s);
    }

    public static void debug (String s)
    {
	System.err.println ("-- " + s);
    }

}

// End
