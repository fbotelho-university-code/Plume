package acdc.ast;

/**
 * A floating point literal node in an abstract syntax tree.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: FloatConsting.java 384 2012-02-13 23:34:27Z vv $
 */
public class FloatConsting implements Node {
	
	public final String val;

	public FloatConsting(String val) {
		this.val = val;
	}
	
	public void accept(Visitor visitor) {
		visitor.visit(this);		
	}
	
}
