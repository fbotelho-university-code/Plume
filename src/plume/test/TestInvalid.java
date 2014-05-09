package plume.test;

import java.io.File;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import plume.compiler.PlumeException;

/**
 * A class to test invalid plume programs.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: TestInvalid.java 370 2012-05-21 12:08:06Z tcomp000 $
 */
@RunWith(value = Parameterized.class)
public class TestInvalid extends AbstractTest {

	@Test(expected=PlumeException.class)
	public void testInvalid() throws Exception {
		compile("Invalid");
	}

	@Parameters
	public static Collection<Collection<String>[]> invalidSources() {
		return sources(TESTS_ROOT_DIR + File.separatorChar + "invalid");
	}

	public TestInvalid(Collection<String> filenames) {
		super(filenames);
	}
}
