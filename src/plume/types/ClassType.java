package plume.types;


public class ClassType implements Type {
	//TODO declaration in only one place please. 
	public static final ClassType OBJECT_TYPE = Type.OBJECT_TYPE; 
	public final String className;
	private ClassType superType;
	
	/**
	 * @param className
	 */
	public ClassType(String className) {
		this.className = className;
	}
	
	
	/**
	 * @param className
	 * @param superType
	 */
	public ClassType(String className, ClassType superType) {
		
		this.className = className;
		this.superType = superType;
	}


	/**
	 * @return the superType
	 */
	public ClassType getSuperType() {
		return superType;
	}
	/**
	 * @param superType the superType to set
	 */
	public void setSuperType(ClassType superType) {
		this.superType = superType;
	}

	/**
	 * For the strange people who do not like accessing fields =) 
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}


	/* (non-Javadoc)
	 * @see plume.types.Type#getTypeName()
	 */
	@Override
	public String getTypeName() {
		return className;  
	}
}
