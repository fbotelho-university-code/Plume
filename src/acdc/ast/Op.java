package acdc.ast;

/**
 * A binary operator in an expression in an abstract syntax tree.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: Op.java 394 2012-02-14 13:36:43Z vv $
 */
public enum Op {
	PLUS, MINUS, SQRT;
	private String[] dcRepresentation = { "+", "-", "v" }; 
	public String toString() {return dcRepresentation[this.ordinal()]; } 
}
