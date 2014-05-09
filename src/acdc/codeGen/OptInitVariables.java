package acdc.codeGen;

import java.util.HashMap;
import java.util.Map;

import acdc.ast.Assigning;
import acdc.ast.FloatConsting;
import acdc.ast.IntConsting;
import acdc.ast.Printing;
import acdc.ast.SymDeclaring;
import acdc.ast.SymReferencing;
import acdc.ast.Type;
import acdc.ast.Visitor;

/**
 * Visitors removes nodes that initialize some variable with constant value and initializes the value
 * This optimization performs better if the constant folding calculation optimization 
 * (@see acdc.codeGen.OptPreCompute) is done  <b>before</b> to detect initialized values with constants mathematical operations.
 * <b>Notice:</b> Currently implementation does not allow the optimization in case undeclared variables are referenced.   
 * @author F‡bio Botelho 41625 
 */

/*
 * Visits the tree in-order. So if it finds an assigning operation with constants before any manipulation of the variable 
 * we can safely initialize the variable with that constant value.
 * The initializations map is used to determine the order of "references" (in the general term)  to the variable. 
 * An assignment with the constant value inserts into the map the value.
 * Other operations insert into the map a null value wich means that we can not  initialize the variable.
*/

public class OptInitVariables extends Visitor{
	private Map<String,SymDeclaring> declarationMaps= new HashMap<String,SymDeclaring>();  //Updated when the traversal finds SymDeclarations. Also used to notice references to the variable other than assignment or assignment with non constant values by setting the value to null     
	
	// Map of initial values assigned to variables through Constants.
	private Map<String,Double> initializations = new HashMap<String,Double>(); //Updated when constant assignements are found before any reference to the variable.
	
	/*
	 * Visitor must check if it is the first reference to the variable and it is a constant assignment. 
	 * If so then it must change the Symdeclaring node in the declarationsMap.       
	 */
	@Override
	public void visit(Assigning node) {
		//Check if variable has not been used/manipulated yet.
		if (!initializations.containsKey(node.id)){
			//Then we can safely change its initialization value if the assignment is done with constants values.
			Double val = null; 
			if (node.expr instanceof FloatConsting ){
				val = Double.valueOf(((FloatConsting) node.expr).val); 
			}else if (node.expr instanceof IntConsting){
				val = Double.valueOf(((IntConsting) node.expr).val); 
			}
			if (val != null){
				//Constant value assignment. We eliminate the need to consider this assignment.
				node.constAssigningOnDeclaration =true;
				SymDeclaring ndecl =  declarationMaps.get(node.id);
				ndecl.initialValue = (ndecl.type == Type.INTEGER) ? String.valueOf(Math.round(val)) : String.valueOf(val); 
			}
		}
		super.visit(node);
	}


	@Override
	public void visit(SymDeclaring node){
		declarationMaps.put(node.id, node); 
	}
	
	@Override
	public void visit(SymReferencing node) {
		if (!initializations.containsKey(node.id)){
			initializations.put(node.id, null); 
		}
	}

	@Override
	public void visit(Printing node) {
		if (!initializations.containsKey(node.id)){
			initializations.put(node.id, null); 
		}
	}
}
