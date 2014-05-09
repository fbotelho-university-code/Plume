package acdc.ast;

/**
 * A depth-first abstract visitor for the abstract syntax tree.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: Visitor.java 393 2012-02-14 10:46:38Z vv $
 */
public abstract class Visitor {
	
	public void visit(Programming node) {
		for(Node child: node.children)
			child.accept(this);
	}
	
	public void visit(SymDeclaring node) {}
	
	public void visit(Assigning node) {
		node.expr.accept(this);
	}

	public void visit(Computing node) {
		node.left.accept(this);
		if (node.right != null)
			node.right.accept(this);
	}
	
	public void visit(SymReferencing node) {}

	public void visit(IntConsting node) {}

	public void visit(FloatConsting node) {}

	public void visit(Printing node) {}

	
}
