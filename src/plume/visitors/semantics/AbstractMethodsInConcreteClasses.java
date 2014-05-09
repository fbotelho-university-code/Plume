/**
 * @author 41625@alunos.fc.ul.pt Fábio Botelho
 * @version $Id 
 * This dedicated visitor visits only class nodes checking for possibly unimplemented methods. 
 */
package plume.visitors.semantics;

import java.util.Map;
import plume.attr.*;
import plume.compiler.ErrorList;
import plume.node.AClass;
import plume.node.Node;
import plume.types.Type;
import plume.visitors.PlumeVisitor;


public class AbstractMethodsInConcreteClasses extends PlumeVisitor {

	
	/**
	 * The PlumeVisitor constructor for Semantics. 
	 * @param topLevelSymbolTable The top level Symbol Table containing the classes attributes.
	 * @param fileName The filePath for the source associated with this tree. 
	 * @param nodeTypes The Map for node Types. 
	 */
	public AbstractMethodsInConcreteClasses(
			Map<String, ClassAttributes> topLevelSymbolTable,
			 String fileName, Map<Node,Type> nodeTypes) {
		super(topLevelSymbolTable,  fileName, nodeTypes);
	}

	@Override
	public void inAClass(AClass node) {
		ClassAttributes currentClass =topLevelSymbolTable.get(node.getType().getText());  
		if (!currentClass.isAbstract()){
			for ( String m : currentClass.getAbstractMethodsToImplement()){
				if (!currentClass.methodDeclaredLocally(m)){
					ErrorList.add(node,"Concrete class "+ currentClass.getType().getClassName() + " does not implement the abstract method : " + m, fileName);
				}
			}
		}
	}
	
}
