/**
 * @author 41625@alunos.fc.ul.pt Fábio Botelho
 * @version $Id 
 */
package plume.visitors.semantics;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import plume.attr.*;

import plume.compiler.ErrorList;
import plume.node.AAddOp;
import plume.node.AAinverseOp;
import plume.node.AAndOp;
import plume.node.ABiggerOp;
import plume.node.ABiggereqOp;
import plume.node.ACastExpr;
import plume.node.AClass;
import plume.node.AConditionalExpr;
import plume.node.ADivOp;
import plume.node.AEqOp;
import plume.node.AErrorExpr;
import plume.node.AFalseExpr;
import plume.node.AFieldAccessExpr;
import plume.node.AIdentifierExpr;
import plume.node.AInstanceofExpr;
import plume.node.AIntegerExpr;
import plume.node.ALowerOp;
import plume.node.ALowereqOp;
import plume.node.AMethodCallExpr;
import plume.node.AMethoddclMember;
import plume.node.AMinusOp;
import plume.node.AModOp;
import plume.node.AMulOp;
import plume.node.ANeqOp;
import plume.node.ANewObjExpr;
import plume.node.ANotOp;
import plume.node.AOpExpr;
import plume.node.AOrOp;
import plume.node.AStringExpr;
import plume.node.ASuperExpr;
import plume.node.AThisExpr;
import plume.node.ATrueExpr;
import plume.node.AType;
import plume.node.Node;
import plume.node.PExpr;
import plume.types.ErrorExpression;
import plume.types.ErrorType;
import plume.types.ExpressionType;
import plume.types.Type;
import plume.visitors.*; 

public class TypeChecking extends PlumeVisitor {
	ClassAttributes currentClass; 
	MethodAttributes currentMethod;
	
	/**
	 * The PlumeVisitor constructor for Semantics. 
	 * @param topLevelSymbolTable The top level Symbol Table containing the classes attributes.
	 * @param fileName The filePath for the source associated with this tree. 
	 * @param nodeTypes The Map for node Types. 
	 */
	public TypeChecking(Map<String, ClassAttributes> topLevelSymbolTable,
			 String fileName, Map<Node,Type> nodeTypes) {
		super(topLevelSymbolTable,  fileName,  nodeTypes);

	}

	@Override
	public void inAClass(AClass node) {
		currentClass = topLevelSymbolTable.get(node.getType().getText());
	}
	
	@Override
	public void outAClass(AClass node) {
		currentClass = null; 
	}
	
	@Override
	public void inAMethoddclMember(AMethoddclMember node) {
		currentMethod =  currentClass.getMethod(node.getId().getText());
	}
	
	
	@Override
	public void outAMethoddclMember(AMethoddclMember node) {
		if (node.getBody() != null){
			Type rslt = nodeTypes.get(node.getType()); 
			Type body = nodeTypes.get(node.getBody());
			if (!isValidType(body, rslt)){
				ErrorList.add(node,"Invalid type in method returned. Found :" + body.getTypeName() + "Expected: " + rslt.getTypeName(), fileName); 
			}
		}
		currentMethod = null; 
	}
	
	private  boolean isValidType ( Type received, Type expected){
		if (received ==  ErrorType.instance  || expected == ErrorType.instance ||  received  == ErrorExpression.instance ){			
			return true;
		}else {
			ClassAttributes recvd = topLevelSymbolTable.get(received.getTypeName());
			ClassAttributes expctd = topLevelSymbolTable.get(expected.getTypeName()); 
			Collection<String> hier = recvd.getStringHierchy(); 
			if (hier.contains(expctd.getType().getTypeName())) return true; 
		}
		return false; 
	}
	
	public void caseAConditionalExpr(AConditionalExpr node){
		if (node.getCond() instanceof AInstanceofExpr){
			AInstanceofExpr instanceOf = (AInstanceofExpr) node.getCond();
			if ((instanceOf.getExpr() instanceof AIdentifierExpr )){
				node.getCond().apply(this);
				Type changedType = nodeTypes.get(instanceOf.getType());
				ChangeTypeInInstanceOf visitor = new ChangeTypeInInstanceOf(topLevelSymbolTable,  fileName, nodeTypes, currentClass, currentMethod, changedType);
				instanceOf.getExpr().apply(visitor);
				node.getThen().apply(this); 
				visitor.restoreTypeOfVariable(); 
				node.getElse().apply(this); 
				outAConditionalExpr(node);
			}
			else super.caseAConditionalExpr(node); 
		}
		else super.caseAConditionalExpr(node); 
	}
	
	@Override
	public void outAConditionalExpr(AConditionalExpr node) {
		Type cond = nodeTypes.get(node.getCond());
		Type then = nodeTypes.get(node.getThen()); 
		Type elsee = nodeTypes.get(node.getElse());
		if (!isValidType(cond, Type.BOOLEAN_TYPE)){
			ErrorList.add(node,"Cannot convert from: " + cond.getTypeName() + " to Boolean", fileName);
		}
		Type result = ErrorType.instance;
		
		if (ErrorType.instance != then && ErrorType.instance != elsee){
			ClassAttributes thenClass =  topLevelSymbolTable.get(then.getTypeName()); 
			ClassAttributes elseeClass =  topLevelSymbolTable.get(elsee.getTypeName());
			if (elsee != ErrorExpression.instance && then != ErrorExpression.instance){
			ClassAttributes res = ClassAttributes.minimumCommonType( thenClass, elseeClass);
			if (res != null){
				//HOW . minimum should always be Object 
				result = res.getType(); 
			}
			}else {
				//Um dos dois é ErrorExpression ou se calhar os dois. Tentamos obter o que nao seja ErrorExpression (possivelmente nao seja...) 
				result = (elsee == ErrorExpression.instance) ? then : elsee ;  
			}
		}
		nodeTypes.put(node, result); 
	}
	
	
	@Override
	public void outAOpExpr(AOpExpr node) {
		ExpressionType expType = (ExpressionType) nodeTypes.get(node.getOp()); 
		Type left = nodeTypes.get(node.getLeft()); 
		Type right = (node.getRight() != null) ? nodeTypes.get(node.getRight()) : null;
		Type result = ErrorType.instance; ;
		if (left != ErrorType.instance && (right == null || right != ErrorType.instance )){
			switch(expType){
			case LOWEREQ: 
			case LOWER: 
			case BIGGEREQ: 
			case BIGGER : 
			case MINUS : 
			case MUL: 
			case DIV : 
			case MOD : 
					if (left != Type.INTEGER_TYPE || right != Type.INTEGER_TYPE){
						ErrorList.add(node, "Invalid operands to arithmetic expression", fileName); 
					}
					else{
						result = ExpressionType.isArithmetic(expType) ?  Type.INTEGER_TYPE : Type.BOOLEAN_TYPE; 
					}
					break ;
				case EQ :
				case NEQ : 
					if (!isValidType(left,right) && !isValidType(right,left)){
						ErrorList.add(node,"Invalid type in right side of comparison operator, expecting : " + left.getTypeName() + "found : " + right.getTypeName(), fileName);  
					}
					else
						result = Type.BOOLEAN_TYPE; 
					break;
				case ADD : 
					if (left == Type.STRING_TYPE && right  == Type.STRING_TYPE){
						result = Type.STRING_TYPE; 
					}else if (left == Type.INTEGER_TYPE && right == Type.INTEGER_TYPE){
						result = Type.INTEGER_TYPE; 
					}else {
						ErrorList.add(node,"Invalid operands to +", fileName);
					}
					break; 
				case NOT : 
					if ( left != Type.BOOLEAN_TYPE){
						ErrorList.add(node,"Expecting boolean", fileName);
					}
					else{
						result = Type.BOOLEAN_TYPE; 
					}
					break; 
				case AINVERSE: 
					if ( left != Type.INTEGER_TYPE){
						ErrorList.add(node,"Invalid operand to unary minus", fileName);
						result = ErrorType.instance; 
					}
					else {
						result = Type.INTEGER_TYPE; 
					}
					break; 
				case AND:
				case OR: 
					if ( left == Type.BOOLEAN_TYPE && right == Type.BOOLEAN_TYPE){
						result = Type.BOOLEAN_TYPE; 
					}else if (left != ErrorExpression.instance && right != ErrorExpression.instance){
						ErrorList.add(node,"Expecting boolean to logical connectives", fileName);
					}
					break; 
			}
		}
		nodeTypes.put(node, result);
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#outAIdentifierExpr(plume.node.AIdentifierExpr)
	 */
	@Override
	public void outAIdentifierExpr(AIdentifierExpr node) {
		ParameterAttributes param = currentMethod.getParameter(node.getId().getText());
		Type result = ErrorType.instance; 
		if (param == null){
			//try to get from class attributes.
			VariableAttributes fattr = currentClass.getField(node.getId().getText());
			if (fattr == null)
				ErrorList.add(node,"Invalid reference to identifier : " + node.getId().getText(), fileName);
			else 
				result = fattr.getType(); 
		}else{
			result = param.getType(); 
		}
		nodeTypes.put(node, result); 
	}
	
	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#outAStringExpr(plume.node.AStringExpr)
	 */
	@Override
	public void outAStringExpr(AStringExpr node) {
		nodeTypes.put(node, Type.STRING_TYPE); 
	}
	
	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#outAIntegerExpr(plume.node.AIntegerExpr)
	 */
	@Override
	public void outAIntegerExpr(AIntegerExpr node) {
		nodeTypes.put(node, Type.INTEGER_TYPE);
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#outATrueExpr(plume.node.ATrueExpr)
	 */
	@Override
	public void outATrueExpr(ATrueExpr node) {
		nodeTypes.put(node, Type.BOOLEAN_TYPE);
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#outAFalseExpr(plume.node.AFalseExpr)
	 */
	@Override
	public void outAFalseExpr(AFalseExpr node) {
		nodeTypes.put(node, Type.BOOLEAN_TYPE);
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#outAErrorExpr(plume.node.AErrorExpr)
	 */
	@Override
	public void outAErrorExpr(AErrorExpr node) {
		nodeTypes.put(node, ErrorExpression.instance);
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#outASuperExpr(plume.node.ASuperExpr)
	 */
	@Override
	public void outASuperExpr(ASuperExpr node) {
		nodeTypes.put(node,currentClass.getSuper().getType()); 
	}

	
	@Override
	public void outAThisExpr(AThisExpr node) {
		nodeTypes.put(node, currentClass.getType());
	}
	
	@Override
	public void inAAddOp(AAddOp node) {
		nodeTypes.put(node, ExpressionType.ADD); 
	}
	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inADivOp(plume.node.ADivOp)
	 */
	@Override
	public void inADivOp(ADivOp node) {
		nodeTypes.put(node, ExpressionType.DIV); 
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inAEqOp(plume.node.AEqOp)
	 */
	@Override
	public void inAEqOp(AEqOp node) {
		nodeTypes.put(node, ExpressionType.EQ); 
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inAAndOp(plume.node.AAndOp)
	 */
	@Override
	public void inAAndOp(AAndOp node) {
		nodeTypes.put(node,  ExpressionType.AND); 
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inANotOp(plume.node.ANotOp)
	 */
	@Override
	public void inANotOp(ANotOp node) {
		nodeTypes.put(node, ExpressionType.NOT); 
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inAOrOp(plume.node.AOrOp)
	 */
	@Override
	public void inAOrOp(AOrOp node) {
		nodeTypes.put(node, ExpressionType.OR); 
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inANeqOp(plume.node.ANeqOp)
	 */
	@Override
	public void inANeqOp(ANeqOp node) {
		nodeTypes.put(node, ExpressionType.NEQ); 
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inALowereqOp(plume.node.ALowereqOp)
	 */
	@Override
	public void inALowereqOp(ALowereqOp node) {
		nodeTypes.put(node, ExpressionType.LOWEREQ); 
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inALowerOp(plume.node.ALowerOp)
	 */
	@Override
	public void inALowerOp(ALowerOp node) {
		nodeTypes.put(node, ExpressionType.LOWER); 
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inABiggereqOp(plume.node.ABiggereqOp)
	 */
	@Override
	public void inABiggereqOp(ABiggereqOp node) {
		nodeTypes.put(node, ExpressionType.BIGGEREQ);
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inABiggerOp(plume.node.ABiggerOp)
	 */
	@Override
	public void inABiggerOp(ABiggerOp node) {
		nodeTypes.put(node, ExpressionType.BIGGER);
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inAMinusOp(plume.node.AMinusOp)
	 */
	@Override
	public void inAMinusOp(AMinusOp node) {
		nodeTypes.put(node, ExpressionType.MINUS);
	}
	
	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inAModOp(plume.node.AModOp)
	 */
	@Override
	public void inAModOp(AModOp node) {
		nodeTypes.put(node, ExpressionType.MOD);
	}
	
	@Override
	public void inAAinverseOp(AAinverseOp node){
		nodeTypes.put(node, ExpressionType.AINVERSE); 
	}
	
	@Override
	public void inAMulOp(AMulOp node) {	
		nodeTypes.put(node, ExpressionType.MUL); 
	}

	@Override
	public void outANewObjExpr(ANewObjExpr node) {
		Type newObjectType = nodeTypes.get(node.getType());
		if (Arrays.asList(Type.BUILTIN_TYPES).contains(newObjectType) && newObjectType != Type.OBJECT_TYPE){
			ErrorList.add(node,"Cannot instantiate primitive type", fileName);
			newObjectType = ErrorType.instance; 
		}
		Type result = ErrorType.instance; 
		if (newObjectType != ErrorType.instance){
			ClassAttributes newClass = topLevelSymbolTable.get(newObjectType.getTypeName());
			if (!newClass.isAbstract()){
				MethodAttributes m = newClass.getConstructor(); 
				result = checkMethodCall(node.getExpr(), result, m, node);
			}
			else {
				ErrorList.add(node, "Cannot instantiate abstract class", fileName);
			}
		}
		nodeTypes.put(node, result);
	}
	
	@Override
	//TODO - no error? 
	public void outAMethodCallExpr(AMethodCallExpr node) {
		Type result = ErrorType.instance;
		Type target =  nodeTypes.get(node.getTarget()); 
		// Must have target : 
		if (target != ErrorType.instance){
			ClassAttributes cl = topLevelSymbolTable.get(target.getTypeName());
			MethodAttributes mattr = cl.getMethod(node.getId().getText());
			result = checkMethodCall(node.getExpr(), result, mattr, node);
		}
		nodeTypes.put(node, result); 
	}
	
	private Type checkMethodCall(List<PExpr> exps, Type result,
			MethodAttributes mattr, Node node) {
		if (mattr != null){
			result = mattr.getReturnType(); 
			if (exps.size() != mattr.getParameterTypes().size()){
				ErrorList.add(node,"No method with that signature could be found ... Different number of parameters in : ", fileName);
			}
			else{
				//We have to check that the method is properly invoked.
				Iterator<PExpr> nodeIt = exps.iterator(); 
				Iterator<Type> typeIt = mattr.getParameterTypes().iterator();
				int i=0; 
				while (nodeIt.hasNext()){
					Type exp = nodeTypes.get(nodeIt.next());
					Type param = typeIt.next();
					if (!isValidType(exp,param)  && param != ErrorType.instance){
						ErrorList.add(node, "Invalid type passed in parameter " + i + " Expecting: " + param.getTypeName() + "Received : " +  exp.getTypeName(),  this.fileName);
					}	
					i++; 
				}
			}
		}
		else{
			ErrorList.add(node, "The method does not exists", fileName); 
		}
		return result;
	}
	
	@Override
	public void outAFieldAccessExpr(AFieldAccessExpr node) {
		Type target = nodeTypes.get(node.getTarget());
		Type result = ErrorType.instance; 
		if (target != ErrorType.instance){
			ClassAttributes cattr = topLevelSymbolTable.get(target.getTypeName());
			String id = node.getId().getText();
			if (!cattr.containsField(id)){
				ErrorList.add(node, "There is no field " + id + " in " + cattr.getType().getClassName(), fileName);
			}
			else{
				result= cattr.getField(id).getType(); 
			}
		}
		nodeTypes.put(node, result); 
	}
	
	@Override
	public void outAInstanceofExpr(AInstanceofExpr node) {
		Type exp = nodeTypes.get(node.getExpr());
		
		Type id = nodeTypes.get(node.getType());
		if (!isValidCast(id,exp) &&  id != ErrorType.instance){
			ErrorList.add(node, "Invalid instanceof expression", fileName);
		}
		nodeTypes.put(node, Type.BOOLEAN_TYPE);
	}
	
	private boolean isValidCast(Type received, Type cast) {
		if (received == cast || received == ErrorExpression.instance || cast == ErrorType.instance  || received == ErrorType.instance) return true; 
		 return isValidType(received, cast)  || isValidType(cast, received);
	}


	@Override
	public void outACastExpr(ACastExpr node) {
		Type exp = nodeTypes.get(node.getExpr());
		Type id = nodeTypes.get(node.getType());
		Type result = id; 
		if (!isValidCast(id, exp)){
			ErrorList.add(node.getExpr(),"Invalid cast", fileName);
			result = ErrorType.instance; 
		}
		nodeTypes.put(node, result); 
	}
	
	@Override
	public void inAType(AType node){
		ClassAttributes cl = topLevelSymbolTable.get(node.getId().getText());
		if (cl != null){
			nodeTypes.put(node, cl.getType()); 
		}
		else{
			nodeTypes.put(node, ErrorType.instance); 
			ErrorList.add(node,"Invalid reference to type: " + node.getId().getText(), fileName);
		}
	}
}
