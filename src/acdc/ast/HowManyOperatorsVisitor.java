package acdc.ast;

/**
 * An example of a visitor for the abstract syntax tree. Computes the number of
 * binary arithmetic operators in a tree.
 * 
 * Usage: HowManyOperatorsVisitor howMany = new HowManyOperatorsVisitor();
 * tree.accept(howMany); System.out.println("found " + howMany.getHowManyOps() +
 * " operations");
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: HowManyOperatorsVisitor.java 384 2012-02-13 23:34:27Z vv $
 */
public class HowManyOperatorsVisitor extends Visitor {

	private int howManyOps;

	public int getHowManyOps() {
		return howManyOps;
	}

	public void visit(Computing node) {
		super.visit(node);
		howManyOps++;
	}
	
}
