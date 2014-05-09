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
 * A class to test syntactically correct but
 * semantically invalid ac programs.
 * Compiles ac code and expects an exception.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: TestInvalidSemantics.java 394 2012-02-14 13:36:43Z vv $
 */
@RunWith(value=Parameterized.class)
public class TestInvalidSemantics {
	
	@Parameters
	public static Collection<String []> getSourceFiles () {
		return Arrays.asList(new String [][] {
			{"i z  i z"},		// redeclaration same type
			{"i z  f z"},		// redeclaration different type
			{"p z"},			// non declared var in assignment, left
			{"z = 5"},			// undeclared var in print
			{"f z  z = w"},		// non declared var in assignment, right
			{"i z  z = 3.0"},	// wrong type in assignment
			{"i z  z = 1 + 2 + 3 + 4.0 + 5 + 6 + 7"},	// wrong type in assignment
			{"f y i k i t y = 3.0@ k = 3@ t = k + y"}, //namartins, wrong type in assignment with variables 
			{"i a a = 2 + 2.0 @"},  //  Result of 2 + 2.0 @ yields float. Wrong type in assignment.
		});
	}
	@Test(expected=ACDCException.class)
	public void testInvalidSyntax () throws ACDCException {
		System.out.println("*** " + sourceCode);
		new Compiler(sourceCode).compile();
	}

	private String sourceCode;
		
	public TestInvalidSemantics(String fileNames) {
		this.sourceCode = fileNames;
	}
}
