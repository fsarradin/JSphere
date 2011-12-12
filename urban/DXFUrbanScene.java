/* DXFUrbanScene.java -
 *
 */

package jsphere.urban;

import java.io.*;

public class DXFUrbanScene
    //    extends AbstractUrbanScene
{
    private FileInput in;

    public DXFUrbanScene ()
    {
    }

    private void _readWhite ()
	throws IOException
    {
	char c = in.getc ();
	while ((c == '\n') || (c == '\t') || (c == ' '))
	    c = in.getc ();
	in.ungetc ();
    }


    private void _readToken ()
	throws IOException
    {
	if (in.isEOF ()) {
	    System.out.println ("<EOF>");
	    return;
	}

	char c = in.getc ();
	
	switch (c) {
	case '\n':
	case ' ':
	case '\t':
	    in.ungetc ();
	    _readWhite ();
	    //	    System.out.println ("<WHITE>");
	    return;
	case '#':
	    in.ungetc ();
	    readComment ();
	    return;
	}
	if (((c >= '0') && (c <= '9'))
	    || (c == '.') || (c == '-') || (c == '+')) {
	    in.ungetc ();
	    readNum ();
	}
	else if (((c >= 'a') && (c <= 'z'))
		 || ((c >= 'A') && (c <= 'Z'))
		 || (c == '_')) {
	    in.ungetc ();
	    readIdent ();
	}
	else
	    System.out.println ("Char: " + c);

    }

    public void parse (File file)
    {
	try {
	    in = new FileInput (new FileReader (file));
	    for (;;) {
		_readToken ();
	    }
	}
	catch (EOFException e) {
	    /* ? */
	}
	catch (IOException e) {
	    e.printStackTrace ();
	}
    }

    public void output (File file)
    {
	System.err.println ("DXFUrbanScene.output() not implemented");
    }

    public static void main (String args[])
    {
    }
}

// End
