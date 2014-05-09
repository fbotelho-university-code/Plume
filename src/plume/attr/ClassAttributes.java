/**
 * @author 41625@alunos.fc.ul.pt Fábio Botelho
 * @version $Id 
 */
package plume.attr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import plume.attr.*;
import plume.node.Node;
import plume.node.Switch;
import plume.types.ClassType;
import plume.types.Type;

public class ClassAttributes {
	
	public final String filePath; 
	public final Node start; 
	
	
	public Set<String> getAbstractMethodsToImplement(){
		Set<String> methods = new HashSet<String>();
		if (this.isAbstract) return methods; 
		ClassAttributes needle = this.superClass;
		ClassAttributes backNeedle = this;
		
		while( needle != null && needle.isAbstract ){
			for (String m : needle.abstracMethods) {
				if (!backNeedle.containsMethod(m) || !backNeedle.methodDeclaredLocally(m) ){
					methods.add(m); 
				}
			}
			backNeedle = needle; 
			needle = needle.superClass; 
		}
		return methods; 
	}
	
	

	/**
	 * The (implicit) constructor to this class. In the case of a constructor,
	 * we cannot use the getMethod, since this looks in the super classes if not
	 * found locally. But primitive classes other than Object do not have
	 * constructors. A getMethod call to method "<init>" of class String, say,
	 * yields the constructor of class Object.
	 */
	public MethodAttributes getConstructor() {
		return methodMap.get("<init>");
	}

	/**
	 * Setup the constructor to this class. This method should be called only
	 * when all fields to all classes have been added to the symbol table, so
	 * that it correctly builds the constructor including all fields in the
	 * super classes.
	 */
	public void addConstructor() {
		MethodAttributes constructorAttributes = new MethodAttributes(this,
				false, false, "<init>");
		constructorAttributes.setReturnType(type);
		addParametersToConstructor(constructorAttributes);
		this.methodMap.put("<init>", constructorAttributes);
	}
	
	/**
	 * Add parameters to a constructor, starting from the top of the hierarchy
	 * (class Object) and moving down towards the current class. In each class
	 * in the hierarchy, adds the parameters by the order found in list
	 * fieldNames (hence the need for this list, in addition to map fieldMap).
	 * 
	 * @param constructor
	 *            The constructor where to add the attributes.
	 */
	private void addParametersToConstructor(MethodAttributes constructor) {
		if (this.type != Type.OBJECT_TYPE) {
			superClass.addParametersToConstructor(constructor);
			for (String fieldName : fieldNames)
				constructor.putParameter(fieldName,
						new ParameterAttributes(fieldMap.get(fieldName)
								.getType(), constructor.nextLocal()));
		}
	}
	
	
	private Set<String> abstracMethods;  
	/**
	 * The superclass of this class
	 */
	private ClassAttributes superClass;
	
	/**
	 * The type of this class.
	 */
	private final ClassType type;
	
	/**
	 * Is this class abstract?
	 */
	private boolean isAbstract;

	/**
	 * The symbol table for the fields in this class.
	 */
	private final Map<String, FieldAttributes> fieldMap;

	/**
	 * The symbol table for the methods in this class.
	 */
	private final Map<String, MethodAttributes> methodMap;
	private Collection<String> fieldNames;
	
	public ClassAttributes(ClassType type, boolean isAbstract, String filePath, Node start) {
		this.filePath = filePath; 
		this.start = start; 
		this.type = type;
		this.isAbstract = isAbstract;
		this.fieldMap = new HashMap<String, FieldAttributes>();
		this.methodMap = new HashMap<String, MethodAttributes>();
		// Add the constructor. No parameters for the time being; these will be
		// added by the addField method.
		this.abstracMethods = new HashSet<String>(); 
		this.fieldNames = new ArrayList<String>(); 
	}
	
	public Collection<ClassAttributes> getHierchy(){
		Collection<ClassAttributes> hier = new LinkedList<ClassAttributes>();
		ClassAttributes needle = this; 
		do {
			hier.add(needle); 
			needle = needle.superClass; 
		}while(needle != null); 
		return hier; 
	}
	
	public static ClassAttributes minimumCommonType(ClassAttributes then , ClassAttributes elsee){
		if (then == elsee ) return then; 
		Collection<ClassAttributes> needle = then.getHierchy(); 
		Collection<ClassAttributes> reference = elsee.getHierchy(); 
		ClassAttributes realNeedle = null;
		
		for ( ClassAttributes c : needle){
			if (reference.contains(c)){
				realNeedle = c; 
				break; 
			}
		}
		return realNeedle; 
	}
	
	
	/**
	 * The type of this class
	 */
	public ClassType getType() {
		return type;
	}
	
	/**
	 * Is this class abstract?
	 */
	public boolean isAbstract() {
		return isAbstract;
	}
	
	/**
	 * Set the supertype to this class, both in the attributes and in the type.
	 */
	public void setSuper(ClassAttributes superClass) {
		this.superClass = superClass;
		if (superClass != null){
			this.type.setSuperType(superClass.getType());
		}
	}
	
	/**
	 * The attributes for super type of this class.
	 */
	public ClassAttributes getSuper() {
		return superClass;
	}
	
	/**
	 * Is a given method defined in this class or in any of its super classes?
	 * Warning: This method loops on a circular extends relation!
	 * @param methodName
	 *            The name of the method we are looking for
	 */
	public boolean containsMethod(String methodName) {
		ClassAttributes currentClass = this;
		
		while (currentClass.type != ClassType.OBJECT_TYPE
				&& !currentClass.methodMap.containsKey(methodName)) {
			currentClass = currentClass.superClass;
		}
		return currentClass.methodMap.containsKey(methodName);
	}
	
	/**
	 * Is a given method declared exactly in this class?
	 */
	public boolean methodDeclaredLocally(String methodName) {
		return methodMap.containsKey(methodName);
	}
		
	/**
	 * Put a method in the methods symbol table.
	 * 
	 * @param methodName
	 *            The name of the method
	 * @param methodAttribute
	 *            The attributes of the method
	 */
	public void putMethod(String methodName, MethodAttributes methodAttribute) {
		methodMap.put(methodName, methodAttribute);
		if (methodAttribute.isAbstract()){
			this.abstracMethods.add(methodName);
		}
	}
		
	/**
	 * The methods attribute of a given method name. Found in this class or in a
	 * super class. null if not found.
	 * Warning: This method loops on a circular extends relation!
	 * 
	 * @param methodName
	 *            The name of the method
	 */
	public MethodAttributes getMethod(String methodName) {
		ClassAttributes currentClass = this;
		while (currentClass.type != ClassType.OBJECT_TYPE
				&& !currentClass.methodMap.containsKey(methodName))
			currentClass = currentClass.superClass;
		return currentClass.methodMap.get(methodName);
	}
		
	/**
	 * Put a field in the fields symbol table. Add the field as a parameter to
	 * the class constructor.
	 * Warning: This method loops on a circular extends relation!
	 * 
	 * @param fieldName
	 *            The name of the field
	 * @param fieldAttribute
	 *            The attributes of the field
	 */
	public void putField(String fieldName, FieldAttributes fieldAttribute) {
		fieldMap.put(fieldName, fieldAttribute);
		fieldNames.add(fieldName);

	}

	/**
	 * The methods attribute of a given field name. Found in this class or in a
	 * super class. null if not found.
	 * Warning: This method loops on a circular extends relation!
	 * 
	 * @param fieldName
	 *            The name of the field
	 */
	public VariableAttributes getField(String fieldName) {
		ClassAttributes currentClass = this;
		while (currentClass.type != ClassType.OBJECT_TYPE
				&& !currentClass.fieldMap.containsKey(fieldName))
			currentClass = currentClass.superClass;
		return currentClass.fieldMap.get(fieldName);
	}
	
	/**
	 * @param key
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsField(String field) {
		return getField(field) != null; 
	}
	
	public boolean isFieldDeclaredLocally(String field){
		return fieldMap.containsKey(field); 
	}
	
	/**
	 * Replace a given variable (field or parameter) name with a new type.
	 * Useful for the if-instance of construct. The variable name must be in
	 * defined in the class hierarchy.
	 * Warning: This method loops on a circular extends relation!
	 * 
	 * @param variableName
	 *            The name of the variable
	 * @param type
	 *            The new type for the variable
	 */
	public void replaceVariable(String variableName, Type type) {
		ClassAttributes currentClass = this;
		while (currentClass.type != ClassType.OBJECT_TYPE
				&& !currentClass.fieldMap.containsKey(variableName))
			currentClass = currentClass.superClass;
		currentClass.fieldMap.get(variableName).setType(type);
	}
	
	@Override
	public String toString() {
		return "ClassType: " + type + "; Fields: " + fieldMap + "; Methods: "
				+ methodMap;
	}



	/**
	 * @return
	 */
	//TODO - change other getHierar
	public Collection<String> getStringHierchy() {
		Collection<String> hier = new LinkedList<String>();
		hier.add(this.getType().className);
		ClassAttributes needle = this; 
		do{ 
			hier.add(needle.getType().className);
			needle = needle.superClass; 
		}
		while( needle != null); 
		return hier; 
	}


	public boolean isPrimitive(){
		return start == null; 
	}

	
	//All hammers look alike. 
	private Set<Switch> visitors = new HashSet<Switch>();
	public boolean beenVisited(Switch visitor) {
		return visitors.contains(visitor); 
	}
	
	public void visit(Switch s){
		visitors.add(s); 
	}
	
}

