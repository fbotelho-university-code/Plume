/**
 * This visitor is used to find the line/column of a token in the original source code..
 * If you got a  tree node (@see plume.node) then you can find the first token from there 
 * by applying this visitor on the node. 
 * 
 * Careful with this invocation. If the wrong node is passed you may not find any token.
 * Also , sablecc getLine/Column info fails in the column part usually.  
 * @author 41625@alunos.fc.ul.pt F�bio Botelho
 * @version $Id 
 */
package plume.visitors.util;

import plume.analysis.DepthFirstAdapter;
import plume.node.EOF;
import plume.node.TAnd;
import plume.node.TB;
import plume.node.TBe;
import plume.node.TColon;
import plume.node.TComma;
import plume.node.TComment;
import plume.node.TDiv;
import plume.node.TDot;
import plume.node.TEq;
import plume.node.TFalse;
import plume.node.TId;
import plume.node.TKabstract;
import plume.node.TKas;
import plume.node.TKclass;
import plume.node.TKelse;
import plume.node.TKerror;
import plume.node.TKextends;
import plume.node.TKfield;
import plume.node.TKif;
import plume.node.TKinstanceof;
import plume.node.TKis;
import plume.node.TKmethod;
import plume.node.TKnew;
import plume.node.TKoverride;
import plume.node.TKsuper;
import plume.node.TKthen;
import plume.node.TKthis;
import plume.node.TL;
import plume.node.TLPar;
import plume.node.TLe;
import plume.node.TLineComment;
import plume.node.TMinus;
import plume.node.TMod;
import plume.node.TMul;
import plume.node.TNeq;
import plume.node.TNot;
import plume.node.TNumber;
import plume.node.TOr;
import plume.node.TPlus;
import plume.node.TRPar;
import plume.node.TSeta;
import plume.node.TString;
import plume.node.TTrue;
import plume.node.TWhiteSpace;
/*
 * The ideia is to find the first token and stop. 
 * TODO : In the moment it still looks in more than the actual token. but we keep only the result of the first token found.  
 */
public class GetLineAndColumn extends DepthFirstAdapter {

	private int line; // The line of the first token found. 
	private int column;  //The column of the first token found 

	/**
	 * @return the line
	 */
	public int getLine() {
		return line;
	}



	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTString(plume.node.TString)
	 */
	@Override
	public void caseTString(TString node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTWhiteSpace(plume.node.TWhiteSpace)
	 */
	@Override
	public void caseTWhiteSpace(TWhiteSpace node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTPlus(plume.node.TPlus)
	 */
	@Override
	public void caseTPlus(TPlus node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTDiv(plume.node.TDiv)
	 */
	@Override
	public void caseTDiv(TDiv node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTMul(plume.node.TMul)
	 */
	@Override
	public void caseTMul(TMul node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTMod(plume.node.TMod)
	 */
	@Override
	public void caseTMod(TMod node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTMinus(plume.node.TMinus)
	 */
	@Override
	public void caseTMinus(TMinus node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTLe(plume.node.TLe)
	 */
	@Override
	public void caseTLe(TLe node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTL(plume.node.TL)
	 */
	@Override
	public void caseTL(TL node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTEq(plume.node.TEq)
	 */
	@Override
	public void caseTEq(TEq node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTNeq(plume.node.TNeq)
	 */
	@Override
	public void caseTNeq(TNeq node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTB(plume.node.TB)
	 */
	@Override
	public void caseTB(TB node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTBe(plume.node.TBe)
	 */
	@Override
	public void caseTBe(TBe node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTAnd(plume.node.TAnd)
	 */
	@Override
	public void caseTAnd(TAnd node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTOr(plume.node.TOr)
	 */
	@Override
	public void caseTOr(TOr node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTNot(plume.node.TNot)
	 */
	@Override
	public void caseTNot(TNot node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTColon(plume.node.TColon)
	 */
	@Override
	public void caseTColon(TColon node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTSeta(plume.node.TSeta)
	 */
	@Override
	public void caseTSeta(TSeta node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTComma(plume.node.TComma)
	 */
	@Override
	public void caseTComma(TComma node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTLPar(plume.node.TLPar)
	 */
	@Override
	public void caseTLPar(TLPar node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTRPar(plume.node.TRPar)
	 */
	@Override
	public void caseTRPar(TRPar node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTDot(plume.node.TDot)
	 */
	@Override
	public void caseTDot(TDot node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKclass(plume.node.TKclass)
	 */
	@Override
	public void caseTKclass(TKclass node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKmethod(plume.node.TKmethod)
	 */
	@Override
	public void caseTKmethod(TKmethod node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKoverride(plume.node.TKoverride)
	 */
	@Override
	public void caseTKoverride(TKoverride node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKextends(plume.node.TKextends)
	 */
	@Override
	public void caseTKextends(TKextends node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKfield(plume.node.TKfield)
	 */
	@Override
	public void caseTKfield(TKfield node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKif(plume.node.TKif)
	 */
	@Override
	public void caseTKif(TKif node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKthen(plume.node.TKthen)
	 */
	@Override
	public void caseTKthen(TKthen node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKelse(plume.node.TKelse)
	 */
	@Override
	public void caseTKelse(TKelse node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKnew(plume.node.TKnew)
	 */
	@Override
	public void caseTKnew(TKnew node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKthis(plume.node.TKthis)
	 */
	@Override
	public void caseTKthis(TKthis node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKsuper(plume.node.TKsuper)
	 */
	@Override
	public void caseTKsuper(TKsuper node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKis(plume.node.TKis)
	 */
	@Override
	public void caseTKis(TKis node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKerror(plume.node.TKerror)
	 */
	@Override
	public void caseTKerror(TKerror node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKabstract(plume.node.TKabstract)
	 */
	@Override
	public void caseTKabstract(TKabstract node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKinstanceof(plume.node.TKinstanceof)
	 */
	@Override
	public void caseTKinstanceof(TKinstanceof node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTTrue(plume.node.TTrue)
	 */
	@Override
	public void caseTTrue(TTrue node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTFalse(plume.node.TFalse)
	 */
	@Override
	public void caseTFalse(TFalse node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTKas(plume.node.TKas)
	 */
	@Override
	public void caseTKas(TKas node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTLineComment(plume.node.TLineComment)
	 */
	@Override
	public void caseTLineComment(TLineComment node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTComment(plume.node.TComment)
	 */
	@Override
	public void caseTComment(TComment node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTNumber(plume.node.TNumber)
	 */
	@Override
	public void caseTNumber(TNumber node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseTId(plume.node.TId)
	 */
	@Override
	public void caseTId(TId node) {
		column = node.getPos(); 
		line = node.getLine();
	}



	/* (non-Javadoc)
	 * @see plume.analysis.AnalysisAdapter#caseEOF(plume.node.EOF)
	 */
	@Override
	public void caseEOF(EOF node) {
		column = node.getPos(); 
		line = node.getLine();
	}
	
}
