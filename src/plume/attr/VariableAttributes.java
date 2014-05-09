/**
 * @author 41625@alunos.fc.ul.pt Fábio Botelho
 * @version $Id 
 */
package plume.attr;

import plume.types.Type;

public abstract class VariableAttributes {
	
	/**
	 * The type of this variable
	 */
	protected Type type;

	public VariableAttributes(Type type) {
		super();
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type.toString();
	}
}
