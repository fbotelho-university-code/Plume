/**
 * A compiler for the Plume language
 * 
 * @author 41625 F�bio Botelho
 * @version $Id 
 */

package plume.compiler;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import plume.visitors.gen.*;
import plume.attr.ClassAttributes;
import plume.attr.MethodAttributes;
import plume.lexer.Lexer;
import plume.node.Node;
import plume.node.Start;
import plume.parser.Parser;
import plume.types.ClassType;
import plume.types.Type;
import plume.visitors.PlumeVisitor;
import plume.visitors.semantics.AbstractMethodsInConcreteClasses;
import plume.visitors.semantics.AllClassesVisitor;
import plume.visitors.semantics.CheckCycles;
import plume.visitors.semantics.MemberDeclarationVisitor;
import plume.visitors.semantics.SuperClassesPass;
import plume.visitors.semantics.TypeChecking;


public class Compiler {
	/**
	 * The classes to compile. A Map from filepath to source 
	 */
	private Map<String,String> sources = new HashMap<String,String>();
	private Map<String,ClassAttributes> topLevel = new HashMap<String,ClassAttributes>();
	private Map<Node, Type> nodeTypes = new HashMap<Node, Type>(); 
	
	/**
	 * Construct with filepaths
	 * @param filenames FilePaths list of source code 
	 * @throws PlumeException  if it founds problem reading the file 
	 */
	public Compiler(Collection<String> filespath)  throws PlumeException{
		ErrorList.errors.clear(); 
		for (String s : filespath){
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(s));
			StringBuilder stringBuilder = new StringBuilder();
			String line = null; 
			String ls = System.getProperty("line.separator");
			while( ( line = reader.readLine() ) != null ) {
				stringBuilder.append( line );
				stringBuilder.append( ls );
			}
			sources.put(s, stringBuilder.toString());
			} catch (FileNotFoundException e) {
				throw new PlumeException("Could not open file: " + s  ); 
			} catch (IOException e) {
				throw new PlumeException("Problem reading file:" + e.getMessage()); 

			}
		}
	}
	
	/**
	 * Compile all sources contained in this Compiler! 
	 * Also Pretty prints the AST build from the source code.  
	 * @throws PlumeException 
	 * @throws Exception 
	 */
	public void compile() throws PlumeException {
		Map<String, Start> parsedTrees = new HashMap<String,Start>(); 
		for ( Map.Entry<String, String> source : sources.entrySet()){
			Start tree=null;
			tree = parse(source.getKey(), source.getValue());
			if (tree != null){
				parsedTrees.put(source.getKey(), tree);
			}
		}
		if (!parsedTrees.isEmpty()){
			checkSemantics(parsedTrees);
		}
		
		if (ErrorList.errors.isEmpty()){
			genCode(parsedTrees); 
		}
		ErrorList.epilog(); 
	}
	
	/**
	 * @param parsedTrees
	 */
	private void genCode(Map<String, Start> parsedTrees) {
		if (!parsedTrees.isEmpty()){
		String filename = parsedTrees.keySet().iterator().next(); 
		String directory = filename.substring(0,
					filename.lastIndexOf(File.separatorChar));
		
		GenPrimitives.genPrimitives(directory); 
		//TODO . refactor this. do not access fields ? 
		applyPlumeVisitor(parsedTrees, new GenCode(topLevel, null, nodeTypes));
		}
	}

	/**
	 * Start the semantic analysis of the parsed files. 
	 * @param parsedTrees A map from FilePath to the parsed Abstract Syntax Trees. 
	 */
	private void checkSemantics(Map<String, Start> parsedTrees) {
		//Top level map containing classes attributes.
		//Create initial symbols & check filename matches class.  
		initSymbolTable(parsedTrees, topLevel);
		//Add primitive type to symbol table. 
		addPrimitives(topLevel);
		//Do a super class pass.
		visitSupers(parsedTrees, topLevel, nodeTypes);

		checkCycles(parsedTrees, topLevel, nodeTypes); 
		//Check Members declarations :
		checkMembersDeclarations(parsedTrees, topLevel, nodeTypes);
		checkConcreteClassImplementAbstractMethods(parsedTrees, topLevel,
				nodeTypes); 
		addConstructorToClasses(topLevel);
		performTypeCheckingInExpressions(parsedTrees, topLevel, nodeTypes);
	}

	private void initSymbolTable(Map<String, Start> parsedTrees,
			Map<String, ClassAttributes> topLevel) {
		applyPlumeVisitor(parsedTrees, new AllClassesVisitor(topLevel,  null, null));
	}

	private void checkCycles(Map<String, Start> parsedTrees,
			Map<String, ClassAttributes> topLevel, Map<Node, Type> nodeTypes) {
		applyPlumeVisitor(parsedTrees, new CheckCycles(topLevel,  null, nodeTypes));
	}
	
	

	private void performTypeCheckingInExpressions(
			Map<String, Start> parsedTrees,
			Map<String, ClassAttributes> topLevel, Map<Node, Type> nodeTypes) {
		applyPlumeVisitor(parsedTrees, new TypeChecking(topLevel, null, nodeTypes));
	}

	private void addConstructorToClasses(Map<String, ClassAttributes> topLevel) {
		for(ClassAttributes c : topLevel.values()){
			c.addConstructor();
		}
	}

	private void checkConcreteClassImplementAbstractMethods(
			Map<String, Start> parsedTrees,
			Map<String, ClassAttributes> topLevel, Map<Node, Type> nodeTypes) {
		applyPlumeVisitor(parsedTrees, new AbstractMethodsInConcreteClasses(topLevel,  null, nodeTypes));
	}
	
	
	/**
	 * @param parsedTrees
	 * @param topLevel
	 */
	private void visitSupers(Map<String, Start> parsedTrees,
			Map<String, ClassAttributes> topLevel, Map<Node,Type> nodeTypes) { 
		applyPlumeVisitor(parsedTrees, new SuperClassesPass(topLevel,  null, nodeTypes));
	}

	/**
	 * @param parsedTrees
	 */
	private void checkMembersDeclarations(Map<String, Start> parsedTrees, Map<String,ClassAttributes> topLevelSymbolTable, Map<Node,Type> nodeTypes) {
		applyPlumeVisitor(parsedTrees, new MemberDeclarationVisitor(topLevelSymbolTable,  null, nodeTypes));
	}
	
	/**
	 * Used to set semantic information to the structure employed that is not acquired in the source code. 
	 * I.e., BuiltinTypes and Methods are inserted into the attributes and type structure. 
	 * @param topLevel
	 */
	private void addPrimitives(Map<String, ClassAttributes> topLevel) {
		ClassAttributes needle;
		for (ClassType builtin : Type.BUILTIN_TYPES){
			if ((needle = topLevel.get(builtin.className)) != null){
				ErrorList.add(needle.start, "Redeclaration of builtin type " + needle.getType().className, needle.filePath);  
			}
		}
		
		
		
		ClassAttributes oatr = new ClassAttributes(Type.OBJECT_TYPE, false, null, null);
		//Create toString(). 
		MethodAttributes m = new MethodAttributes(oatr, false, false, "toString"); 
		m.setReturnType(Type.STRING_TYPE);
		oatr.putMethod("toString", m);
		topLevel.put(Type.OBJECT_TYPE.className, oatr);
		for (ClassType ty : Type.BUILTIN_TYPES){
			if (ty != Type.OBJECT_TYPE){
				ClassAttributes attr = new ClassAttributes(ty, false, null, null);
				attr.setSuper(oatr); 
				topLevel.put(ty.className, attr);
			}
		}
	}
	
	/**
	 * Apply a PlumeVisitor to all ast .
	 * @param parsedTrees
	 * @param visitor
	 */
	private void applyPlumeVisitor(Map<String,Start> parsedTrees, PlumeVisitor visitor){
		for (Map.Entry<String, Start> entry : parsedTrees.entrySet()){
			visitor.setFileName(entry.getKey()); 
			entry.getValue().apply(visitor); 
		}
	}
	
	

	
	/**
	 * Parse the source code and build the AST. Encapsulates the lexer also.  
	 * @param filepath The filepath of the @param source used for error messages; 
	 * @param source The string containing the source code; 
	 * @return The Start node for the Abstract Syntax Tree constructed by the parser;  
	 * @throws PlumeException if any error is found.  
	 */
	private Start parse(String filepath, String source) throws PlumeException {
		Start tree = null;
		//System.out.println("Compiling " + source + "...");
		try {
			CharArrayReader reader = new CharArrayReader(source.toCharArray());
			Parser p = new Parser(new Lexer(new PushbackReader(reader, 1024)));
			tree = p.parse();
		} catch (plume.parser.ParserException e) {
			ErrorList.addParserError(e, filepath);
		} catch (plume.lexer.LexerException e) {
			ErrorList.addLexerError(e, filepath);
		} catch (IOException e) {
			ErrorList.add(null, "IO problem while reading file " , filepath);
		} 

		return tree;
	}
	
}
