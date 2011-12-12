/* Observer.java - an observer in urban open spaces
 *
 */

package jsphere.observer;

import jsphere.observer.ObserverPath;
import jsphere.observer.ObserverPosture;

public class Observer
{
    // path followed by the observer
    private ObserverPath path;
    // current observer posture
    private ObserverPosture current_post;
    // current observer step
    private double current_step;
    // observer step ratio
    private double step;

    public Observer ( ObserverPath path )
    {
	this ( path, path.getPreferredStep () );
    }

    public Observer ( ObserverPath path, double step )
    {
	this.path = path;
	this.current_step = 0.0;
	this.current_post = path.getPosture ( 0.0 );
	// If the step is closed to 0.0, the observer will not advance
	if (Math.abs ( step ) < 1e-7)
	    this.step = path.getPreferredStep ();
	else
	    this.step = step;
    }

    /**
     * Get current step in the route.
     */
    public double getCurrentStep () { return current_step; }

    /**
     * Get the length of a step.
     */
    public double getStepLength () { return step; }

    /**
     * Get the current orientation of the observer.
     */
    public ObserverPosture getPosture () { return current_post; }

    /**
     * Get the orientation of the observer at 'time' t.
     */
    public ObserverPosture getPosture (double t)
    {
	return path.getPosture (t);
    }

    /**
     * Check if the observer is at the end of his route.
     */
    public boolean atEnd () { return current_step > (1.0 + 1e-5); }
    
    /**
     * Advance the observer one step ahead.
     */
    public void advance ()
    {
	current_step += step;
	current_post = path.getPosture (current_step);
    }
    
    public static void main (String args[])
    {
        ObserverPath path = new ObserverPath ();
        
        path.addPoint (new jsphere.geom.Point3D (-50, 5, 1.6));
        path.addPoint (new jsphere.geom.Point3D (60, 5, 1.6));

        Observer obs = new Observer (path, 0.01);
        int i = 0;
        while (!obs.atEnd ()) {
            System.out.println ("[" + obs.getCurrentStep () + "] " + obs.getPosture ());
            obs.advance ();
        }
    }
}

// End
