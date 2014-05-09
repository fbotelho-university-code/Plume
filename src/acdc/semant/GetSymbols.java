package acdc.semant;

import java.util.List;
import java.util.Map;

import acdc.ast.Assigning;
import acdc.ast.Printing;
import acdc.ast.SymDeclaring;
import acdc.ast.SymReferencing;
import acdc.ast.Type;
import acdc.ast.Visitor;

/**
 * Visitor capable of constructing symbol table for ac language. 
 * The language only supports variable symbols. 
 * The visitor completes an associative array from id to type of variables
 * 
 * @author Fábio Botelho - 41625 
 *
 */
public class GetSymbols extends Visitor{

	public final Map<String,Type> symbolTable; // Tabela de simbolos (apenas variaveis ) do programa.  
	private final List<String> erros; 
	
	
	/**
	 * Construtor do Visitor
	 * @param map A tabela de simbolos a construir. 
	 * @param erros A lista de erros no processo de compilação. 
	 */
	public GetSymbols(Map<String,Type> map, List<String> erros){
		this.symbolTable = map; 
		this.erros = erros; 
	}
	
	@Override
	public void visit(SymDeclaring node) {
		if (symbolTable.containsKey(node.id)){
			//Overriding variable with new type. Not sure if the correct behaviour. Avoiding type related problems with redeclared variable but creating problems with first declaration
			erros.add("Redeclaration of variable " +  node.id);
		}
		else{
			symbolTable.put(node.id, node.type); 
		}
	}
		
	public void visit(SymReferencing node){
		checkReferencedVariable(node.id);
	}
	
	public void visit(Assigning node){
		checkReferencedVariable(node.id);
		super.visit(node); 
	}

	
	public void visit(Printing node){
		checkReferencedVariable(node.id); 
	}
	
	private void checkReferencedVariable(String id) {
		if (!symbolTable.containsKey(id)){
			 erros.add("Undeclared variable referenced" + id);
			//implicit declaration of variable to avoid errors related to it. 
			 symbolTable.put(id, acdc.ast.Type.FLOAT); 
		 }
	}

}
