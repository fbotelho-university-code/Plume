package acdc.ast;

/**
 * A reference-to-a-symbol node in an abstract syntax tree.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: SymReferencing.java 384 2012-02-13 23:34:27Z vv $
 */
public class SymReferencing implements Node {
	
	public final String id;

	public SymReferencing(String id) {
		this.id = id;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);		
	}
}
