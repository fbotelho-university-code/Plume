/**
 * A fixe input file compilation test. Ignore this. 
 * @author 41625@alunos.fc.ul.pt
 * @version $Id 
 */
package plume.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;


public class TestCompiler {
	
	public static void main(String[] args) throws Exception {
		Collection<String> filenames = new ArrayList<String>();
		File dir = new File("/Users/balayhashi/Documents/workspace/trunk/../tcomp000/test/valid/validList"); 
		for (File f : dir.listFiles()) {
		if (!f.isDirectory())
			if (f.getAbsolutePath().endsWith(".plume")){
				filenames.add(f.getAbsolutePath());
			}
		}
		filenames.add("./TestComp.plume");
		filenames.add("./BaseClass.plume");
		Compiler c = new Compiler(filenames) ; 
		c.compile();
		}
}
