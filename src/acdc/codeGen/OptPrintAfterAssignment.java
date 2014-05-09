package acdc.codeGen;

import java.util.ArrayDeque;
import java.util.Deque;

import acdc.ast.Assigning;
import acdc.ast.Computing;
import acdc.ast.Printing;
import acdc.ast.SymDeclaring;
import acdc.ast.Visitor;

/**
 * Visitor decorates trees to find printing operations of a variable immediately after assignment of that variable. 
 * @author F‡bio Botelho - 41625 
 */

public class OptPrintAfterAssignment extends Visitor{
	private Deque<Assigning> stack = new ArrayDeque<Assigning>();
	private final Assigning sentinel = new Assigning(null, null); 
	//private final Printing sentinel = new Printing("sentinel");
	
	@Override
	public void visit(SymDeclaring node){
		stack.push(sentinel); 
	}
	
	@Override
	public void visit(Assigning node) {
		stack.push(node); 
		super.visit(node);
	}
	
	@Override
	public void visit(Computing node){
		stack.clear(); 
	}
	
	@Override
	public void visit(Printing node){
		if (!stack.isEmpty()){
			Assigning assg = stack.pop(); 
			if (assg.id == node.id){
				assg.isPrintedNext = true; 
				node.isStacked = true; 
			}
		}
		stack.clear(); 
	}
}
