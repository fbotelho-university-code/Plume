package acdc.parser;

import java.io.CharArrayReader;

import acdc.ast.HowDeepVisitor;
import acdc.ast.HowManyOperatorsVisitor;
import acdc.ast.PrettyPrinterVisitor;
import acdc.ast.Programming;
import acdc.lexer.CharStream;

/**
 * Exercise the parser and scanner on some examples.
 * Exercise as well the various visitor examples.
 * 
 * @author cytron
 * @author Vasco T. Vasconcelos
 * @version $Id: TestParser.java 384 2012-02-13 23:34:27Z vv $
 */
public class TestParser {

	public static void main(String[] args) throws Throwable {

		// Add more examples to this array
		// Each will be tried through the parser
		String[] examples = new String[] {
				"",
				"i a",
				"f z",
				"i a  i b  f c  f d",
				"i a  p a",
				"i a  p a  p a  p a",
				"i a  a = 7  p a",
				"i a  a = 1234567 p a",
				"f z  z = 123456.67890 p z",
				"f b  b = 5.0 - 3.2 + 3.2   p b",
				"f b  i a  a = 5   b = a + 3.2   p a  p b",
				"f b  i a  b = a + 3.2 - b + 4.8 + a - 8.0 + a - a + a + 6 - 5  p b",
				"f b  i a  a = 4  b = 7  b = a + 3.2 - b + 4.8 + a - 8.0 + a - a + a + 6 - 5  p b"
		};

		for (String example : examples) {
			try {
				System.out.println("Parsing: " + example);
				CharArrayReader reader = new CharArrayReader(example.toCharArray());
				CharStream s = new CharStream(reader);
				ParserGeneratingAST p = new ParserGeneratingAST(s);
				p.Prog();
				Programming program = p.getProgram();
				// Visit to pretty print
				program.accept(new PrettyPrinterVisitor());
				// Visit to compute number of operators;
				HowManyOperatorsVisitor howMany = new HowManyOperatorsVisitor();
				program.accept(howMany);
				System.out.println("found " + howMany.getHowManyOps() + " operations");
				// Visit to compute the tree depth
				HowDeepVisitor depth = new HowDeepVisitor();
				program.accept(depth);
				System.out.println("the tree is " + depth.getDepth() + " nodes deep");
				System.out.println("   Parse successful");
			}
			catch (Throwable t) {
				System.out.println("   Parse ended with error: " + t);
				System.out.println("Stack trace: ");
				t.printStackTrace(System.out);
			}
		}
	}
}
