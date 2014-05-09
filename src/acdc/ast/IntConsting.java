package acdc.ast;

/**
 * An integer literal node in an abstract syntax tree.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: IntConsting.java 384 2012-02-13 23:34:27Z vv $
 */
public class IntConsting implements Node {

	public final String val;

	public IntConsting(String val) {
		this.val = val;
	}
	
	public void accept(Visitor visitor) {
		visitor.visit(this);		
	}
}
