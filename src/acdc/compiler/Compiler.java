package acdc.compiler;

import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import acdc.ast.PrettyPrinterVisitor;
import acdc.ast.Programming;
import acdc.ast.Visitor;
import acdc.codeGen.GenCode;
import acdc.codeGen.OptInitVariables;
import acdc.codeGen.OptPreCompute;
import acdc.codeGen.OptPrintAfterAssignment;
import acdc.lexer.CharStream;
import acdc.parser.ParserGeneratingAST;
import acdc.semant.GetSymbols;
import acdc.semant.TypeChecker;

public class Compiler {
		
	private CharStream sourceCode;
	private final List<String> erros = new LinkedList<String>(); // Lista de erros
	
	public Compiler(String sourceCode){
		this.sourceCode = new CharStream(new StringReader(sourceCode)); 
	}
		
	public void compile() throws ACDCException {
	//	TokenStream tokenStream = new TokenStream(new CharStream(new StringReader(sourceCode)));
		ParserGeneratingAST parser = new ParserGeneratingAST(sourceCode);
		
		Visitor []visits = new Visitor[6];
		HashMap<String, acdc.ast.Type> symboltable = new HashMap<String,acdc.ast.Type>(); 
		visits[0] = new GetSymbols(symboltable, erros);
		visits[1] = new TypeChecker(symboltable, erros);
		visits[2] = new OptPreCompute(symboltable); 
		visits[3] = new OptInitVariables();
		visits[4] = new OptPrintAfterAssignment(); 
		GenCode generator = (GenCode) (visits[5] = new GenCode());
		parser.Prog(); 
		Programming p = parser.getProgram();
		
		PrettyPrinterVisitor printer = new PrettyPrinterVisitor();
		p.accept(printer); 
		for(Visitor v : visits){
			if (erros.isEmpty()){
				p.accept(v); 
				System.out.println("After : " + v.getClass().getSimpleName());
				p.accept(printer); 
			}
		}
		
		if (erros.isEmpty()){
			try{
				FileOutputStream fout = new FileOutputStream("out.dc"); 
				fout.write(generator.getCode().getBytes()); 
				fout.flush(); fout.close();
			}catch(Exception e){
				throw new ACDCException ("Problems outputing file out.dc:"  + e.getMessage()); 
			}
		}
		else{
			for (String err : erros){
				System.err.println(err); 
			}
			throw new ACDCException(""); 
		}
	}
	
	public static void main(String [ ] args) throws ACDCException{
		Compiler c = new Compiler("i a  p a");
		c.compile(); 
	}

}
