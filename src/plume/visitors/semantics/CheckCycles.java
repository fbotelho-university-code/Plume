/**
 * @author 41625@alunos.fc.ul.pt Fábio Botelho
 * @version $Id 
 * This dedicated visitor checks if classes extensions do not have cycles. 
 */
package plume.visitors.semantics;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import plume.attr.*;
import plume.compiler.ErrorList;
import plume.node.AClass;
import plume.node.Node;
import plume.types.Type;
import plume.visitors.PlumeVisitor;

public class CheckCycles extends PlumeVisitor {

	public CheckCycles(Map<String, ClassAttributes> topLevelSymbolTable,
			 String fileName,  Map<Node,Type> nodeTypes) {
		super(topLevelSymbolTable,  fileName,  nodeTypes);
	}
	
	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inAClass(plume.node.AClass)
	 */
	@Override
	public void inAClass(AClass node) {
		ClassAttributes currentClass =topLevelSymbolTable.get(node.getType().getText()); 
		ClassAttributes needle = currentClass;
		Set<ClassAttributes> classes = new HashSet<ClassAttributes>();
		ClassAttributes backup = needle; 
		while ( needle != null && needle.getType() != Type.OBJECT_TYPE ){
			if (classes.contains(needle)){
				backup.setSuper(topLevelSymbolTable.get(Type.OBJECT_TYPE.getClassName()));
				ErrorList.add(node.getExtends(),"Circular extension not allowed", fileName);
				break; 
			}
			classes.add(needle); 
			backup = needle; 
			needle = needle.getSuper(); 
		}
	}
	
}
