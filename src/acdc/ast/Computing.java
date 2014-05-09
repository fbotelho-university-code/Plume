package acdc.ast;

/**
 * A binary arithmetic operator node in an abstract syntax tree.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: Computing.java 384 2012-02-13 23:34:27Z vv $
 */
public class Computing implements Node {

	/**
	 * Not final because of the method we use to build the tree.
	 * See the parser.
	 */
	public Node left;

	public final Op op;
	
	public final Node right;
	
	public Computing(Node left, Op op, Node right) {
		this.left = left; this.right = right; this.op = op;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);		
	}
}
