/**
 * @author 41625@alunos.fc.ul.pt
 */
package plume.visitors.gen;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DUP;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

/**
 * @author balayhashi
 *
 */

public class GenPrimitives {
	public static void genPrimitives(String path){
		createObject(path);
		createString(path); 
		createInteger(path); 
		createBoolean(path); 
	}
	
	private static void createBoolean(String path){
		ClassGen cg = new ClassGen("Boolean", "Object",
				"<generated>", Constants.ACC_PUBLIC | Constants.ACC_SUPER, null);
		ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool
		InstructionList il = new InstructionList();
		// We then create the main method, supplying the method's name and the
		// symbolic type signature encoded with Type objects.
		cg.addEmptyConstructor(Constants.ACC_PUBLIC);
		//createToStringOfInteger(cg, cp, il, "Boolean", Type.BOOLEAN);
		
		cg.addField(new FieldGen(Constants.ACC_PUBLIC , org.apache.bcel.generic.Type.BOOLEAN, "v", cp).getField());
		
		MethodGen mg = new MethodGen(Constants.ACC_PUBLIC | Constants.ACC_STATIC, 
				new org.apache.bcel.generic.ObjectType("Boolean"),
				new org.apache.bcel.generic.Type[] {org.apache.bcel.generic.Type.BOOLEAN},
				new String[] {"vv"}, "valueOf", "Boolean",
				il, cp);
		
		InstructionFactory fi = new InstructionFactory(cg);
		il.append(fi.createNew("Boolean"));
		il.append(new DUP()); 
		il.append(new DUP()); 
		il.append(fi.createInvoke("Boolean", "<init>", org.apache.bcel.generic.Type.VOID, org.apache.bcel.generic.Type.NO_ARGS, Constants.INVOKESPECIAL));
		il.append(InstructionFactory.createLoad(org.apache.bcel.generic.Type.BOOLEAN, 0)); 
		il.append(fi.createFieldAccess("Boolean", "v", org.apache.bcel.generic.Type.BOOLEAN, Constants.PUTFIELD));
		il.append(InstructionFactory.createReturn(new org.apache.bcel.generic.ObjectType("Boolean")));
		mg.setMaxStack();
		cg.addMethod(mg.getMethod());
		il.dispose(); // Allow instruction handles to be reused

		//Create  intValue
		
		mg = new MethodGen(Constants.ACC_PUBLIC , 
				org.apache.bcel.generic.Type.BOOLEAN, 
				org.apache.bcel.generic.Type.NO_ARGS, 
				new String[0], "intValue", "Boolean",
				il, cp);
		
		fi = new InstructionFactory(cg);
		il.append(InstructionFactory.createThis()); 
		il.append(fi.createFieldAccess("Boolean", "v", org.apache.bcel.generic.Type.BOOLEAN, Constants.GETFIELD));
		il.append(InstructionFactory.createReturn(org.apache.bcel.generic.Type.BOOLEAN)); 
		mg.setMaxStack();
		cg.addMethod(mg.getMethod());
		il.dispose(); // Allow instruction handles to be reused

		//CREATE TO STRING
		mg = new MethodGen(Constants.ACC_PUBLIC , 
				new org.apache.bcel.generic.ObjectType("String"),  
				org.apache.bcel.generic.Type.NO_ARGS, 
				new String[0], "toString", "String",
				il, cp);

		fi = new InstructionFactory(cg);
		il.append(InstructionFactory.createThis());
		il.append(fi.createFieldAccess("Boolean", "v", org.apache.bcel.generic.Type.BOOLEAN, Constants.GETFIELD));
		il.append(fi.createInvoke("java/lang/String", "valueOf", org.apache.bcel.generic.Type.STRING, new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.BOOLEAN}, Constants.INVOKESTATIC));
		il.append(fi.createInvoke("String", "valueOf", new org.apache.bcel.generic.ObjectType ("String"), new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.STRING }, Constants.INVOKESTATIC));
		il.append(InstructionFactory.createReturn(Type.OBJECT));
		mg.setMaxStack();
		cg.addMethod(mg.getMethod());
		il.dispose(); // Allow instruction handles to be reused
		//createToStringOfString(cg, cp, il);

		// Last but not least we dump the JavaClass object to a file.
		try {
			cg.getJavaClass().dump(path + "/" + "Boolean.class");
		} catch (java.io.IOException e) {
			System.err.println(e);
		}
}
	private static void createInteger(String path){
		ClassGen cg = new ClassGen("Integer", "Object",
				"<generated>", Constants.ACC_PUBLIC | Constants.ACC_SUPER, null);
		ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool
		InstructionList il = new InstructionList();
		
		// We then create the main method, supplying the method's name and the
		// symbolic type signature encoded with Type objects.
		cg.addEmptyConstructor(Constants.ACC_PUBLIC);
		//createToStringOfInteger(cg, cp, il, "Integer", Type.INT);
		cg.addField(new FieldGen(Constants.ACC_PUBLIC , org.apache.bcel.generic.Type.INT, "v", cp).getField());
		
		MethodGen mg = new MethodGen(Constants.ACC_PUBLIC | Constants.ACC_STATIC, 
				new org.apache.bcel.generic.ObjectType("Integer"),
				new org.apache.bcel.generic.Type[] {org.apache.bcel.generic.Type.INT},
				new String[] {"vv"}, "valueOf", "Integer",
				il, cp);
		
		InstructionFactory fi = new InstructionFactory(cg);
		il.append(fi.createNew("Integer"));
		il.append(new DUP()); 
		il.append(new DUP()); 
		il.append(fi.createInvoke("Integer", "<init>", org.apache.bcel.generic.Type.VOID, org.apache.bcel.generic.Type.NO_ARGS, Constants.INVOKESPECIAL));
		il.append(InstructionFactory.createLoad(org.apache.bcel.generic.Type.INT, 0)); 
		il.append(fi.createFieldAccess("Integer", "v", org.apache.bcel.generic.Type.INT, Constants.PUTFIELD));
		il.append(InstructionFactory.createReturn(new org.apache.bcel.generic.ObjectType("Integer")));
		mg.setMaxStack();
		cg.addMethod(mg.getMethod());
		il.dispose(); // Allow instruction handles to be reused

		//Create  intValue
		
		mg = new MethodGen(Constants.ACC_PUBLIC , 
				org.apache.bcel.generic.Type.INT, 
				org.apache.bcel.generic.Type.NO_ARGS, 
				new String[0], "intValue", "Integer",
				il, cp);
		
		fi = new InstructionFactory(cg);
		il.append(InstructionFactory.createThis()); 
		il.append(fi.createFieldAccess("Integer", "v", org.apache.bcel.generic.Type.INT, Constants.GETFIELD));
		il.append(InstructionFactory.createReturn(org.apache.bcel.generic.Type.INT)); 
		mg.setMaxStack();
		cg.addMethod(mg.getMethod());
		il.dispose(); // Allow instruction handles to be reused

		//CREATE TO STRING
		mg = new MethodGen(Constants.ACC_PUBLIC , 
				new org.apache.bcel.generic.ObjectType("String"),  
				org.apache.bcel.generic.Type.NO_ARGS, 
				new String[0], "toString", "String",
				il, cp);

		fi = new InstructionFactory(cg);
		il.append(InstructionFactory.createThis());
		il.append(fi.createFieldAccess("Integer", "v", org.apache.bcel.generic.Type.INT, Constants.GETFIELD));
		il.append(fi.createInvoke("java/lang/String", "valueOf", org.apache.bcel.generic.Type.STRING, new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.INT}, Constants.INVOKESTATIC));
		il.append(fi.createInvoke("String", "valueOf", new org.apache.bcel.generic.ObjectType ("String"), new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.STRING }, Constants.INVOKESTATIC));
		il.append(InstructionFactory.createReturn(Type.OBJECT));
		mg.setMaxStack();
		cg.addMethod(mg.getMethod());
		il.dispose(); // Allow instruction handles to be reused
		//createToStringOfString(cg, cp, il);


		// Last but not least we dump the JavaClass object to a file.
		try {
			cg.getJavaClass().dump(path + "/" + "Integer.class");
		} catch (java.io.IOException e) {
			System.err.println(e);
		}
}

	private static void createString(String path){
			ClassGen cg = new ClassGen("String", "Object",
					"<generated>", Constants.ACC_PUBLIC | Constants.ACC_SUPER, null);
			ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool
			InstructionList il = new InstructionList();
			// We then create the main method, supplying the method's name and the
			// symbolic type signature encoded with Type objects.
			cg.addEmptyConstructor(Constants.ACC_PUBLIC);
			
			
			
			cg.addField(new FieldGen(Constants.ACC_PUBLIC , org.apache.bcel.generic.Type.STRING, "v", cp).getField());
			MethodGen mg = new MethodGen(Constants.ACC_PUBLIC | Constants.ACC_STATIC, 
					new org.apache.bcel.generic.ObjectType("String"),
					new org.apache.bcel.generic.Type[] {org.apache.bcel.generic.Type.STRING},
					new String[] {"vv"}, "valueOf", "String",
					il, cp);
			
			InstructionFactory fi = new InstructionFactory(cg);
			il.append(fi.createNew("String"));
			il.append(new DUP()); 
			il.append(new DUP()); 
			il.append(fi.createInvoke("String", "<init>", org.apache.bcel.generic.Type.VOID, org.apache.bcel.generic.Type.NO_ARGS, Constants.INVOKESPECIAL));
			il.append(InstructionFactory.createLoad(org.apache.bcel.generic.Type.STRING, 0)); 
			il.append(fi.createFieldAccess("String", "v", org.apache.bcel.generic.Type.STRING, Constants.PUTFIELD));
			il.append(InstructionFactory.createReturn(new org.apache.bcel.generic.ObjectType("String")));
			mg.setMaxStack();
			cg.addMethod(mg.getMethod());
			il.dispose(); // Allow instruction handles to be reused


			
			mg = new MethodGen(Constants.ACC_PUBLIC , 
					org.apache.bcel.generic.Type.STRING,  
					org.apache.bcel.generic.Type.NO_ARGS, 
					new String[0], "strValue", "String",
					il, cp);
			
			fi = new InstructionFactory(cg);
			il.append(InstructionFactory.createThis()); 
			il.append(fi.createFieldAccess("String", "v", org.apache.bcel.generic.Type.STRING, Constants.GETFIELD));
			il.append(InstructionFactory.createReturn(org.apache.bcel.generic.Type.STRING)); 
			mg.setMaxStack();
			cg.addMethod(mg.getMethod());
			il.dispose(); // Allow instruction handles to be reused
			
			

			// CREATE TOSTRING 
			
			mg = new MethodGen(Constants.ACC_PUBLIC , 
					new org.apache.bcel.generic.ObjectType("String"),  
					org.apache.bcel.generic.Type.NO_ARGS, 
					new String[0], "toString", "String",
					il, cp);

			fi = new InstructionFactory(cg);
			il.append(InstructionFactory.createThis());
			il.append(InstructionFactory.createReturn(Type.OBJECT));
			mg.setMaxStack();
			cg.addMethod(mg.getMethod());
			il.dispose(); // Allow instruction handles to be reused
			//createToStringOfString(cg, cp, il);

			
				// Last but not least we dump the JavaClass object to a file.
			try {
				cg.getJavaClass().dump(path + "/" + "String.class");
			} catch (java.io.IOException e) {
				System.err.println(e);
			}
	}
	
	

	private static void createObject(String path) {
		ClassGen cg = new ClassGen("Object", "java.lang.Object",
				"<generated>", Constants.ACC_PUBLIC | Constants.ACC_SUPER, null);
		
		ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool
		InstructionList il = new InstructionList();
		// We then create the main method, supplying the method's name and the
		// symbolic type signature encoded with Type objects.
		cg.addEmptyConstructor(Constants.ACC_PUBLIC);
		
		createToStringOfObject(cg, cp, il);
		
		// Last but not least we dump the JavaClass object to a file.
		try {
			cg.getJavaClass().dump(path + "/" + "Object.class");
		} catch (java.io.IOException e) {
			System.err.println(e);
		}
	}

	
	
	private static void createToStringOfObject(ClassGen cg, ConstantPoolGen cp,
			InstructionList il) {
		MethodGen mg = new MethodGen(Constants.ACC_PUBLIC, 
				new org.apache.bcel.generic.ObjectType("String"),
				Type.NO_ARGS,
				new String[0], "toString", "Object",
				il, cp);
		il.append(new PUSH(cp, "<object>"));
		InstructionFactory fi = new InstructionFactory(cg); 
		il.append(fi.createInvoke("String", "valueOf", new org.apache.bcel.generic.ObjectType ("String"), new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.STRING }, Constants.INVOKESTATIC));
		il.append(InstructionFactory.createReturn(Type.OBJECT)); 
		mg.setMaxStack();
		cg.addMethod(mg.getMethod());
		il.dispose(); // Allow instruction handles to be reused
	}
}
