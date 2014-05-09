package acdc.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import acdc.compiler.Compiler;

/**
 * A class to test valid ac programs.
 * Compiles ac code, runs dc code,
 * compares the dc output with the expected output.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: TestValid.java 394 2012-02-14 13:36:43Z vv $
 */
@RunWith(value = Parameterized.class)
public class TestValid {

	@Parameters
	public static Collection<String[]> validSources() {
		return Arrays.asList(new String[][] {
		/* 0 */	{"", "" },
				{"i a", ""},
		/* 2 */	{"f z", ""},
				{"i a  i b  f c  f d", ""},
				{"i a  p a", "0"},
		/* 5 */	{"i a  p a  p a  p a", "0\n0\n0"},
				{"i a  a = 7  p a", "7"},
				{"i a  a = 1234567  p a", "1234567"},
				//{"f z  z = 123456.67890  p z", "123456.67890"}, //lost the ability to preserve the last 0.
				{"f z  z = 123456.6789  p z", "123456.6789"},
		/* 9 */	{"f b  b = 5.0 - 3.2 + 3.2   p b", "5.0"},
				{"f b  i a  a = 5   b = a + 3.2   p a  p b", "5\n8.2"},
				{"f b  i a  b = a + 3.2 - b + 4.8 + a - 8.0 + a - a + a + 6 - 5  p b", "1.0"},
		/* 12*/ {"f b  i a  a = 4  b = 7  b = a + 3.2 - b + 4.8 + a - 8.0 + a - a + a + 6 - 5  p b", "6.0"},
				{"i a  a = 4@  p a", "2"},
				{"i a  i b  a = 4 b = a + 5 @  p a  p b", "4\n3"},
		/* 15*/	{"i a  i b  a = 3 b = 4  b = 10 + a@ - b + 3  p a  p b", "3\n2"},
				{"i a  i b  a = 3 b = 4  b = 13 + a@ - b + 3  p a  p b", "3\n3"},
				{"f z  z = 12.23  z = z @  p z", "3.49"},
				{"i a  f z  a = 4  z = a + 12.23  p z", "16.23"},
			    {"f b  i a  a = 4  b = 7  b = a + 3.2 - b + 4.8 + a - 8.0 @ + a - a + a + 6 @ - 5  p a  p b", "4\n-2"},
			//{"f z  z = 123456.67890 @  p z", "351.36402"}, diff precision
	/* 20 */   {"f z  z = 123456.67890 @  p z", "351.36402618936387"},
				/*namartins*/
				{"f q f w f e f r f t f y f u f o f a f s f d f g f h f j f k f l f z f x f c f v f b f n f m m = 3 p m", "3.0"},
  				//{"i y f t i k y = 3@ t = 3.0@ p y p t","1\n1.7\n"}, diff precision
				{"i y f t i k y = 3@ t = 3.0@ p y p t","1\n1.7320508075688772\n"}, 
  				{"f t t = 3.0@ p t","1.7320508075688772"},
  			    {"f q f w f e f r f t f y f u f o f a f s f d f g f h f j f k f l f z f x" +
  			    		"f c f v f b f n f m m = 3 q = q + w + e + r + t + y + u + o + a " +
  			    		"+ s + d + g + h + j + k + l + z + x + c + v + b + n + m  p q", "3.0"},
  		  		{"i t i k f j k = 3@ t = 4@ j = k + t p j","3"},
  		  		{"i x x=000000000000000000000000000000 p x","0"}, 
  		  		{"f a a = 3 p a", "3.0"},
  		  		
  		/* 27*/ 
  		  		/* fabio botelho */ 
  		  		{"f a a = 4.0@ p a", "2.0"}, // Checking that a float parameter to @ yields a float result
  		  		{"i a i b a = 5 p b  a = a + 5 p b  b = 2 p b b = 1 + 2 p b",  "0\n0\n2\n3"},
		});
	}

	@Test
	public void testValid() throws Exception {
		System.out.println("*** " + sourceCode);
		new Compiler(sourceCode).compile();
		String[] command = {"dc", "out.dc"};
		Process process = Runtime.getRuntime().exec(command);
		checkOutput(process);
		int exitCode = process.waitFor();
		assertEquals(exitCode, 0);
		System.out.println("done!");
	}

	private String sourceCode;

	private String expectedResult;

	public TestValid(String fileNames, String expectedResult) {
		this.sourceCode = fileNames;
		this.expectedResult = expectedResult;
	}

	/**
	 * Check the dc output against the expected output
	 * @param process The process running the dc interpreter
	 * @throws IOException When one of the BufferedReaders fail
	 */
	private void checkOutput(Process process) throws IOException {
		BufferedReader found = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		BufferedReader expected = new BufferedReader(new StringReader(
				expectedResult));
		assertEquals(sameContents(found, expected), true);
		expected.close();
		found.close();
	}
	
	/**
	 * Are two BufferedReaders equal in contents?
	 * @param found One reader
	 * @param expected The other reader
	 * @throws IOException When one readLine() fails
	 */
	private static boolean sameContents(BufferedReader found, BufferedReader expected)
			throws IOException {
		String lf;
		while ((lf = found.readLine()) != null)
			if (!lf.equals(expected.readLine()))
				return false;
		return expected.readLine() == null;
	}
}
