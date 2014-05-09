/**
 * @author 41625@alunos.fc.ul.pt Fábio Botelho
 * @version $Id 
 */
package plume.attr;

import plume.attr.VariableAttributes;
import plume.types.Type;


public class FieldAttributes extends VariableAttributes {

	public FieldAttributes(Type type) {
		super(type);
	}

	@Override
	public String toString() {
		return "field: " + type.toString();
	}
}
