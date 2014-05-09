package plume.types;

public interface Type {

	// Set final types Object, Boolean, Integer. 
	public static final ClassType OBJECT_TYPE = new ClassType("Object", null);
	//TODO - extending OBJECT to ease the job of declaring normal methods like toString(). 
	public static final ClassType BOOLEAN_TYPE = new ClassType("Boolean", OBJECT_TYPE); 
	public static final ClassType INTEGER_TYPE = new ClassType("Integer", OBJECT_TYPE);
	public static final ClassType STRING_TYPE = new ClassType("String", OBJECT_TYPE);
	
	
	public static final ClassType[] BUILTIN_TYPES = {
		OBJECT_TYPE, BOOLEAN_TYPE, INTEGER_TYPE, STRING_TYPE,
	};

	/**
	 * @return
	 */
	String getTypeName();
	
	
}
