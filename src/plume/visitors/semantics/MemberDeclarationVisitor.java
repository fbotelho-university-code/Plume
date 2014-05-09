/**
 * @author 41625@alunos.fc.ul.pt Fábio Botelho
 * @version $Id 
 * 
 * This dedicated visitors fill the topLevelSymbol table with the attributes for fields and methods.
 * It visits the attributes of classes in an hierarchical order. (Super's first).  
 */
package plume.visitors.semantics;

import java.util.Iterator;
import java.util.Map;

import plume.attr.*;

import plume.compiler.ErrorList;
import plume.node.AArg;
import plume.node.AClass;
import plume.node.AFielddclMember;
import plume.node.AMethoddclMember;
import plume.node.Node;
import plume.types.ErrorType;
import plume.types.Type;

import plume.visitors.*; 

public class MemberDeclarationVisitor extends PlumeVisitor {
	
	private ClassAttributes currentClass = null; 
	private MethodAttributes currentMethod = null; 
	private TypeChecking visitTypes;  
	
	@Override
	public void setFileName(String f ){
		visitTypes.setFileName(f); 
		super.setFileName(f); 
	}
	
	/**
	 * The PlumeVisitor constructor for Semantics. 
	 * @param topLevelSymbolTable The top level Symbol Table containing the classes attributes.
	 * @param fileName The filePath for the source associated with this tree. 
	 * @param nodeTypes The Map for node Types. 
	 */
	public MemberDeclarationVisitor(
			Map<String, ClassAttributes> topLevelSymbolTable,
			 String fileName, Map<Node,Type> nodeTypes) {
		super(topLevelSymbolTable,  fileName, nodeTypes);
		visitTypes = new TypeChecking(topLevelSymbolTable, fileName, nodeTypes); 
	}
	
	/* Entering Class: set the  currentClass  to appropiate object in symbol table. 
	 */
	@Override
	public void caseAClass(AClass node) {
		//Visit parent first. 
		currentClass = topLevelSymbolTable.get(node.getType().getText());
		if (!currentClass.beenVisited(this)){
			if ( currentClass.getType() != Type.OBJECT_TYPE && !currentClass.getSuper().beenVisited(this) && !currentClass.getSuper().isPrimitive()){
				String fileBackup = fileName;
				fileName = currentClass.getSuper().filePath;
				ClassAttributes back = currentClass; 
				currentClass.getSuper().start.apply(this);
				currentClass = back; 
				fileName = fileBackup; 
			}
			currentClass.visit(this); 
			super.caseAClass(node); 
		}
	}
	
	@Override
	public void inAFielddclMember(AFielddclMember node) {
		String fieldName = node.getId().getText(); 
		Type nodeType = ErrorType.instance;
		if (currentClass.containsField(fieldName)){
			if (currentClass.isFieldDeclaredLocally(fieldName)){
				ErrorList.add(node.getId(),"Field overriding not allowed", fileName); 
			}else{
				ErrorList.add(node.getId(),"Redeclaration of field", fileName);
			}
		}
		else{
			node.getType().apply(visitTypes); 
			nodeType = nodeTypes.get(node.getType());
		}
		//nodeTypes.put(node, nodeType); 
		currentClass.putField(fieldName, new FieldAttributes(nodeType));
	}
	
	@Override
	public void inAMethoddclMember(AMethoddclMember node) {

		String methodName = node.getId().getText();
		MethodAttributes methodAttr = new MethodAttributes(currentClass, node.getBody() == null , node.getKoverride() != null, methodName);
		//TODO - i don't think return type should be set to error on every situation. 

		if (methodAttr.isAbstract() && !currentClass.isAbstract()){
			ErrorList.add(node.getId(),"Abstract method on non abstract class", fileName);
		}
		else if (currentClass.containsMethod(methodName)){
			if (currentClass.methodDeclaredLocally(methodName)){
				ErrorList.add(node,"Redeclaration of method", fileName);
			}else 
				if (!methodAttr.isOverride()){
					ErrorList.add(node,"Override keyword is mandatory but is missing" , fileName);
				}
		}else if (methodAttr.isOverride()){
			ErrorList.add(node.getKoverride(),"Invalid override. Method not present in upper classes", fileName);
		}
		node.getType().apply(visitTypes); 
		Type nodeType = nodeTypes.get(node.getType());
		methodAttr.setReturnType(nodeType);

		currentClass.putMethod(methodName, methodAttr); 
		currentMethod = methodAttr;
	}
	
	@Override
	public void inAArg(AArg node) {
		String argName = node.getId().getText();
		Type fieldType = ErrorType.instance;
		if (currentMethod.containsField(argName)){
			ErrorList.add(node,"Duplicate argument name", fileName); 
		}
		else{
			node.getType().apply(visitTypes); 
			fieldType = nodeTypes.get(node.getType());
		}
		if (fieldType == ErrorType.instance){
			currentMethod.setReturnType(ErrorType.instance);
		}
		ParameterAttributes fattr =  new ParameterAttributes(fieldType, currentMethod.getLocals() + 1 );
		currentMethod.putParameter(argName, fattr); 
		nodeTypes.put(node,  fieldType);
	}
	
	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inAMethoddcl(plume.node.AMethoddcl)
	 */
	@Override
	public void outAMethoddclMember(AMethoddclMember node) {
		//TODO : needed because of the hack. 
		if (currentClass.getType() != Type.OBJECT_TYPE){
			if (currentMethod.isOverride()){
				MethodAttributes superMethod = currentClass.getSuper().getMethod(node.getId().getText()); 
				if (superMethod != null){
					Iterator<Type> currentArgs = currentMethod.getParameterTypes().iterator(); 
					Iterator<Type> superArgs = superMethod.getParameterTypes().iterator();
					
					while (currentArgs.hasNext()){
						if (superArgs.hasNext()){
							if (currentArgs.next() != superArgs.next()){
								ErrorList.add(node,"Different signatures of methods of overriden method", fileName); 
							}
						}else{
							ErrorList.add(node,"Different signatures of methods of overriden method", fileName); 
							break; 
						}
					}
					if (superArgs.hasNext()){
						ErrorList.add(node,"Different signatures of methods of overriden method", fileName); 
					}
				}
			}
		}
		currentMethod = null; 
	}
	
	//Leaving a Class : set the currentClass to null
	@Override
	public void outAClass(AClass node){
		currentClass = null;
	}
	
}
