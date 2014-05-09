package plume.visitors;

/**
 * @author 41625@alunos.fc.ul.pt Fábio Botelho
 * @version $Id 
 */

import java.util.Map;

import plume.analysis.DepthFirstAdapter;
import plume.attr.*;
import plume.node.Node;
import plume.types.Type;


public abstract class PlumeVisitor extends DepthFirstAdapter {
	
	public final Map<String,ClassAttributes> topLevelSymbolTable;
	public final Map<Node,Type> nodeTypes; 
	protected  String fileName;
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	/**
	 * The PlumeVisitor constructor for Semantics. 
	 * @param topLevelSymbolTable The top level Symbol Table containing the classes attributes.
	 * @param fileName The filePath for the source associated with this tree. 
	 * @param nodeTypes The Map for node Types. 
	 */
	public PlumeVisitor(Map<String, ClassAttributes> topLevelSymbolTable,
			 String fileName, Map<Node,Type> nodeTypes) {
		super();
		this.topLevelSymbolTable = topLevelSymbolTable;

		this.fileName = fileName;
		this.nodeTypes = nodeTypes; 
	}
}
