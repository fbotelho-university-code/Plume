/**
 * @author 41625@alunos.fc.ul.pt Fábio Botelho
 * @version $Id 
 */
package plume.types;

/**
 * @author balayhashi
 *
 */
public class ErrorExpression implements Type{

	public static ErrorExpression instance = new ErrorExpression(); 
	private ErrorExpression() {}
	/* (non-Javadoc)
	 * @see plume.types.Type#getTypeName()
	 */
	@Override
	public String getTypeName() {
		return "Object"; 
	}

}
