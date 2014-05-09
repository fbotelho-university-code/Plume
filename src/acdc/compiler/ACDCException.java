package acdc.compiler;

/**
 * Any error occurring during compilation: lexical, syntactical, semantic.
 * 
 * @author Vasco T. Vasconcelos
 * @version $Id: ACDCException.java 396 2012-02-14 14:51:21Z vv $
 */
@SuppressWarnings("serial")
public class ACDCException extends Exception {

	public ACDCException(String string) {
		super(string);
	}
}
