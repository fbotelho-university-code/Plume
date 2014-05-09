/**
 * @author 41625@alunos.fc.ul.pt Fábio Botelho
 * @version $Id 
 */
package plume.attr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import plume.types.Type;


public class MethodAttributes {

	/**
	 * The class this method belongs to.
	 */
	private final ClassAttributes myClass;

	/**
	 * The symbol table for the parameters and variables local to this method.
	 */
	private final Map<String, ParameterAttributes> parameterMap;

	/**
	 * The types of the parameters in this method.
	 */
	private List<Type> parameterTypes;

	/**
	 * The return type of this method.
	 */
	private Type returnType;

	/**
	 * Is this method abstract?
	 */
	private boolean isAbstract;

	/**
	 * Does this method overrides some method in some super class?
	 */
	private boolean isOverride;

	private List<String> parameterNames;

	private String name;
	


	public MethodAttributes(ClassAttributes myClass, boolean isAbstract,
			boolean isOverride, String name) {
		this.myClass = myClass;
		this.isAbstract = isAbstract;
		this.isOverride = isOverride;
		this.parameterMap = new HashMap<String, ParameterAttributes>();
		this.parameterTypes = new ArrayList<Type>();
		this.nextLocal = 1; // local 0 is 'this'
		this.parameterNames = new ArrayList<String>();
		this.name = name; 
	}
	
	public String getName(){
		return this.name; 
	}
	public void setName(String name){
		this.name = name; 
	}
	
	/**
	 * The attributes of the class to which this method belongs.
	 */
	public ClassAttributes getMyClass() {
		return myClass;
	}

	/**
	 * Is this method abstract?
	 */
	public boolean isAbstract() {
		return isAbstract;
	}

	/**
	 * Does this method overrides some other method?
	 */
	public boolean isOverride() {
		return isOverride;
	}

	
	/**
	 * The list of parameters to this method.
	 */
	public List<Type> getParameterTypes() {
		return parameterTypes;
	}

	/**
	 * Set the return type of this method
	 * @param returnType The return type
	 */
	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}

	/**
	 * The return type of this method.
	 */
	public Type getReturnType() {
		return returnType;
	}

	/**
	 * The attributes to a given parameter.
	 * @param parameterName The name of the parameter
	 */
	public ParameterAttributes getParameter(String parameterName) {
		return parameterMap.get(parameterName);
	}

	
	/**
	 * @param key
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsField(String key) {
		return parameterMap.containsKey(key);
	}

	/**
	 * Put a parameter in the symbol table of this method.
	 * @param parameterName The name of the parameter
	 * @param attributes The attributes of the parameter
	 */
	public void putParameter(String parameterName, ParameterAttributes attributes) {
		parameterMap.put(parameterName, attributes);
		parameterNames.add(parameterName); 
		parameterTypes.add(attributes.getType()); 
	}

	/**
	 * The attributes of a given variable (field or parameter.
	 * Warning: This method loops on a circular extends relation!
	 *
	 * @param variableName The name of the variable
	 */
	public VariableAttributes retrieveVariable(String variableName) {
		VariableAttributes result = parameterMap.get(variableName);
		if (result == null)
			result = myClass.getField(variableName);
		return result;
	}

	/**
	 * Replace a given variable (field or parameter) name with a new type.
	 * Useful for the if-instance of construct. The variable name must be in
	 * defined in the class hierarchy.
	 * 
	 * @param variableName
	 *            The name of the variable
	 * @param type
	 *            The new type for the variable
	 */
	public void replaceVariable(String variableName, Type type) {
		VariableAttributes variableAttribute = parameterMap.get(variableName);
		if (variableAttribute != null)
			variableAttribute.setType(type);
		else
			myClass.replaceVariable(variableName, type);
		
	}
	
	public Collection<String> getParameterNames() {
		return parameterNames; 
	}
	
	
	@Override
	public String toString() {
		return parameterTypes + " -> " + returnType + "{symbolTable: "
				+ parameterMap + "}";
	}

	// Locals _ Used in code generation

	/**
	 * The next available local variable number
	 */
	private int nextLocal;

	/**
	 * The number of locally declared variables in a method.
	 */
	public int getLocals() {
		return nextLocal;
	}

	public int getIndex(String param){
		return this.parameterNames.indexOf(param) + 1; 
	}
	/**
	 * Use this in constructor ParameterAttributes
	 * 
	 * @return The next availabe local (parameter) variable
	 */
	public int nextLocal() {
		return nextLocal++;
	}

	/**
	 * @return
	 */
	public boolean isMain() {
		return name.equals("main") && returnType == Type.STRING_TYPE && this.parameterMap.isEmpty(); 
	}
	
	
}
