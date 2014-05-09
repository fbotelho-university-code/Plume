package acdc.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * A program node in an abstract syntax tree.
 * A program is simply a list of nodes (symbol declarations or statements)
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: Programming.java 384 2012-02-13 23:34:27Z vv $
 */
public class Programming implements Node {

	public final List<Node> children;

	public Programming() {
		this.children = new ArrayList<Node>();
	}

	public void adoptChild (Node n) {
		children.add(n);
	}
	
	public void accept(Visitor visitor) {
		visitor.visit(this);		
	}
	
}
