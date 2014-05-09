package acdc.ast;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * An example of a visitor for the abstract syntax tree. Computes the depth of a
 * tree.
 * 
 * The depth of an empty tree is 0. The depth of a non empty tree is one plus
 * the max of the depths of the children.
 * 
 * Usage: HowDeepVisitor depth = new HowDeepVisitor(); tree.accept(depth);;
 * System.out.println("the tree is " + depth.getDepth() + " nodes deep");
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: HowDeepVisitor.java 384 2012-02-13 23:34:27Z vv $
 */
public class HowDeepVisitor extends Visitor {
	/**
	 * This stack contains the depths of the subtrees already visited. We use
	 * the Java 1.6 Deque (Double Ended QUEue) for a stack. Use this and not the
	 * "old" Stack class.
	 */
	private Deque<Integer> stack = new ArrayDeque<Integer>();

	/**
	 * @return The depth of the tree
	 */
	public int getDepth() {
		return stack.peek();
	}

	/**
	 * The depth of computing node is one plus the max of the two depths of the
	 * two children. The depths of the children are popped from the stack; the
	 * depth of the tree under this node is then pushed into the stack.
	 */
	public void visit(Computing node) {
		super.visit(node);
		stack.push(1 + Math.max(stack.pop(), stack.pop()));
	}

	/**
	 * The depth of an assignment node is one plus depth of its only child. The
	 * depth of the child is popped from the stack; the depth of the tree under
	 * this node is then pushed into the stack.
	 */
	public void visit(Assigning node) {
		super.visit(node);
		stack.push(stack.pop() + 1);
	}

	/**
	 * This is the node at the root of the tree. We first push 0 onto the stack,
	 * for the empty tree. For each child we push the new max. The depth of the
	 * deepest child is popped from the stack; the depth of the tree under this
	 * node is then pushed into the stack.
	 */
	public void visit(Programming node) {
		stack.push(0);
		for (Node child : node.children) {
			child.accept(this);
			stack.push(Math.max(stack.pop(), stack.pop()));
		}
		stack.push(stack.pop() + 1);
	}

	/**
	 * Leaves have depth one.
	 */
	public void visit(SymDeclaring node) {
		stack.push(1);
	}

	/**
	 * Leaves have depth one.
	 */
	public void visit(SymReferencing node) {
		stack.push(1);
	}

	/**
	 * Leaves have depth one.
	 */
	public void visit(IntConsting node) {
		stack.push(1);
	}

	/**
	 * Leaves have depth one.
	 */
	public void visit(FloatConsting node) {
		stack.push(1);
	}

	/**
	 * Leaves have depth one.
	 */
	public void visit(Printing node) {
		stack.push(1);
	}
}
