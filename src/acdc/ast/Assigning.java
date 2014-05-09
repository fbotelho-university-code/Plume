package acdc.ast;

/**
 * An assignment node in an abstract syntax tree.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: Assigning.java 384 2012-02-13 23:34:27Z vv $
 */
public class Assigning implements Node {

	public final String id;
	public  Node expr;

	public boolean isPrintedNext=false; 
	public boolean constAssigningOnDeclaration = false;  //Annotation. Optimization may consider this assignment redudant. 

	public Assigning(String id, Node val) {
		this.id = id;
		this.expr = val;
	}

	public void accept (Visitor visitor){
		visitor.visit(this);
	}
}
