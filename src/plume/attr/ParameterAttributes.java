/**
 * @author 41625@alunos.fc.ul.pt Fábio Botelho
 * @version $Id 
 */
package plume.attr;


import plume.types.Type;

public class ParameterAttributes extends VariableAttributes {

	/**
	 * The number of the register associated to this parameter. Used for code
	 * generation purposes.
	 */
	private int register;

	public ParameterAttributes(Type type, int register) {
		super(type);
		this.register = register;
	}

	public int registerNumber() {
		return register;
	}
	
	@Override
	public String toString() {
		return "parameter: " + type.toString();
	}
}
