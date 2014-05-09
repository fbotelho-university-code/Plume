package acdc.ast;


/**
 * An example of a visitor for the abstract syntax tree.
 * Prints on the stdout a program, one statement per line.
 * Furthermore, places parenthesis around binary operations in expressions.
 * 
 * Usage:
 * 		tree.accept(new PrinterVisitor());
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: PrettyPrinterVisitor.java 394 2012-02-14 13:36:43Z vv $
 */
public class PrettyPrinterVisitor extends Visitor {
	
	public void visit(SymDeclaring node) {
		System.out.println(typeToString(node.type) + " " + node.id);
	}
	
	public void visit(Assigning node) {
		System.out.print(node.id + " = ");
		node.expr.accept(this);
		System.out.println();
	}

	public void visit(Computing node) {
		System.out.print("(");
		node.left.accept(this);
		System.out.print(" " + opToString(node.op) + " ");
		if (node.right != null){
			node.right.accept(this);
		}
		System.out.print(")");
	}

	public void visit(SymReferencing node) {
		System.out.print(node.id);
	}

	public void visit(IntConsting node) {
		System.out.print(node.val);
	}

	public void visit(FloatConsting node) {
		System.out.print(node.val);
	}

	public void visit(Printing node) {
		System.out.println("p " + node.id);
	}
	
	private static String typeToString (Type type) {
		return type == Type.INTEGER ? "i" : "f";
	}

	private static String opToString (Op op) {
		return op == Op.PLUS ? "+" : "-";
	}
}
