package plume.compiler;

import java.util.Collection;
import java.util.LinkedList;


import plume.lexer.LexerException;
import plume.node.*;
import plume.parser.ParserException;
import plume.visitors.util.GetLineAndColumn;
public class ErrorList {

	public  static Collection<String> errors = new LinkedList<String>(); 
	public final static GetLineAndColumn getLineAndColumn = new GetLineAndColumn(); 
	
	public static  void add(Node position, String error, String filePath) {
		if (position != null){
			position.apply(getLineAndColumn); 
			errors.add("\n" + filePath + ":" + getLineAndColumn.getLine() + ":" + getLineAndColumn.getColumn() + ":\n" + error );
		}
		else
			errors.add("\n" + filePath + ":?:?:\n" + error );
	}
	
	/**
	 * Complete the compilation process by printing the list of errors if any are present.
	 * @throws PlumeException if error list is not empty. 
	 */
	public static void epilog() throws PlumeException {
		if (errors.size() > 0) {
			System.out.println("Found " + errors.size() + " error"
					+ (errors.size() == 1 ? "" : "s") + "!");
			StringBuilder msg = new StringBuilder(); 
			for (String err : errors){
				msg.append(err); 
			}
			throw new PlumeException(msg.toString());
		}
	}	
	
	public static void addParserError(ParserException e, String filepath) {
		errors.add(filepath + ":" + getLine(e.getMessage()) + ":" +  getPos(e.getMessage()) + ":\n  Syntax error:"  + e.getMessage().substring(e.getMessage().indexOf(']') + 2));  
	}

	public  static void addLexerError(LexerException e, String filepath) {
		errors.add(filepath + ":" + getLine(e.getMessage()) + ":" +  getPos(e.getMessage()) + ":\n  Syntax error:"  + e.getMessage().substring(e.getMessage().indexOf(']') + 2));  
	}
	
	private static int getLine(String s) {
		return Integer.valueOf(s.substring(s.indexOf('[') + 1, s.indexOf(',')));
	}

	private static int getPos(String s) {
		return Integer.valueOf(s.substring(s.indexOf(',') + 1, s.indexOf(']')));
	}
}
