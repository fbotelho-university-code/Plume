package plume.test;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;

import plume.compiler.Compiler;
import plume.compiler.PlumeException;

/**
 * An abstract class to test plume programs.
 * Subclasses will test valid and invalid programs.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: AbstractTest.java 370 2012-05-21 12:08:06Z tcomp000 $
 */
public abstract class AbstractTest {

	/**
	 * Compile the classes in Collection filenames
	 * @param kind Valid/Invalid, used for print message only
	 * @throws PlumeException When the compilation fails
	 */
	protected void compile(String kind) throws PlumeException {
		System.out.println("*** " + (testNumber++) + " _ " + kind + " test "
				+ " _ " + filenames);
		new Compiler(filenames).compile();
		System.out.println("done!");
	}

	/**
	 * Construct
	 * @param filenames The classes to be compiled
	 */
	protected AbstractTest(Collection<String> filenames) {
		this.filenames = filenames;
	}

	/**
	 * Directory of the test suit.
	 */
	protected static final String TESTS_ROOT_DIR = "../tcomp000/test";
//	protected static final String TESTS_ROOT_DIR = "test";

	/**
	 * 
	 * @return A collection of arrays. Each element in the collection is a test
	 *         input. The arrays are of length one since the constructor to this
	 *         class requires a single parameter. Each element in the array
	 *         (that is each test input) is a collection of filenames, as
	 *         required by the Compiler class constructor.
	 */
	protected static Collection<Collection<String>[]> sources(String testsDir) {
		Collection<Collection<String>[]> result = new ArrayList<Collection<String>[]>();
		File testDirectory = new File(testsDir);
		addFilenames(testDirectory, result);
		addDirectories(testDirectory, result);
		return result;
	}

	/**
	 * The input to the compiler: a collection of plume files
	 */
	protected Collection<String> filenames;

	/**
	 * The number of this test
	 */
	protected static int testNumber;

	/**
	 * Add the valid filenames in a given directory to a given collection
	 * @param testDirectory The test directory
	 * @param result @see sources()
	 */
	private static void addFilenames(File testDirectory,
			Collection<Collection<String>[]> result) {
		Collection<String> filenames = new ArrayList<String>();
		for (File file : testDirectory.listFiles(new IsPlumeFileFilter()))
			filenames.add(file.getAbsolutePath());
		Collection<String>[] array = new Collection[1];
		array[0] = filenames;
		result.add(array);
	}

	/**
	 * Add the valid directories in a given directory to a given collection
	 * @param testDirectory The test directory
	 * @param result @see sources()
	 */
	private static void addDirectories(File testDirectory,
			Collection<Collection<String>[]> result) {
		for (File file : testDirectory.listFiles(new IsDirectoryFilter()))
			addFilenames(file, result);
	}

	/**
	 * A class to filter File, accepting only those that are files (and not
	 * directories) and ends with ".plume"
	 */
	private static class IsPlumeFileFilter implements FileFilter {
		public boolean accept(File file) {
			return file.isFile() && file.getName().endsWith(".plume");
		}
	}

	/**
	 * A class to filter File, accepting only those that are directories (and
	 * not files).
	 */
	private static class IsDirectoryFilter implements FileFilter {
		public boolean accept(File file) {
			return file.isDirectory() && !file.getName().equals(".svn");
		}
	}
}
