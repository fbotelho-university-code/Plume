/**
 * @author 41625@alunos.fc.ul.pt Fábio Botelho
 * @version $Id 
 */
package plume.types;

/**
 * @author balayhashi
 *
 */
public enum ExpressionType implements Type{
	ADD , DIV, EQ, AND, NOT, OR, NEQ, LOWEREQ, LOWER, BIGGEREQ, BIGGER, MINUS, MOD, MUL, AINVERSE;

	/**
	 * @param expType
	 * @return
	 */
	public static boolean isArithmetic(ExpressionType expType) {
		return ( expType == ADD ||
				expType == DIV || 
				expType == MINUS ||
				expType == MOD ||
				expType == MUL) ;  
	}

	/* (non-Javadoc)
	 * @see plume.types.Type#getTypeName()
	 */
	@Override
	public String getTypeName() {
		return this.name(); 
	}
}
