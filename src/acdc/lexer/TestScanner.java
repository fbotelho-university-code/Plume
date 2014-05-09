package acdc.lexer;

import java.io.CharArrayReader;

import acdc.compiler.ACDCException;

/**
 * Testing the Scanner
 * @author Vasco T. Vasconcelos
 * @version $Id: TestScanner.java 393 2012-02-14 10:46:38Z vv $
 *
 */
public class TestScanner {

	static String example = "f b   i a   a = 5   b = a + 3.2   p b";
	public static void main(String[] args) throws ACDCException {
		CharArrayReader reader = new CharArrayReader(example.toCharArray());
		CharStream s = new CharStream(reader);
		ScannerCode.init(s);
		Token token;
		do {
		 token = ScannerCode.Scanner();
		 System.out.println(token);
		} while (token.type != Token.EOF);
	}
}
