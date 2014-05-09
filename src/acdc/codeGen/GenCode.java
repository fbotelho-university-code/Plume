package acdc.codeGen;

import acdc.ast.*;

/**
 * Visitor class capable of generating the final code for a dc program from a well-formed AST
 * @author F‡bio Botelho 41625
 */

public class GenCode extends Visitor{

	
	private final StringBuilder finalCode= new StringBuilder();  // the output program
	
	/*
	 * The final output. Must be called after visiting the tree
	 * @returns the String representing the final output program. 
	 */
	public String getCode(){
		return finalCode.toString(); 
	}
	
	/*
	 * Visiting SymDeclaring. Must copy  the initial value of the declaration variable to the register 
	 * (non-Javadoc)
	 * @see acdc.ast.Visitor#visit(acdc.ast.SymDeclaring)
	 */
	@Override
	public void visit(SymDeclaring node) {
		finalCode.append(node.initialValue + "\n"); //push initial value on the stack.
		finalCode.append("s" + node.id + "\n"); // copy to the register
		//TODO - could check if it is printed next 
	}
	
	/*
	 * Visiting Assigning. Must guarantee that the expression is evaluated and 
	 * "poped" to the top of the stack. Only then it can copy the value to the register variable
	 * (non-Javadoc)
	 * @see acdc.ast.Visitor#visit(acdc.ast.Assigning)
	 */
	@Override
	public void visit(Assigning node){
		//Check if assignment has already been considered
		if (!node.constAssigningOnDeclaration){
			//Set the expression result in top of stack: 
			super.visit(node); 
			//Pop & Copy the register if it is not printed on next instruction
			if (!node.isPrintedNext){
				finalCode.append("s" + node.id + "\n"); 
			}
			//else the result stays on top of stack to be printed next. 
		}
	}

	/*
	 * Visiting Computing. Visiting operands sets them on the stack. It is necessary to order operation. 
	 * (non-Javadoc)
	 * @see acdc.ast.Visitor#visit(acdc.ast.Computing)
	 */
	@Override
	public void visit(Computing node) {
		super.visit(node);
		finalCode.append(node.op + "\n"); 
	}
	
	/*
	 * Visiting SymReferencing. Must copy value from register variable to top of the stack
	 * (non-Javadoc)
	 * @see acdc.ast.Visitor#visit(acdc.ast.SymReferencing)
	 */
	@Override
	public void visit(SymReferencing node) {
		finalCode.append("l" + node.id + "\n"); 
	}

	/*
	 * Visiting IntConsting. Push constant to the stack. 
 	 * (non-Javadoc)
	 * @see acdc.ast.Visitor#visit(acdc.ast.IntConsting)
	 */
	@Override
	public void visit(IntConsting node) {
		finalCode.append(node.val + "\n"); 
	}
	
	/*
	 * Visiting FloatConsting. Push constant to the stack. 
	 * (non-Javadoc)
	 * @see acdc.ast.Visitor#visit(acdc.ast.FloatConsting)
	 */
	
	@Override
	public void visit(FloatConsting node) {
		finalCode.append(node.val + "\n"); 
	}

	/*
	 * Visiting Printing. Generates the Printing code for the node variable identifier. 
	 * Operates differently depending where previous operation was the assignment and calculation of the variable value. 
	 * In that case if optimized the value is stacked in the top. 
	 * (non-Javadoc)
	 * @see acdc.ast.Visitor#visit(acdc.ast.Printing)
	 */
	@Override
	public void visit(Printing node){
		if (!node.isStacked){
			//Copy value from register variable to top of the stack. 
			finalCode.append("l" + node.id + "\n");   
			finalCode.append("p\n" + "c"); //Print and clear stack contents.  
			finalCode.append("c\n");
		}
		else{
			finalCode.append("p" + "\n"); //print value
			finalCode.append("s" + node.id + "\n"); //pop to register variable. (clears stack content).   
		}
	}
	
}
