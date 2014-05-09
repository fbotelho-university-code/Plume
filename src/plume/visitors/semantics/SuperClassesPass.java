/**
 * @author 41625@alunos.fc.ul.pt Fábio Botelho
 * @version $Id 
 * 
 * Dedicated visitor to class nodes. Should be applied after all classes are present in the top level symbol table.
 * It updates the table with the class super classes. 
 */
package plume.visitors.semantics;

import java.util.Map;

import plume.attr.*;
import plume.compiler.ErrorList;
import plume.node.AClass;
import plume.node.Node;
import plume.types.ClassType;
import plume.types.ErrorType;
import plume.types.Type;
import plume.visitors.*;

public class SuperClassesPass extends PlumeVisitor {
	
	/**
	 * The PlumeVisitor constructor for Semantics. 
	 * @param topLevelSymbolTable The top level Symbol Table containing the classes attributes.
	 * @param fileName The filePath for the source associated with this tree. 
	 * @param nodeTypes The Map for node Types. 
	 */
	public SuperClassesPass(Map<String, ClassAttributes> topLevelSymbolTable,
			 String fileName,  Map<Node,Type> nodeTypes) {
		super(topLevelSymbolTable,  fileName,  nodeTypes);
	}
	
	
	/* 
	 * Overriding case because we want to stop the visit here.  
	 * @see plume.analysis.DepthFirstAdapter#caseAClass(plume.node.AClass)
	 */
	@Override
	public void caseAClass(AClass node) {
		TypeChecking visit = new TypeChecking(topLevelSymbolTable,  fileName, nodeTypes);
		node.getExtends().apply(visit);
		Type superType = nodeTypes.get(node.getExtends()); 
		ClassAttributes superAttr = (superType == null || superType == ErrorType.instance) ? topLevelSymbolTable.get(Type.OBJECT_TYPE.className) : topLevelSymbolTable.get(superType.getTypeName()) ;
		ClassAttributes  currentClassAttr = topLevelSymbolTable.get(node.getType().getText());

		for (ClassType ty : Type.BUILTIN_TYPES){
			if (superAttr.getType() == ty  && ty!= Type.OBJECT_TYPE){
					ErrorList.add(node.getExtends(),"Cannot extend primitive type: " + superType.getTypeName() , fileName);
					superAttr = topLevelSymbolTable.get(Type.OBJECT_TYPE.getClassName()); 
			}
		}
		if (superAttr == currentClassAttr &&  currentClassAttr.getType() == Type.OBJECT_TYPE){
				superAttr = null; 
				ErrorList.add(node.getExtends(),"Fatal problem solved by hack. Please do not set Object extending Object", fileName);
			}
		currentClassAttr.setSuper(superAttr); 
	}
}
