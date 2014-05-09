package acdc.codeGen;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import acdc.ast.Assigning;
import acdc.ast.Computing;
import acdc.ast.FloatConsting;
import acdc.ast.IntConsting;
import acdc.ast.Op;
import acdc.ast.SymReferencing;
import acdc.ast.Visitor;
import acdc.ast.Type;


/**
 * Visitor responsible for precomputing Computing expressions that only manipulate Constant operands.
 * For example 2+ 5 is replaced by a constant node 7.
 * The visitor responsible for analysing types and implicit casts must run before (TODO : explain why). We expect variables to be declared. Types changed when forbidden casts is done  
 * @author F‡bio Botelho  - 41625 
 */
public class OptPreCompute extends Visitor{
	private Deque<Double> stack= new ArrayDeque<Double>() ; // Stack para guardar argumentos
	private Map<String,Type> symbolTable; 
	
	public OptPreCompute(Map<String,Type> symbolTable) {
		this.symbolTable = symbolTable; 
	}
	@Override
	public void visit(Assigning node){
		super.visit(node);
		//If stack is not empty then we should replace the expression.
		if (!stack.isEmpty()){
			//get type of variable to know the type of constant we need to create
			Type resultType = symbolTable.get(node.id);
			Double dval = stack.pop(); 
			String val = (resultType == Type.INTEGER) ? String.valueOf( Math.round(Math.floor(dval))) : String.valueOf(dval);   
			node.expr = (resultType == Type.INTEGER) ? new IntConsting(val) : new FloatConsting(val) ;
		}
	}
	
	
	@Override
	public void visit(Computing node) {
		Double left,right;  
		super.visit(node);
		
		switch(node.op){
			case  SQRT:
				if (stack.size() == 1){
					left = stack.pop();
					stack.push(Math.sqrt(left));  //stacking result. 
				}
				else stack.clear(); 
				break; 
			default: 
				if (stack.size() ==  2){
					right = stack.pop(); 
					left = stack.pop();
					Double val = (node.op == Op.PLUS) ? left + right : left - right; 
					stack.push(val); //stacking result. 
				}
				else stack.clear(); 
		}
	}
	
	public void visit(SymReferencing node){
		stack.clear(); 
	}
	@Override
	public void visit(IntConsting node) {
		stack.push(Double.valueOf(node.val)); 
	}

	@Override
	public void visit(FloatConsting node) {
		stack.push(Double.valueOf(node.val)); 
	}
	
}
