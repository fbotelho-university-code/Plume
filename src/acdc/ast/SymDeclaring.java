package acdc.ast;

/**
 * A symbol declaration node in an abstract syntax tree.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: SymDeclaring.java 384 2012-02-13 23:34:27Z vv $
 */
public class SymDeclaring implements Node {
	
	public final Type type;
	public final String id;
	public String initialValue; //public. nazi encapsulation . fmmCompliat() TODO   
	
	public SymDeclaring(String name, Type type) {
		this.id = name; this.type = type;
		if (type == Type.FLOAT){
			initialValue = "0.0"; 
		}
		else {
			initialValue = "0"; 
		}
	}
	
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
