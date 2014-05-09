/**
 * @author 41625@alunos.fc.ul.pt
 */
package plume.visitors.gen;

import plume.types.*;

/**
 * @author balayhashi
 *
 */
public final class Triplet {
	public final String variableName; 
	public final Type currentType; 
	public final Type castType;
	/**
	 * @param variableName
	 * @param currentType
	 * @param castType
	 */
	public Triplet(String variableName, Type currentType, Type castType) {
		super();
		this.variableName = variableName;
		this.currentType = currentType;
		this.castType = castType;
	}
	
}
