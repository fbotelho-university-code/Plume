package plume.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * A class to test valid plume programs.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: TestValid.java 370 2012-05-21 12:08:06Z tcomp000 $
 */
@RunWith(value = Parameterized.class)
public class TestValid extends AbstractTest {

	@Parameters
	public static Collection<Collection<String>[]> validSources() {
		return sources(TESTS_ROOT_DIR + File.separatorChar + "valid");
	}

	public TestValid(Collection<String> filenames) {
		super(filenames);
	}

	@Test
	public void testValid() throws Exception {
		compile("Valid");
		checkOutput();
		System.out.println("output check done!");
	}

	private void checkOutput() throws IOException, InterruptedException {
		for (String startClass : expectFilenames()) {
			System.out.println("Testing expects"); 
			String directory = startClass.substring(0,
					startClass.lastIndexOf(File.separatorChar));
			String classfile = startClass.substring(startClass
					.lastIndexOf(File.separatorChar) + 1);
			System.out.println("Running java -classpath " + directory + " " + classfile);
			String[] command = { "java", "-classpath", directory, classfile };
			Process process = Runtime.getRuntime().exec(command);
			compareOutput(process, startClass);
			int exitCode = process.waitFor();
			assertEquals(exitCode, 0);
		}
	}

	/**
	 * Check the java output against the expected output
	 * 
	 * @param process
	 *            The process running the java interpreter
	 * @param expectedResult
	 * @throws IOException
	 *             When one of the BufferedReaders fail
	 */
	private static void compareOutput(Process process, String startClass)
			throws IOException {
		BufferedReader found = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		BufferedReader expected = new BufferedReader(new FileReader(startClass
				+ ".expect"));
		System.out.println("Comparing to " + startClass + ".expect");
		boolean areSame =sameContents(found, expected); 
		assertEquals(true, areSame);
		expected.close();
		found.close();
	}

	/**
	 * Are two BufferedReaders equal in contents?
	 * 
	 * @param found
	 *            One reader
	 * @param expected
	 *            The other reader
	 * @throws IOException
	 *             When one readLine() fails
	 */
	private static boolean sameContents(BufferedReader found,
			BufferedReader expected) throws IOException {
		String lf;
		System.out.println("Found\t==\tExpected:");
		while ((lf = found.readLine()) != null) {
			System.out.print(lf + "\t==\t");
			String s = expected.readLine();
			System.out.println(s);
			if (!lf.equals(s))
				return false;
		}
		return expected.readLine() == null;
	}

	protected Collection<String> expectFilenames() {
		Collection<String> result = new ArrayList<String>();
		if (!filenames.isEmpty()) {
			String aFilename = filenames.iterator().next();
			File testDirectory = new File(aFilename.substring(0,
					aFilename.lastIndexOf(File.separatorChar)));
			for (File file : testDirectory.listFiles(new IsExpectFileFilter())) {
				String filename = file.getAbsolutePath();
				result.add(filename.substring(0, filename.length() - 7));
			}
		}
		return result;
	}

	/**
	 * A class to filter File, accepting only those that are files (and not
	 * directories) and ends with ".expect"
	 */
	private static class IsExpectFileFilter implements FileFilter {
		public boolean accept(File file) {
			return file.isFile() && file.getName().endsWith(".expect");
		}
	}
}
