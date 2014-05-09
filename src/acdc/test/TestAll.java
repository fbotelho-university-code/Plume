package acdc.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * A class to test ac programs.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: TestAll.java 388 2012-02-13 23:49:42Z vv $
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = { TestValid.class, TestInvalidSyntax.class,
		TestInvalidSemantics.class })

public class TestAll {
	
}
