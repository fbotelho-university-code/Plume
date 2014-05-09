/**
 * An insignificant visitor. Sorts the members of a class such that all fields 
 * appear before the methods.
 *  
 * @author 41625@alunos.fc.ul.pt
 * @version $id 
 */
package plume.visitors;

import java.util.Collections;
import java.util.Comparator;

import plume.analysis.DepthFirstAdapter;
import plume.node.*; 

/**
 * @author 41625 Fabio Botelho
 * This visitors changes the tree by ordering the class members 
 * such that fields declaration appear before all methods  
 */
public class SortClassMembers extends DepthFirstAdapter {

	
	// Overriding caseMembersClass. This way we do not have to adapt all the tree 
	@Override 
	public void caseAClass(AClass node) {	
		Collections.sort(node.getMember(), 
				new Comparator<PMember>() { 
					public int compare(PMember ob , PMember ob2){
						//fields are smaller than Methods case we want them first in the list.
						//As the class name length is also smaller (field vs method we use that to compare 
						return ob.getClass().getSimpleName().length() - ob2.getClass().getSimpleName().length(); 
						}
					}
		); 
	}
	
}
