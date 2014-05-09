package acdc.parser;

import static acdc.lexer.Token.ASSIGN;
import static acdc.lexer.Token.EOF;
import static acdc.lexer.Token.FLTDCL;
import static acdc.lexer.Token.FNUM;
import static acdc.lexer.Token.ID;
import static acdc.lexer.Token.INTDCL;
import static acdc.lexer.Token.INUM;
import static acdc.lexer.Token.MINUS;
import static acdc.lexer.Token.PLUS;
import static acdc.lexer.Token.PRINT;
import static acdc.lexer.Token.SROOT;
import acdc.ast.Assigning;
import acdc.ast.Computing;
import acdc.ast.FloatConsting;
import acdc.ast.IntConsting;
import acdc.ast.Node;
import acdc.ast.Op;
import acdc.ast.Printing;
import acdc.ast.Programming;
import acdc.ast.SymDeclaring;
import acdc.ast.SymReferencing;
import acdc.ast.Type;
import acdc.compiler.ACDCException;
import acdc.lexer.CharStream;
import acdc.lexer.Token;
import acdc.lexer.TokenStream;

/**
 * Recursive-descent parser based on the grammar given in Figure 2.1
 * 
 * @author cytron
 * @author Vasco T. Vasconcelos
 * @version $Id: ParserGeneratingAST.java 394 2012-02-14 13:36:43Z vv $
 */

public class ParserGeneratingAST {

	private TokenStream ts;

	/**
	 * The tree resulting from the parsing process.
	 */
	private Programming program;

	public ParserGeneratingAST(CharStream s) throws ACDCException {
		this.ts = new TokenStream(s);
		this.program = new Programming();
	}

	public Programming getProgram() {
		return program;
	}
	
	
	public void Prog() throws ACDCException {
		if (ts.peek() == FLTDCL || ts.peek() == INTDCL || ts.peek() == ID
				|| ts.peek() == PRINT || ts.peek() == EOF) {
			Dcls();
			Stmts();
			expect(EOF);
		} else
			error("expecting floatdcl, intdcl, id, print, or eof");
	}
	
	private void Dcls() throws ACDCException {
		if (ts.peek() == FLTDCL || ts.peek() == INTDCL) {
			Dcl();
			Dcls();
		} else if (ts.peek() == ID || ts.peek() == PRINT || ts.peek() == EOF) {
			// Do nothing for lambda-production
		} else
			error("expecting floatdcl, intdcl, id, print, or eof");
	}
	
	/**
	 * Here we place in our program a new node: integer of floating point
	 * variable declaration.
	 * @throws ACDCException 
	 */
	private void Dcl() throws ACDCException {
		Token id;
		if (ts.peek() == FLTDCL) {
			expect(FLTDCL);
			id = expect(ID);
			program.adoptChild(new SymDeclaring(id.val, Type.FLOAT));
		} else if (ts.peek() == INTDCL) {
			expect(INTDCL);
			id = expect(ID);
			program.adoptChild(new SymDeclaring(id.val, Type.INTEGER));
		} else
			error("expecting float or int declaration");
	}

	/**
	 * Figure 2.7 code
	 * @throws ACDCException 
	 */
	private void Stmts() throws ACDCException {
		if (ts.peek() == ID || ts.peek() == PRINT) {
			Stmt();
			Stmts();
		} else if (ts.peek() == EOF) {
			// Do nothing for lambda-production
		} else
			error("expecting id, print, or eof");
	}

	/**
	 * Here we place in our program a new node: a print or an assignment
	 * statement.
	 * @throws ACDCException 
	 */
	private void Stmt() throws ACDCException {
		if (ts.peek() == ID) {
			Token id = expect(ID);
			expect(ASSIGN);
			Node val = Val();
			Node expr = Expr(val);
			program.adoptChild(new Assigning(id.val, expr));
		} else if (ts.peek() == PRINT) {
			expect(PRINT);
			Token id = expect(ID);
			program.adoptChild(new Printing(id.val));
		} else
			error("expecting id or print");
	}

	/**
	 * @return The Node corresponding to the Expr production.
	 * @throws ACDCException 
	 */
	private Node Expr(Node left) throws ACDCException {
		if (ts.peek() == PLUS) {
			expect(PLUS);
			Node right = Val();
			return Expr(new Computing(left, Op.PLUS, right));
		} else if (ts.peek() == MINUS) {
			expect(MINUS);
			Node right = Val();
			return Expr(new Computing(left, Op.MINUS, right));
		} else if (ts.peek() == SROOT){
				expect(SROOT); 
				return Expr(new Computing(left, Op.SQRT, null));
		} else if (ts.peek() == ID || ts.peek() == PRINT || ts.peek() == EOF) {
			// Do nothing for lambda-production
			return left;
		} else {
			error("expecting plus, minus, @, id, print, or eof");
			return null;	// Don't care
		}
	}
	
	/**
	 * @return The Node corresponding to the Val production.
	 * @throws ACDCException 
	 */
	private Node Val() throws ACDCException {
		Token token;
		if (ts.peek() == ID) {
			token = expect(ID);
			return new SymReferencing(token.val);
		} else if (ts.peek() == INUM) {
			token = expect(INUM);
			return new IntConsting(token.val);
		} else if (ts.peek() == FNUM) {
			token = expect(FNUM);
			return new FloatConsting(token.val);
		} else {
			error("expecting id, inum, or fnum");
			return null;	// Don't care
		}
	}

	/**
	 * Checks whether the next token in the stream is the expected one. Removes
	 * the token from the stream.
	 * 
	 * @param token
	 *            The expected token type.
	 * @return The token that was removed from the stream.
	 * @throws ACDCException 
	 */
	private Token expect(int token) throws ACDCException {
		Token t = ts.advance();
		if (t.type != token)
			error("expecting token " + Token.token2str[token]
					+ " but found token " + Token.token2str[t.type]);
		return t;
	}

	private void error(String message) throws ACDCException {
		throw new ACDCException(message+ " but found " + Token.token2str[ts.peek()]);
	}
}
