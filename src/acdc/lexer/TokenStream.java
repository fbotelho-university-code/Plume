package acdc.lexer;

import acdc.compiler.ACDCException;

/** 
 * @author cytron
 * @version $Id: TokenStream.java 393 2012-02-14 10:46:38Z vv $
 */
public class TokenStream {

	private Token nextToken;

	public TokenStream(CharStream s) throws ACDCException {
		ScannerCode.init(s);
		advance();
	}

	public int peek() {
		return nextToken.type;
	}

	public Token advance() throws ACDCException {
		Token ans = nextToken;
		nextToken = ScannerCode.Scanner();
		return ans;
	}

}
