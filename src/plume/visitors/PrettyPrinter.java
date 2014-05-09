 
/**
 * A Pretty Printer for the generated AST from a parsed plume source. 
 * The final outcome is not very different from the source code.
 * In practice the output could pratically be provided as input again.
 *  
 * @author 41625 - Fabio Botelho
 * @version $Id 
 */

package plume.visitors;


import java.util.Iterator;
import java.util.LinkedList;

import plume.analysis.DepthFirstAdapter;
import plume.node.AAddOp;
import plume.node.AAinverseOp;
import plume.node.AAndOp;
import plume.node.AArg;
import plume.node.ABiggerOp;
import plume.node.ABiggereqOp;
import plume.node.ACastExpr;
import plume.node.AClass;
import plume.node.AConditionalExpr;
import plume.node.ADivOp;
import plume.node.AEqOp;
import plume.node.AErrorExpr;
import plume.node.AFalseExpr;
import plume.node.AFieldAccessExpr;
import plume.node.AFielddclMember;
import plume.node.AIdentifierExpr;
import plume.node.AInstanceofExpr;
import plume.node.AIntegerExpr;
import plume.node.ALowerOp;
import plume.node.ALowereqOp;
import plume.node.AMethodCallExpr;
import plume.node.AMethoddclMember;
import plume.node.AMinusOp;
import plume.node.AModOp;
import plume.node.AMulOp;
import plume.node.ANeqOp;
import plume.node.ANewObjExpr;
import plume.node.ANotOp;
import plume.node.AOpExpr;
import plume.node.AOrOp;
import plume.node.AStringExpr;
import plume.node.ASuperExpr;
import plume.node.AThisExpr;
import plume.node.ATrueExpr;
import plume.node.AType;
import plume.node.PArg;
import plume.node.PExpr;
import plume.node.PMember;
import plume.node.TId;
import plume.node.TKclass;
import plume.node.TKnew;


public class PrettyPrinter extends DepthFirstAdapter {
	private boolean inFields=false; //Used to find if already printed a field.
	private boolean inMethods=false; //Used to find if already printed a method. 
	private String ident = "    ";  //Space. 
	
	/**
	 * 
	 */
	public PrettyPrinter() {
		super();
	}
	
	/**
	 * Pretty print the class name and type. 
	 */
	@Override
	public void caseAClass(AClass node) {
		if (node.getKabstract() != null) 
			System.out.print("abstract "); 
		System.out.print("class "); 
		node.getType().apply(this);
		System.out.print("::");
		node.getExtends().apply(this);
		System.out.println(); 
		LinkedList<PMember> list = node.getMember(); 
		for (PMember p : list){
			p.apply(this); 
		}
	}
	
	/**
	 * Print the token class
	 */
	@Override
	public void caseTKclass(TKclass node) {
		System.out.print("class"); 
	}
	
	/**
	 * Print header for fields if first field visited.  
	 * Print the Field keyword before printing the actual field declaration (identifier and type).  
	 */
	//We have to visit inField and outField since we share AAtr with method arguments. TODO - is sharing a good ideia? 
	public void inAFielddMember(AFielddclMember node) {

	}
	
	
	/**
	 * Print the newline after printing a field member. 
	 */
	public void outAFielddcMember(AFielddclMember node){
		System.out.println(""); 
	}
	
	/**
	 * Print the Attribute. 
	 */
	
	@Override
	public void caseAArg(AArg node) {
		node.getId().apply(this); 
		System.out.print(" :: "); 
		node.getType().apply(this); 
	}

	/**
	 * Print the Method declaration. 
	 */
	@Override
	public void caseAMethoddclMember(AMethoddclMember node) {
		if (!this.inMethods){
			System.out.println("\n" + ident + "Methods :\n"); 
			this.inMethods = true; 
		}
		
		System.out.print(ident ); 
		node.getId().apply(this);
		
		System.out.print (" :: (");
		
		Iterator<PArg> it = node.getArgs().iterator(); 
		while (it.hasNext()){
            it.next().apply(this);
            if (it.hasNext() )  System.out.print(", ");  
         }
		
		System.out.print( ") -> "); 
		node.getType().apply(this); 
		System.out.print("is\n" + ident + "     "); 
		
		if(node.getBody() != null) {
            node.getBody().apply(this);
        }
		else {
			System.out.print("abstract"); 
		}
		
		System.out.println(""); 
	}


	/**
	 * Print the instance of expression. 
	 */
	//@Override
	public void caseAInstanceofExpr(AInstanceofExpr node) {
        System.out.print("("); 
		node.getExpr().apply(this);
		System.out.print(" instanceof ");
		node.getType().apply(this); 
        System.out.print(")"); 
	}
	
	

	

	/**
	 * Encapsulate OP expressions in (). Print (
	 */
	@Override
	public void inAOpExpr(AOpExpr node) {
		System.out.print("(");
	}

	/**
	 * Encapsulate OP expressions in (). Print )
	 */
	@Override
	public void outAOpExpr(AOpExpr node){
		System.out.print(")"); 
	}
	

	
	/**
	 * Print the cast 
	 */
	@Override
	public void caseACastExpr(ACastExpr node) {
		System.out.print("(("); 
		node.getType().apply(this);
		System.out.print(")"); 
		node.getExpr().apply(this); 
		System.out.print(") "); 
	}
	
	/**
	 * Print the method invocation with the possibly new keyword. 
	 */
	@Override
	public void caseAMethodCallExpr(AMethodCallExpr node) {
		node.getTarget().apply(this); 
		System.out.print(".");
		node.getId().apply(this); 
		System.out.print("("); 
		Iterator<PExpr> it = node.getExpr().iterator(); 
		while (it.hasNext()){
			it.next().apply(this);
			if (it.hasNext()) System.out.print(", "); 
		}
		System.out.print(")"); 
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#caseANewObjExpr(plume.node.ANewObjExpr)
	 */
	@Override
	public void caseANewObjExpr(ANewObjExpr node) {
		node.getKnew().apply(this); 
		node.getType().apply(this); 
		System.out.print("("); 
		Iterator<PExpr> it = node.getExpr().iterator(); 
		while (it.hasNext()){
			it.next().apply(this);
			if (it.hasNext()) System.out.print(", "); 
		}
		System.out.print(")"); 

	}

	/**
	 * Print the Conditional Expression.  
	 */
	@Override
	public void caseAConditionalExpr(AConditionalExpr node) {
		System.out.print("(if "); 
        node.getCond().apply(this);
        System.out.print(" then "); 
        node.getThen().apply(this);
        System.out.print(" else "); 
        node.getElse().apply(this);
        System.out.print(")");
	}

	
	/*
	 * Print the Operators : 
	 */
	
	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#caseAFielddclMember(plume.node.AFielddclMember)
	 */
	@Override
	public void caseAFielddclMember(AFielddclMember node) {
		if (!this.inFields) {
			System.out.println("\n" + ident + "// Attributes :\n");
			this.inFields = true; 
		}
		System.out.print(ident + "Field");
		node.getId().apply(this);
		System.out.print("::"); 
		node.getType().apply(this); 
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#caseAFieldAccessExpr(plume.node.AFieldAccessExpr)
	 */
	@Override
	public void caseAFieldAccessExpr(AFieldAccessExpr node) {
		node.getTarget().apply(this); 
		System.out.print("."); 
		node.getId().apply(this); 
	}

	/**
	 * Print the  new keyword.  
	 * @see plume.analysis.AnalysisAdapter#caseTKnew(plume.node.TKnew)
	 */
	@Override
	public void caseTKnew(TKnew node) {
		System.out.print(" new "); 
	}

	/**
	 * Print " + " 
	 */
	@Override
	public void inAAddOp(AAddOp node) {
		System.out.print( " + "); 
	}
	
	/**
	 * Print " / " 
	 */
	@Override
	public void inADivOp(ADivOp node) {
		System.out.print( " / "); 
	}

	/**
	 * Print " == " 
	 */
	@Override
	public void inAEqOp(AEqOp node) {
		System.out.print(" == "); 
	}

	/**
	 * Print " && " 
	 */
	@Override
	public void inAAndOp(AAndOp node) {
		System.out.print( " && "); 
	}


	/**
	 * Print "!" 
	 */
	@Override
	public void inANotOp(ANotOp node) {
		System.out.print("!"); 
	}
	
	

	/**
	 * Print the " || "  
	 */
	@Override
	public void inAOrOp(AOrOp node) {
		System.out.print(" || "); 
	}


	/**
	 * Print the " != "  
	 */
	@Override
	public void inANeqOp(ANeqOp node) {
		System.out.print(" != "); 
		
	}


	/**
	 * Print the " <= "  
	 */
	@Override
	public void inALowereqOp(ALowereqOp node) {
		System.out.print(" <= "); 
	}


	/**
	 * Print the " < "  
	 */
	@Override
	public void inALowerOp(ALowerOp node) {
		System.out.print(" < "); 
	}


	/**
	 * Print the " >= "  
	 */
	
	@Override
	public void inABiggereqOp(ABiggereqOp node) {
		System.out.print(" >= "); 
	}


	/**
	 * Print the " - "  
	 */
	@Override
	public void inABiggerOp(ABiggerOp node) {
		System.out.print(" > "); 
	}


	/**
	 * Print the " - "  
	 */
	@Override
	public void inAMinusOp(AMinusOp node) {
		System.out.print(" - "); 
	}

	/**
	 * Print the " % "  
	 */
	@Override
	public void inAModOp(AModOp node) {
		System.out.print(" % "); 
	}

	/**
	 * Print the " * "  
	 */
	@Override
	public void inAMulOp(AMulOp node) {
		System.out.print(" * "); 
	}

	@Override
	public void caseTId(TId node) {
		System.out.print(" " + node.getText() + " "); 
	}

	
	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inAStringExpr(plume.node.AStringExpr)
	 */
	@Override
	public void inAStringExpr(AStringExpr node) {
		System.out.print(node.getString().getText());
	}
	
	
	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#caseAAinverseOp(plume.node.AAinverseOp)
	 */
	@Override
	public void caseAAinverseOp(AAinverseOp node) {
		System.out.print("-"); 
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inAIdentifierExpr(plume.node.AIdentifierExpr)
	 */
	@Override
	public void inAIdentifierExpr(AIdentifierExpr node) {
		System.out.print(" " + node.getId().getText() + " ");
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inATrueExpr(plume.node.ATrueExpr)
	 */
	@Override
	public void inATrueExpr(ATrueExpr node) {
		System.out.print (" true ");
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inAFalseExpr(plume.node.AFalseExpr)
	 */
	@Override
	public void inAFalseExpr(AFalseExpr node) {
		System.out.print (" false ");
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inASuperExpr(plume.node.ASuperExpr)
	 */
	@Override
	public void inASuperExpr(ASuperExpr node) {
		System.out.print( " super "); 
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inAThisExpr(plume.node.AThisExpr)
	 */
	@Override
	public void inAThisExpr(AThisExpr node) {
		System.out.print( " this "); 
	}
	

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inAIntegerExpr(plume.node.AIntegerExpr)
	 */
	@Override
	public void inAIntegerExpr(AIntegerExpr node) {
		System.out.print( " " + node.getNumber().getText() + " " );
	}

	/* (non-Javadoc)
	 * @see plume.analysis.DepthFirstAdapter#inAErrorExpr(plume.node.AErrorExpr)
	 */
	@Override
	public void inAErrorExpr(AErrorExpr node) {
		System.out.print(" error "); 
	}
	
}
