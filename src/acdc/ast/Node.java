package acdc.ast;

/**
 * An arbitrary node in an abstract syntax tree,
 * equipped with a visitor pattern.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: Node.java 384 2012-02-13 23:34:27Z vv $
 */
public interface Node {
	public void accept(Visitor visitor);
}
