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
 * A class to test lexicon invalid ac programs.
 * Compiles ac code and expects an exception.
 * 
 * @author Vasco T. Vasconcelos
 * @author Carlos M‹o de Ferro on 03-03-12 based on similiar classes
 * @version $Id: TestInvalidSyntax.java 394 2012-02-14 13:36:43Z vv $
 */
@RunWith(value=Parameterized.class)
public class TestInvalidLexicon {
	
	@Parameters
	public static Collection<String []> getSourceFiles () {
		return Arrays.asList(new String [][] {
			{"?"}, //tcomp008
			{"/"},//tcomp008
			{"% $"},//tcomp008
			{"~ Û"},//tcomp008
			{"!"},//tcomp008
			{"****"},//tcomp008
			{"/ #"},//tcomp008
			{","},//tcomp008
			{"¼"},//tcomp008
			{"»"},//tcomp008
			{"« `"},//tcomp008
			//{"@"},//tcomp008
			//{"+"},//tcomp008
		});
	}

	@Test(expected=ACDCException.class)
	public void testInvalidLexicon () throws ACDCException {
		System.out.println("*** " + sourceCode);
		new Compiler(sourceCode).compile();
	}

	private String sourceCode;
		
	public TestInvalidLexicon(String fileNames) {
		this.sourceCode = fileNames;
	}
}
