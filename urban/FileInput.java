/* FileInput.java -
 *
 */

package jsphere.urban;

import java.io.*;

public class FileInput
{
    final private static int BUFSIZ = 8 * 1024 + 1;
    //    final private static int BUFSIZ = 10;

    private int start, finish; // buffer start and finish pointers
    private char buf[];        // buffer
    private int line_number;   // current line number
    private FileReader reader; // file reader

    private boolean have_crlf; // for CR/LF treatment
    private boolean is_closed; // is file closed?


    public FileInput (FileReader reader)
	throws IOException
    {
	this.reader = reader;
	buf = new char[BUFSIZ];
	start = 0;
	finish = 0;
	line_number = 1;
	fillbuf ();
	have_crlf = false;
	is_closed = false;
    }

    public int getLineNumber ()
    {
	return line_number;
    }

    public boolean isEOF ()
	throws IOException
    {
	if (start >= finish) {
	    fillbuf ();
	    return start >= finish;
	}
	else
	    return false;
    }

    public char getc ()
	throws IOException, EOFException
    {
	if (start >= finish)
	    fillbuf ();
	if (start < finish) {
	    char c = buf [start];
	    start++;
	    if (c == '\r') {
		have_crlf = true;
		c = getc ();
		if (c != '\n')
		    ungetc ();
		have_crlf = false;
		line_number++;
		return '\n';
	    }
	    else if ((c == '\n') || (c == '\r')) {
		if (!have_crlf)
		    line_number++;
	    }
	    return c;
	}
	throw new EOFException ();
    }

    public boolean peekc (char c)
	throws IOException
    {
	if (start >= finish)
	    fillbuf ();
	if (start < finish) {
	    if (buf [start] == c) {
		start++;
		return true;
	    }
	    else
		return false;
	}
	return false;
    }

    public void ungetc ()
    {
	if (start == 0)
	    throw new InternalError ("ungetc");

	start--;
	if ((buf[start] == '\n') || (buf[start] == '\r')) {
	    line_number--;
	}
    }

    private void fillbuf ()
	throws IOException
    {
	int len = finish - start;
	System.arraycopy (buf, start, buf, 0, len);
	start = 0;
	finish = len;
	
	len = buf.length - len;
	len = reader.read (buf, finish, len);
	if (len >= 0)
	    finish += len;
	else
	    close ();
    }

    public void close ()
    {
	try {
	    if ((reader != null) && !is_closed)
		reader.close ();
	    is_closed = true;
	}
	catch (IOException e) {}
    }
}

// End
