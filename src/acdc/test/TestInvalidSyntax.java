package acdc.test;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import acdc.compiler.ACDCException;
import acdc.compiler.Compiler;

/**
 * A class to test syntactically invalid ac programs.
 * Compiles ac code and expects an exception.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: TestInvalidSyntax.java 394 2012-02-14 13:36:43Z vv $
 */
@RunWith(value=Parameterized.class)
public class TestInvalidSyntax {
	
	@Parameters
	public static Collection<String []> getSourceFiles () {
		return Arrays.asList(new String [][] {
			{"i"},
			{"f"},
			{"p"},
			{"a"},
			{"3"},
			{"4.5"},
			{"i i"},
			{"a a"},
			{"f f"},
			{"f i"},
			{"p p"},
			{"p 5"},
			{"i a  i"},
			{"i a  p"},
			{"a ="},
			{"a = p"},
			{"f = 5"},
			{"a = 5 +"},
			{"a = - 5"},
			{"a = 1 + 2 + 3 + 4 + 5 +"},
			{"a = 1 + 2 + 3 + + 4 + 5"},
			{"% %"},
			{"i a  a = 3  i b  b = a + 3  p b"},
			{"++"},
			{"a = 3++"},
			{"a = @3"},
			{"a = b @ 3"},
			{"a = @"} //tcomp008
		});
	}

	@Test(expected=ACDCException.class)
	public void testInvalidSyntax () throws ACDCException {
		System.out.println("*** " + sourceCode);
		new Compiler(sourceCode).compile();
	}

	private String sourceCode;
		
	public TestInvalidSyntax(String fileNames) {
		this.sourceCode = fileNames;
	}
}
