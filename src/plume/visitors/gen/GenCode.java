/**
 * This class visits the AST and generates the .class for the class of the tree.
 * There are no type verification. Type checking must be performed before as this visitor assumes that all type ares correct.  
 * @author 41625@alunos.fc.ul.pt
 * @version $id 
 */
package plume.visitors.gen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DUP;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.INEG;
import org.apache.bcel.generic.INSTANCEOF;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;

import plume.attr.ClassAttributes;
import plume.attr.MethodAttributes;
import plume.attr.VariableAttributes;
import plume.compiler.ErrorList;
import plume.node.ACastExpr;
import plume.node.AClass;
import plume.node.AConditionalExpr;
import plume.node.AErrorExpr;
import plume.node.AFalseExpr;
import plume.node.AFieldAccessExpr;
import plume.node.AFielddclMember;
import plume.node.AIdentifierExpr;
import plume.node.AInstanceofExpr;
import plume.node.AIntegerExpr;
import plume.node.AMethodCallExpr;
import plume.node.AMethoddclMember;
import plume.node.ANewObjExpr;
import plume.node.AOpExpr;
import plume.node.AStringExpr;
import plume.node.ASuperExpr;
import plume.node.AThisExpr;
import plume.node.ATrueExpr;
import plume.node.Node;
import plume.node.PExpr;
import plume.types.ExpressionType;
import plume.types.Type;
import plume.visitors.PlumeVisitor;


public class GenCode extends PlumeVisitor {
	
	private ClassAttributes currentClass; 
	private MethodAttributes currentMethod;
	
	
	//Bcel attributes for generation of the code. 
	private ClassGen cg;  //Current class generator
	private InstructionFactory fi; //Instruction factory based on current class
	private InstructionList il;  //Instruction list 
	private MethodGen mg;  // Current Method generation 
	private ConstantPoolGen cp;  //Current class constant pool 
	
	//Map for currently casted variables while visiting the tree. 
	//This is used for instanceof inside if then else expressions. The map contains the variables that should be cast before used in the then body of if expressions. 
	private Map<String, Triplet> castedVariables = new HashMap<String, Triplet>();
	
	/**
	 * @param topLevelSymbolTable
	 * @param fileName
	 * @param nodeTypes
	 */
	public GenCode(Map<String, ClassAttributes> topLevelSymbolTable,
			String fileName, Map<Node, Type> nodeTypes) {
		super(topLevelSymbolTable, fileName, nodeTypes);
	}

	/*
	 * initialize the bcel classGen cg and constantpool cp. Set currentClass. 
	 */
	@Override
	public void inAClass(AClass node) {
		String className = node.getType().getText();
		currentClass = topLevelSymbolTable.get(className);
		if (currentClass.isAbstract()){
			cg = new ClassGen(className, currentClass.getSuper().getType().className, this.fileName, Constants.ACC_PUBLIC | Constants.ACC_SUPER | Constants.ACC_ABSTRACT, null);
		}else{
			cg = new ClassGen(className, currentClass.getSuper().getType().className, this.fileName, Constants.ACC_PUBLIC | Constants.ACC_SUPER, null);
		}
		cp = cg.getConstantPool(); 
	}
	
	/*
	 * Create  the .class file and constructor of the class.  
	 */
	@Override
	public void outAClass(AClass node) {
		createConstructor();
		String directory = this.fileName.substring(0,
				this.fileName.lastIndexOf(File.separatorChar));
		try{
			cg.getJavaClass().dump( directory +"/" + node.getType().getText() + ".class");
		}catch (IOException e ){
			ErrorList.add(node,"Could not save file : " + e.getMessage() , fileName); 
		}
		currentClass = null; 
	}
	
	
	/* 
	 * Cretate field in current class. Fields are final.
	 */
	@Override
	public void inAFielddclMember(AFielddclMember node) {
		cg.addField(new FieldGen(Constants.ACC_PUBLIC | Constants.ACC_FINAL, getBecelType(nodeTypes.get(node.getType())), node.getId().getText(), cp).getField()); 
	}
	
	/* 
	 * Accessing a field. Creates field Access based on class target.  
	 */
	@Override
	public void outAFieldAccessExpr(AFieldAccessExpr node) {
		Type target = nodeTypes.get(node.getTarget());
		ClassAttributes c = topLevelSymbolTable.get(target.getTypeName());
		VariableAttributes f = c.getField(node.getId().getText()); 
		il.append(fi.createGetField(getName(target), node.getId().getText(), getBecelType(f.getType())));
		unboxIt(f.getType()); 
	}
	
	/*
	 * Entering methods. Create MethodGen before visiting method body.   
	 */
	@Override
	public void inAMethoddclMember(AMethoddclMember node) {
		String methodName = node.getId().getText();
		currentMethod = currentClass.getMethod(methodName);
		if (!currentMethod.isAbstract()){
			createMethod(methodName);
		}
		else{
			createAbstractMethod(methodName); 
		}
	}

	/*
	 * After visiting Method body we must wrap up and add the method to the class. 
	 * It is also necessary to append the return instruction before and clean up bcel.  
	 */
	@Override
	public void outAMethoddclMember(AMethoddclMember node){
		if (!currentMethod.isAbstract()){
			//boxing in actual return type. 
			boxIt(nodeTypes.get(node.getBody()));
			il.append(InstructionFactory.createReturn(getBecelType(currentMethod.getReturnType()))); 
			mg.setMaxStack(); 
			cg.addMethod(mg.getMethod());
			il.dispose();
			if (currentMethod.isMain()){
				createStaticMain(); 
			}
		}
		currentMethod = null;
	}
	
	/* 
	 * Cast expression. Generates the checkCast operation. 
	 */
	@Override
	public void outACastExpr(ACastExpr node) {
		Type t = nodeTypes.get(node.getExpr());
		boxIt(t); 
		Type result = nodeTypes.get(node.getType()); 
		il.append(fi.createCast(getBecelType(t), getBecelType(result)));
		unboxIt(result); 	
	}
	
	
	public void caseAConditionalExpr(AConditionalExpr node){
		boolean isInstanceOf = node.getCond() instanceof AInstanceofExpr &&  ((AInstanceofExpr ) node.getCond()).getExpr() instanceof AIdentifierExpr ; 
		node.getCond().apply(this);	
		String variableName = null; 
		if (isInstanceOf){
			variableName = updateCastedVariables(node); 
		}
		BranchInstruction if_icmplt_2 = InstructionFactory.createBranchInstruction(Constants.IFEQ, null);
		il.append(if_icmplt_2);
		node.getThen().apply(this);
		if (isInstanceOf){
			removeFromCastedVariables(variableName); 
		}
		BranchHandle append = il.append(InstructionFactory.createBranchInstruction(Constants.GOTO, null));
		InstructionHandle el = il.append(new NOP()); 
		node.getElse().apply(this); 
		if_icmplt_2.setTarget(el);
		InstructionHandle h = il.append(new NOP());
		append.setTarget(h);
	}

	

	
	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#outAInstanceofExpr(plume.node.AInstanceofExpr)
	 */
	@Override
	public void outAInstanceofExpr(AInstanceofExpr node) {
		Type castedType = nodeTypes.get(node.getType()); 
		boxIt(nodeTypes.get(node.getExpr()));
		il.append(new INSTANCEOF(cp.addClass(new ObjectType(getName(castedType)))));
	}

	/*
	 * Visit AND(&&), OR(||), and String concat (+). The case is necessary since in those operations we must not visit the operands first. 
	 */
	@Override 
	public void caseAOpExpr(AOpExpr node){
		ExpressionType expType = (ExpressionType) nodeTypes.get(node.getOp()); 
		switch (expType){
			case AND :
			createShortCircuitAnd(node);
				break;
			case OR :
			createShortCircuiteOR(node);
				break;
			case ADD :
				Type left = nodeTypes.get(node.getLeft()); 
				if (left == Type.STRING_TYPE){
					this.createStringConcat(node);
					break; 
				}
			default : 
				super.caseAOpExpr(node); 
		}
	}

	

	/*
	 * Visit all other expressions. 
	 */
	@Override
	public void outAOpExpr(AOpExpr node) {
		ExpressionType expType = (ExpressionType) nodeTypes.get(node.getOp()); 
		Type left = nodeTypes.get(node.getLeft()); 
		switch (expType){
			case DIV :
				createBinArithmetic("/"); 
				break; 
			case MINUS: 
				createBinArithmetic("-"); 
				break; 
			case MOD: 
				createBinArithmetic("%"); 
				break;
			case MUL:
				createBinArithmetic("*"); 
				break; 
			case ADD : 
				if ( left != Type.STRING_TYPE ){
					createBinArithmetic("+"); 
				}
				break;
			case AINVERSE : 
				il.append(new INEG()); 
				break;
				//TODO comparison 
			case LOWEREQ:
				createCmpOp(Constants.IF_ICMPGT);
				break; 
			case LOWER :
				createCmpOp(Constants.IF_ICMPGE); 
				break; 
			case BIGGEREQ:
				createCmpOp(Constants.IF_ICMPLT);
				break; 
			case BIGGER : 
				createCmpOp(Constants.IF_ICMPLE); 
				break; 
			case EQ :
				if (left == Type.BOOLEAN_TYPE || left == Type.INTEGER_TYPE)
					createCmpOp(Constants.IF_ICMPNE);
				else
					createCmpOp(Constants.IF_ACMPNE); 
				break;
			case NEQ :
				if (left == Type.BOOLEAN_TYPE || left == Type.INTEGER_TYPE)
					createCmpOp(Constants.IF_ICMPEQ);
				else
					createCmpOp(Constants.IF_ACMPEQ); 
				break; 
			case NOT : 
				createCmpOp(Constants.IFNE); 
				break;
		}
	}
	
	/*
	 * A method call. We must create a invokevirtual on the target . In the case that the target
	 * is a Super expression then we must create a invokespecial
	 */
	@Override
	public void outAMethodCallExpr(AMethodCallExpr node){
		String methodName = node.getId().getText();
		Type targetType = nodeTypes.get(node.getTarget());
		boxIt(targetType); 
		ClassAttributes targetClass = topLevelSymbolTable.get(targetType.getTypeName());
		MethodAttributes methodAttributes = targetClass.getMethod(methodName);
		if (!(node.getTarget() instanceof ASuperExpr)){
			il.append(fi.createInvoke(targetType.getTypeName(), methodName, getBecelType(methodAttributes.getReturnType()), getBecelType(methodAttributes.getParameterTypes()) , Constants.INVOKEVIRTUAL));
		}
		else {
			il.append(fi.createInvoke(targetType.getTypeName(), methodName, getBecelType(methodAttributes.getReturnType()), getBecelType(methodAttributes.getParameterTypes()), Constants.INVOKESPECIAL)); 
		}
		unboxIt(methodAttributes.getReturnType()); 
	}
	
	/*
	 * Create a new expression. 
	 */
	@Override
	public void inANewObjExpr(ANewObjExpr node){
		Type resultType = nodeTypes.get(node.getType());
		il.append(fi.createNew(new ObjectType(resultType.getTypeName())));
		il.append(new DUP()); 
	}
	
	//caseNew is override to box arguments 
	@Override 
	public void caseANewObjExpr(ANewObjExpr node){
		inANewObjExpr(node);
        if(node.getKnew() != null)
        {
            node.getKnew().apply(this);
        }
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        {
            List<PExpr> copy = new ArrayList<PExpr>(node.getExpr());
            for(PExpr e : copy)
            {
                e.apply(this);
                boxIt(nodeTypes.get(e));
                
            }
        }
        outANewObjExpr(node);
	}

	@Override
	public void outANewObjExpr(ANewObjExpr node) {
		Type resultType = nodeTypes.get(node.getType());
		ClassAttributes targetClass = topLevelSymbolTable.get(resultType.getTypeName());
		MethodAttributes methodAttributes = targetClass.getConstructor();
		il.append(fi.createInvoke(getName(resultType), "<init>", org.apache.bcel.generic.Type.VOID, getBecelType(methodAttributes.getParameterTypes()) , Constants.INVOKESPECIAL));
	}
	
	//Simple case push into stack the value. 
	@Override
	public void outAStringExpr(AStringExpr node) {
		String s = node.getString().getText(); 
		s = s.substring(1, s.length()-1);
		il.append(new PUSH(cp, s)); 
	}
	
	
	//method call is overriden to box arguments 
	@Override 
	public void caseAMethodCallExpr(AMethodCallExpr node){
		 inAMethodCallExpr(node);
	        if(node.getTarget() != null)
	        {
	            node.getTarget().apply(this);
	        }
	        if(node.getId() != null)
	        {
	            node.getId().apply(this);
	        }
	        {
	            List<PExpr> copy = new ArrayList<PExpr>(node.getExpr());
	            for(PExpr e : copy)
	            {
	                e.apply(this);
	                
	                boxIt(nodeTypes.get(e));
	            }
	        }
	        outAMethodCallExpr(node);
		
	}


	//Simple case push into stack the value. 
	@Override
	public void outAIntegerExpr(AIntegerExpr node) {
		il.append(new PUSH(cp, Integer.valueOf(node.getNumber().getText()))); 

	}

	//Simple case push into stack the value. 
	@Override
	public void outATrueExpr(ATrueExpr node) {
		il.append(new PUSH(cp, true)); 
	}

	//Simple case push into stack the value. 
	@Override
	public void outAFalseExpr(AFalseExpr node) {
		il.append(new PUSH(cp, false));
	}
	
	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#outAErrorExpr(plume.node.AErrorExpr)
	 */
	@Override
	public void outAErrorExpr(AErrorExpr node) {
	    il.append(fi.createNew("java.lang.Exception"));
	    il.append(InstructionConstants.DUP);
	    il.append(fi.createInvoke("java.lang.Exception", "<init>", org.apache.bcel.generic.Type.VOID, org.apache.bcel.generic.Type.NO_ARGS, Constants.INVOKESPECIAL));
	    il.append(InstructionConstants.ATHROW);	  
	    mg.addException("java.lang.Exception"); 
	}
	
	
	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#outAIdentifierExpr(plume.node.AIdentifierExpr)
	 */
	@Override
	public void outAIdentifierExpr(AIdentifierExpr node) {
		String id = node.getId().getText();
		Type result = null; 
		if (currentMethod.containsField(id)){
			//Is local variable
			il.append(InstructionFactory.createLoad(getBecelType(currentMethod.getParameter(id).getType()), currentMethod.getIndex(id)));
			result = currentMethod.getParameter(id).getType(); 
		}
		else{
			//Is field of current class
			il.append(InstructionFactory.createThis()); 
			il.append(fi.createFieldAccess(currentClass.getType().getTypeName(), id, getBecelType(currentClass.getField(id).getType()), org.apache.bcel.Constants.GETFIELD));
			result = currentClass.getField(id).getType(); 
		}
		if (!this.castedVariables.isEmpty()  && this.castedVariables.containsKey(id)){
			il.append(fi.createCast(getBecelType(this.castedVariables.get(id).currentType), getBecelType(this.castedVariables.get(id).castType)));
			unboxIt(this.castedVariables.get(id).castType); 
		}else{
			unboxIt(result); 
		}
	}
	
	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#outAThisExpr(plume.node.AThisExpr)
	 */
	@Override
	public void outAThisExpr(AThisExpr node) {
		il.append((InstructionFactory.createThis()));
	}

	
	public void outASuperExpr(ASuperExpr node){
		il.append(InstructionFactory.createThis()); 
	}
	
	private org.apache.bcel.generic.Type[] getBecelType(Collection<Type> argumentTypes) {
		org.apache.bcel.generic.Type[] result = new org.apache.bcel.generic.Type[argumentTypes.size()];
		int i =0; 
		for (Type t : argumentTypes){
			result[i++] = getBecelType(t);
		}
		return result; 
	}
	
	/**
	 * @param returnType
	 * @return
	 */
	private org.apache.bcel.generic.Type getBecelType(Type returnType) {
		//TODO clean up
		if (returnType.getTypeName().equals("Object"))
			return new org.apache.bcel.generic.ObjectType(returnType.getTypeName()); 
		else if (returnType.getTypeName().equals("String")) 
			return new org.apache.bcel.generic.ObjectType("String"); 
		else if (returnType.getTypeName().equals("Integer"))
			return new org.apache.bcel.generic.ObjectType("Integer");
		else if (returnType.getTypeName().equals("Boolean"))
			return new org.apache.bcel.generic.ObjectType("Boolean");
		else return new org.apache.bcel.generic.ObjectType(returnType.getTypeName());
	}
	
	/**
	 * Beware that this works the other way around ! - v2 op v1 
	 * @param op
	 */
	private void createCmpOp(short op) {
		BranchInstruction if_icmplt_2 = InstructionFactory.createBranchInstruction(op, null);
		il.append(if_icmplt_2);
		il.append(new PUSH(cp, 1));
		BranchHandle append = il.append(InstructionFactory.createBranchInstruction(Constants.GOTO, null));
		InstructionHandle ih_7 = il.append(new PUSH(cp, 0));
		if_icmplt_2.setTarget(ih_7);
		InstructionHandle h = il.append(new NOP());
		append.setTarget(h); 
	}
	
	private void createBinArithmetic(String c) {
		il.append(InstructionFactory.createBinaryOperation(c, org.apache.bcel.generic.Type.INT));
	}
	
	private void createStringConcat( AOpExpr node){
		//Delete the load of strings.
		il.append(fi.createNew("java.lang.StringBuilder"));
    	il.append(InstructionConstants.DUP);
    	node.getLeft().apply(this);  //Load the left string.
    	il.append(fi.createInvoke("java.lang.StringBuilder", "<init>", org.apache.bcel.generic.Type.VOID, new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.STRING }, Constants.INVOKESPECIAL));
    	node.getRight().apply(this); 
    	il.append(fi.createInvoke("java.lang.StringBuilder", "append", new org.apache.bcel.generic.ObjectType("java.lang.StringBuilder"), new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.STRING }, Constants.INVOKEVIRTUAL));
    	il.append(fi.createInvoke("java.lang.StringBuilder", "toString", org.apache.bcel.generic.Type.STRING, org.apache.bcel.generic.Type.NO_ARGS, Constants.INVOKEVIRTUAL));
	}
	
	private void createMethod(String methodName) {
		fi = new InstructionFactory(cg); 
		il = new InstructionList();
		org.apache.bcel.generic.Type returnType =  !methodName.equals("<init>") ? getBecelType(currentMethod.getReturnType()) : org.apache.bcel.generic.Type.VOID;
		org.apache.bcel.generic.Type[] paramTypes =  getBecelType(currentMethod.getParameterTypes());
		Collection<String> names = currentMethod.getParameterNames(); 
		String[] paramNames = toArray(names);
		mg = new MethodGen(Constants.ACC_PUBLIC, returnType, paramTypes, paramNames,  methodName , currentClass.getType().getTypeName(), il, cp );
	}
	
	private String[] toArray(Collection<String> names) {
		String[] paramNames = new String[names.size()];
		int i=0; 
		for (String n : names){ 
			paramNames[i++] = n; 
		}
		return paramNames;
	}
	
	private void createConstructor() {
		currentMethod = currentClass.getConstructor();
		createMethod("<init>");
		il.append(InstructionFactory.createThis()); 
		Collection<Type> supTypes = new ArrayList<Type>(); 
		Iterator<String> it = currentMethod.getParameterNames().iterator(); 
		int i =1 ;
		while(it.hasNext()){
			String s = it.next(); 
			if (!currentClass.isFieldDeclaredLocally(s)){
				Type c = currentMethod.getParameter(s).getType(); 
				supTypes.add(c); 
				//Must push to call super constructor. TODO : possibly we do not need to push since they are already in TOS.... 
				il.append(InstructionFactory.createLoad(getBecelType(c), i++)); 
			}
			else {
				break; 
			}
		}
		
		//Call super constructor :
		il.append(fi.createInvoke(getName(currentClass.getSuper().getType()), "<init>", org.apache.bcel.generic.Type.VOID, getBecelType(supTypes), Constants.INVOKESPECIAL));

		it = currentMethod.getParameterNames().iterator(); 
		while (it.hasNext()){
			String s = it.next();
			if (currentClass.isFieldDeclaredLocally(s)){
				il.append(InstructionFactory.createThis());
				Type c = currentMethod.getParameter(s).getType(); 
				il.append(InstructionFactory.createLoad(getBecelType(c), i++));
				il.append(fi.createPutField(getName(currentClass.getType()), s, getBecelType(c)));
			}
		}
		il.append(InstructionFactory.createReturn(org.apache.bcel.generic.Type.VOID));
		mg.setMaxStack(); 
		cg.addMethod(mg.getMethod());
		il.dispose(); 
		//initialize  fields of this object:
		currentMethod = null;
	}
	
	private void  createStaticMain(){
		mg = new MethodGen(Constants.ACC_STATIC | Constants.ACC_PUBLIC, org.apache.bcel.generic.Type.VOID,  new org.apache.bcel.generic.Type[] { new ArrayType(org.apache.bcel.generic.Type.STRING, 1) },  new String[] { "argv" }, "main", currentClass.getType().getTypeName(),  il, cp);
		il.append(fi.createFieldAccess("java.lang.System", "out",
				new ObjectType("java.io.PrintStream"), Constants.GETSTATIC));
		String className = currentClass.getType().getClassName();
		il.append(fi.createNew(new ObjectType(className)));
		il.append(new DUP());
		il.append(fi.createInvoke(className, "<init>", org.apache.bcel.generic.Type.VOID, org.apache.bcel.generic.Type.NO_ARGS, Constants.INVOKESPECIAL));
		InstructionHandle start = il.append(fi.createInvoke(className, "main", new org.apache.bcel.generic.ObjectType("String"),org.apache.bcel.generic.Type.NO_ARGS, Constants.INVOKESPECIAL)); 
		unboxIt(Type.STRING_TYPE); 
		InstructionHandle end = il.append(fi.createInvoke("java.io.PrintStream", "println",
				org.apache.bcel.generic.Type.VOID, new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.STRING }, Constants.INVOKEVIRTUAL));
		BranchHandle gotoEnd = il.append(InstructionFactory.createBranchInstruction(Constants.GOTO, null));
		InstructionHandle solve = il.append(InstructionFactory.createPop(1)); 
		il.append(fi.createFieldAccess("java.lang.System", "out", new ObjectType("java.io.PrintStream"), Constants.GETSTATIC));
		il.append(new PUSH(cp, "<error>"));
		il.append(fi.createInvoke("java.io.PrintStream", "println", org.apache.bcel.generic.Type.VOID, new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.STRING }, Constants.INVOKEVIRTUAL));
		mg.addExceptionHandler(start, end, solve, new ObjectType("java.lang.Exception"));
		InstructionHandle returnIns  =il.append(InstructionFactory.createReturn(org.apache.bcel.generic.Type.VOID));
		gotoEnd.setTarget(returnIns);
		mg.setMaxStack(); 
		cg.addMethod(mg.getMethod()); 
		il.dispose(); 
	}
	
	private void createAbstractMethod(String methodName) {
		org.apache.bcel.generic.Type returnType =   getBecelType(currentMethod.getReturnType()); 
		org.apache.bcel.generic.Type[] paramTypes =  getBecelType(currentMethod.getParameterTypes());
		Collection<String> names = currentMethod.getParameterNames(); 
		String[] paramNames = toArray(names);
		il = new InstructionList(); 
		mg = new MethodGen(Constants.ACC_PUBLIC | Constants.ACC_ABSTRACT, returnType, paramTypes, paramNames,  methodName , currentClass.getType().getTypeName(), il, cp );
		mg.addException("java.lang.Exception");
		mg.setMaxStack();
		mg.setMaxLocals();
		cg.addMethod(mg.getMethod());
		il.dispose();
	}
	
	private void removeFromCastedVariables(String variableName) {
		castedVariables.remove(variableName);
	}

	private String updateCastedVariables(AConditionalExpr node) {
		String variableName;
		AInstanceofExpr expression = (AInstanceofExpr) node.getCond();
		variableName = ((AIdentifierExpr) expression.getExpr()).getId().getText();
		Type currentType = currentMethod.retrieveVariable(variableName).getType();
		Type castType = nodeTypes.get(expression.getType());
		this.castedVariables.put(variableName, new Triplet(variableName, currentType, castType));
		return variableName;
	}
	/**
	 * @param type
	 * @return
	 */
	private String getName(Type t){
		return t.getTypeName(); 
	}
	
	private void createShortCircuiteOR(AOpExpr node) {
		createCircuitedConnective(node, Constants.IFNE);
	}

	private void createShortCircuitAnd(AOpExpr node) {
		createCircuitedConnective(node, Constants.IFEQ);
	}

	private void createCircuitedConnective(AOpExpr node, short op) {
		node.getLeft().apply(this); 
		BranchInstruction if_icmp = InstructionFactory.createBranchInstruction(op, null); 
		il.append(if_icmp);
		node.getRight().apply(this);
		BranchHandle end = il.append(InstructionFactory.createBranchInstruction(Constants.GOTO, null));
		//must push 0 
		InstructionHandle els = il.append(new PUSH(cp,  op == Constants.IFNE ? 1 : 0));
		if_icmp.setTarget(els); 
		InstructionHandle nop = il.append(new NOP());
		end.setTarget(nop);
	}
	
	private void boxIt(Type t) {
		if (t == Type.INTEGER_TYPE){
			il.append(fi.createInvoke("Integer", "valueOf", new org.apache.bcel.generic.ObjectType ("Integer"), new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.INT }, Constants.INVOKESTATIC));
		}else if (t == Type.BOOLEAN_TYPE){
			il.append(fi.createInvoke("Boolean", "valueOf", new org.apache.bcel.generic.ObjectType ("Boolean"), new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.BOOLEAN }, Constants.INVOKESTATIC));
		}else if (t == Type.STRING_TYPE){
			il.append(fi.createInvoke("String", "valueOf", new org.apache.bcel.generic.ObjectType ("String"), new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.STRING }, Constants.INVOKESTATIC));
		}
	}
	private void unboxIt(Type t){
		if (t == Type.INTEGER_TYPE){
			il.append(fi.createInvoke("Integer", "intValue", org.apache.bcel.generic.Type.INT, org.apache.bcel.generic.Type.NO_ARGS, Constants.INVOKEVIRTUAL));
		}
		else if (t == Type.BOOLEAN_TYPE){
			il.append(fi.createInvoke("Boolean", "intValue", org.apache.bcel.generic.Type.BOOLEAN, org.apache.bcel.generic.Type.NO_ARGS, Constants.INVOKEVIRTUAL));
		}else if (t == Type.STRING_TYPE){
			il.append(fi.createInvoke("String", "strValue", org.apache.bcel.generic.Type.STRING, org.apache.bcel.generic.Type.NO_ARGS, Constants.INVOKEVIRTUAL));
		}
	}
	
}
