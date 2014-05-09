package plume.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * A class to test all plume programs.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: TestAll.java 370 2012-05-21 12:08:06Z tcomp000 $
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = { TestValid.class, TestInvalid.class })
public class TestAll {
}
