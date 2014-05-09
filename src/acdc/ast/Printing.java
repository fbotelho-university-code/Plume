package acdc.ast;

/**
 * A print statement node in an abstract syntax tree.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: Printing.java 384 2012-02-13 23:34:27Z vv $
 */
public class Printing implements Node {
	public final String id;
	public boolean isStacked =false; 
	
	public Printing(String id) {
		this.id = id;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);		
	}
}
