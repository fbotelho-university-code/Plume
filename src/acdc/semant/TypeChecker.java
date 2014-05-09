package acdc.semant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import acdc.ast.Assigning;
import acdc.ast.Computing;
import acdc.ast.FloatConsting;
import acdc.ast.IntConsting;
import acdc.ast.SymReferencing;
import acdc.ast.Type;
import acdc.ast.Visitor;
/**
 * Visitor responsible for checking the types conversions made implicity by the code in ac.
 * Currently implicit conversions are allowed except when trying to assigning some float expression or value to an integer variable
 * Changes the content of the symbol table and generates erros when forbidden implicit casts are detected.
 * 
 * @author Fábio Botelho - 41625
 *
 */
public class TypeChecker extends Visitor{
	private final Deque<Type> stack = new ArrayDeque<Type>();
	private final Map<String,Type> symbolTable;
	private final List<String> erros;
	
	/**
	 * Construtor do Visitor
	 * @param map A tabela de simbolos previamente formada. Pode ser alterada no visitor. 
	 * @param erros A lista de erros do processo de compilação. 
	 */
	public TypeChecker(Map<String,Type> map, List<String> erros){
		symbolTable = map; 
		this.erros = erros; 
	}
	
	@Override
	public void visit(Assigning node) {
		//TODO check if variable exists. 
		Type left =  symbolTable.get(node.id); 
		stack.push(left); 
		super.visit(node); 
		boolean  safe = checkImplicitCastError();
		if (!safe){
			erros.add("May loose precision on implicit conversion from float to int ");
		}
		Type result = stack.pop(); 
		if (result != left){
			symbolTable.put(node.id, result); 
		}
		super.visit(node); //TODO check this
	}

	public void visit(SymReferencing node){
		stack.push(symbolTable.get(node.id)); 
		super.visit(node);
	}

	@Override
	public void visit(IntConsting node) {
		stack.push(Type.INTEGER);
		super.visit(node);
	}
	
	@Override
	public void visit(FloatConsting node) {
		stack.push(Type.FLOAT); 
		super.visit(node);
	}

	public void visit(Computing node){
		super.visit(node);
		//Type left = stack.pop(); 
		if  (node.right  == null) {
			stack.push(Type.INTEGER);
		}
		checkImplicitCastError(); 
	}
	
	private boolean checkImplicitCastError() {
		Type right =  stack.pop(); 
		Type left = stack.pop();
		//TODO use peek()
		if (left == Type.INTEGER && right == Type.FLOAT){
			stack.push(Type.FLOAT); //TODO CHECK THIS 
			return false; 
		}
		if ((left == right) && right == Type.INTEGER){
			stack.push(Type.INTEGER); 
		}
		else{
			stack.push(Type.FLOAT); 
		}
		return true; 
	}
	
	
}
