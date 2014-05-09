/**
 * @author 41625@alunos.fc.ul.pt Fábio Botelho
 * @version $Id
 * 
 *  This dedicated visitor is intended to  visit only  Class nodes. 
 *  It does not traverse the tree. And updates the topLevel Symbol Table (passed  as a constructor field) 
 *  with the attributes of the classes found.  
 *  
 */
package plume.visitors.semantics;

import java.util.Map;
import plume.attr.*;
import plume.compiler.ErrorList;
import plume.node.AClass;
import plume.node.Node;
import plume.types.ClassType;
import plume.types.Type;
import plume.visitors.PlumeVisitor;


public class AllClassesVisitor extends PlumeVisitor {

	/**
	 * The PlumeVisitor constructor for Semantics. 
	 * @param topLevelSymbolTable The top level Symbol Table containing the classes attributes.
	 * @param fileName The filePath for the source associated with this tree. 
	 * @param nodeTypes The Map for node Types. 
	 */
	public AllClassesVisitor(Map<String, ClassAttributes> topLevelSymbolTable,
			 String fileName, Map<Node,Type> nodeTypes) {
		super(topLevelSymbolTable,  fileName, nodeTypes);
	}

	/* 
	 * Using case to stop visiting the tree since we are only interested in 
	 * analysing the Class node. 
	 */
	@Override
	public void caseAClass(AClass node) {
		boolean isAbstract = (node.getKabstract() != null) ? true: false;
		String  className = node.getType().getText();
		if (!fileName.endsWith( className + ".plume")){
			ErrorList.add(node.getType(), "Class name " +  className + "does not match the filename ", fileName);
		}
		if (!topLevelSymbolTable.containsKey( className)){
			topLevelSymbolTable.put( className, new ClassAttributes(new ClassType( className), isAbstract, fileName, node));
		}
		else{
			ErrorList.add(node,"Class name found : " +  className + "Was previously declared at : " + topLevelSymbolTable.get( className).filePath, fileName); 
		}
	}
}
