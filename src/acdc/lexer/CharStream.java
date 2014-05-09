package acdc.lexer;

import java.io.Reader;

/**
 * Provides peek, EOF, and advance for the chapter 2 scanner
 * 
 * @author cytron
 * @version $Id: CharStream.java 384 2012-02-13 23:34:27Z vv $
 */
public class CharStream {

	public final static char BLANK = ' ';

	private final Reader is;

	private char nextChar;
	private boolean eof;

	public CharStream(Reader ds) {
		this.is = ds;
		this.eof = false;
		this.nextChar = 0;
		advance();
	}

	public char peek() {
		return nextChar;
	}

	public boolean EOF() {
		return eof;
	}

	public char advance() {
		char ans = nextChar;
		try {
			int next = is.read();
			//
			// If end of file, read will return -1
			//
			if (next == -1) {
				eof = true;
				nextChar = 0;
			} else {
				nextChar = (char) next;
			}
		}
		//
		// On any problem, just assume end of input
		//
		catch (Throwable t) {
			System.out.println("Error encountered " + t);
			eof = true;
			return 0;
		}
		return ans;
	}
}
