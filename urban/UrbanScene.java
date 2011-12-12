/* UrbanScene.java - urban scene representation
 *
 */

package jsphere.urban;

import jsphere.geom.Face;

import java.io.File;

public interface UrbanScene
{
    public String getID ();

    public void addFace (Face face);
    public Face getFace (int i);
    public int size ();

    public void parse (File file);
    //    public void output (File file);
}

// End
