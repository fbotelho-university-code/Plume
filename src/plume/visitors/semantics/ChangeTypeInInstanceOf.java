/**
 * @author 41625@alunos.fc.ul.pt Fábio Botelho
 * @version $Id 
 * 
 * This dedicated visitors change types of identifiers accordingly to the wanted type. 
 * It is useful for instanceof situations used in if conditions with the purpose of the implicit cast. 
 */
package plume.visitors.semantics;

import java.util.Map;

import plume.attr.*;

import plume.node.AIdentifierExpr;
import plume.node.Node;
import plume.types.Type;
import plume.visitors.PlumeVisitor;


public class ChangeTypeInInstanceOf extends PlumeVisitor {

	public final ClassAttributes currentClass;  
	public final MethodAttributes currentMethod;  
	private Type backUpType =null;
	private Type toChange; 
	private AIdentifierExpr theNode; 
	
	
	/**
	 * The PlumeVisitor constructor for Semantics. 
	 * @param topLevelSymbolTable The top level Symbol Table containing the classes attributes.
	 * @param fileName The filePath for the source associated with this tree. 
	 * @param nodeTypes The Map for node Types. 
	 * @param currentClass The current Class attributes. 
	 * @param currentMethod The current Method attributes. 
	 * @param toChange The type to change to. 
	 */
	public ChangeTypeInInstanceOf(
			Map<String, ClassAttributes> topLevelSymbolTable,
			 String fileName, Map<Node, Type> nodeTypes, ClassAttributes currentClass, MethodAttributes currentMethod, Type toChange) {
		super(topLevelSymbolTable, fileName, nodeTypes);
		this.currentClass = currentClass; 
		this.currentMethod = currentMethod; 
		this.toChange = toChange; 
	}
	
	
		
	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inAIdentifierExpr(plume.node.AIdentifierExpr)
	 */
	@Override
	public void inAIdentifierExpr(AIdentifierExpr node) {
		theNode = node; 
		ParameterAttributes param = currentMethod.getParameter(node.getId().getText());
		if (param == null){
			//try to get from class attributes.
			VariableAttributes fattr = currentClass.getField(node.getId().getText());
			if (fattr != null)
				backUpType = fattr.getType();
			fattr.setType(toChange); 
		}else{
			backUpType = param.getType();
			param.setType(toChange); 
		}
	}
	
	/**
	 * Used to restore the type of the identifier to 
	 * the original type. 
	 */
	public void restoreTypeOfVariable(){
		if (backUpType != null){
		AIdentifierExpr node = theNode; 
		ParameterAttributes param = currentMethod.getParameter(node.getId().getText());
		if (param == null){
			//try to get from class attributes.
			VariableAttributes fattr = currentClass.getField(node.getId().getText());
			if (fattr != null)
				fattr.setType(backUpType); 
		}else{
			param.setType(backUpType); 
		}
		}
	}
}
