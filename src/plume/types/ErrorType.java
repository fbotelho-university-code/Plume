/**
 * @author 41625@alunos.fc.ul.pt
 * @version id 
 */
package plume.types;


public class ErrorType implements Type{

	public final static Type instance = new ErrorType();
	
	private ErrorType(){
		
	}

	/* (non-Javadoc)
	 * @see plume.types.Type#getTypeName()
	 */
	@Override
	public String getTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

}
