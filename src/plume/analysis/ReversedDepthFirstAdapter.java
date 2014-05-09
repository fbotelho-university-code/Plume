/* This file was generated by SableCC (http://www.sablecc.org/). */

package plume.analysis;

import java.util.*;
import plume.node.*;

public class ReversedDepthFirstAdapter extends AnalysisAdapter
{
    public void inStart(Start node)
    {
        defaultIn(node);
    }

    public void outStart(Start node)
    {
        defaultOut(node);
    }

    public void defaultIn(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    public void defaultOut(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    @Override
    public void caseStart(Start node)
    {
        inStart(node);
        node.getEOF().apply(this);
        node.getPClass().apply(this);
        outStart(node);
    }

    public void inAClass(AClass node)
    {
        defaultIn(node);
    }

    public void outAClass(AClass node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAClass(AClass node)
    {
        inAClass(node);
        {
            List<PMember> copy = new ArrayList<PMember>(node.getMember());
            Collections.reverse(copy);
            for(PMember e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getExtends() != null)
        {
            node.getExtends().apply(this);
        }
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        if(node.getKabstract() != null)
        {
            node.getKabstract().apply(this);
        }
        outAClass(node);
    }

    public void inAType(AType node)
    {
        defaultIn(node);
    }

    public void outAType(AType node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAType(AType node)
    {
        inAType(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAType(node);
    }

    public void inAFielddclMember(AFielddclMember node)
    {
        defaultIn(node);
    }

    public void outAFielddclMember(AFielddclMember node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFielddclMember(AFielddclMember node)
    {
        inAFielddclMember(node);
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAFielddclMember(node);
    }

    public void inAMethoddclMember(AMethoddclMember node)
    {
        defaultIn(node);
    }

    public void outAMethoddclMember(AMethoddclMember node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMethoddclMember(AMethoddclMember node)
    {
        inAMethoddclMember(node);
        if(node.getKoverride() != null)
        {
            node.getKoverride().apply(this);
        }
        if(node.getBody() != null)
        {
            node.getBody().apply(this);
        }
        {
            List<PArg> copy = new ArrayList<PArg>(node.getArgs());
            Collections.reverse(copy);
            for(PArg e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAMethoddclMember(node);
    }

    public void inAArg(AArg node)
    {
        defaultIn(node);
    }

    public void outAArg(AArg node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAArg(AArg node)
    {
        inAArg(node);
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAArg(node);
    }

    public void inAConditionalExpr(AConditionalExpr node)
    {
        defaultIn(node);
    }

    public void outAConditionalExpr(AConditionalExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAConditionalExpr(AConditionalExpr node)
    {
        inAConditionalExpr(node);
        if(node.getElse() != null)
        {
            node.getElse().apply(this);
        }
        if(node.getThen() != null)
        {
            node.getThen().apply(this);
        }
        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }
        outAConditionalExpr(node);
    }

    public void inAOpExpr(AOpExpr node)
    {
        defaultIn(node);
    }

    public void outAOpExpr(AOpExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAOpExpr(AOpExpr node)
    {
        inAOpExpr(node);
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        if(node.getOp() != null)
        {
            node.getOp().apply(this);
        }
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        outAOpExpr(node);
    }

    public void inAMethodCallExpr(AMethodCallExpr node)
    {
        defaultIn(node);
    }

    public void outAMethodCallExpr(AMethodCallExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMethodCallExpr(AMethodCallExpr node)
    {
        inAMethodCallExpr(node);
        {
            List<PExpr> copy = new ArrayList<PExpr>(node.getExpr());
            Collections.reverse(copy);
            for(PExpr e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getTarget() != null)
        {
            node.getTarget().apply(this);
        }
        outAMethodCallExpr(node);
    }

    public void inANewObjExpr(ANewObjExpr node)
    {
        defaultIn(node);
    }

    public void outANewObjExpr(ANewObjExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANewObjExpr(ANewObjExpr node)
    {
        inANewObjExpr(node);
        {
            List<PExpr> copy = new ArrayList<PExpr>(node.getExpr());
            Collections.reverse(copy);
            for(PExpr e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        if(node.getKnew() != null)
        {
            node.getKnew().apply(this);
        }
        outANewObjExpr(node);
    }

    public void inAFieldAccessExpr(AFieldAccessExpr node)
    {
        defaultIn(node);
    }

    public void outAFieldAccessExpr(AFieldAccessExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFieldAccessExpr(AFieldAccessExpr node)
    {
        inAFieldAccessExpr(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getTarget() != null)
        {
            node.getTarget().apply(this);
        }
        outAFieldAccessExpr(node);
    }

    public void inAInstanceofExpr(AInstanceofExpr node)
    {
        defaultIn(node);
    }

    public void outAInstanceofExpr(AInstanceofExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAInstanceofExpr(AInstanceofExpr node)
    {
        inAInstanceofExpr(node);
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        outAInstanceofExpr(node);
    }

    public void inACastExpr(ACastExpr node)
    {
        defaultIn(node);
    }

    public void outACastExpr(ACastExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseACastExpr(ACastExpr node)
    {
        inACastExpr(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        outACastExpr(node);
    }

    public void inAIdentifierExpr(AIdentifierExpr node)
    {
        defaultIn(node);
    }

    public void outAIdentifierExpr(AIdentifierExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIdentifierExpr(AIdentifierExpr node)
    {
        inAIdentifierExpr(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        outAIdentifierExpr(node);
    }

    public void inAStringExpr(AStringExpr node)
    {
        defaultIn(node);
    }

    public void outAStringExpr(AStringExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAStringExpr(AStringExpr node)
    {
        inAStringExpr(node);
        if(node.getString() != null)
        {
            node.getString().apply(this);
        }
        outAStringExpr(node);
    }

    public void inAIntegerExpr(AIntegerExpr node)
    {
        defaultIn(node);
    }

    public void outAIntegerExpr(AIntegerExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIntegerExpr(AIntegerExpr node)
    {
        inAIntegerExpr(node);
        if(node.getNumber() != null)
        {
            node.getNumber().apply(this);
        }
        outAIntegerExpr(node);
    }

    public void inATrueExpr(ATrueExpr node)
    {
        defaultIn(node);
    }

    public void outATrueExpr(ATrueExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseATrueExpr(ATrueExpr node)
    {
        inATrueExpr(node);
        if(node.getTrue() != null)
        {
            node.getTrue().apply(this);
        }
        outATrueExpr(node);
    }

    public void inAFalseExpr(AFalseExpr node)
    {
        defaultIn(node);
    }

    public void outAFalseExpr(AFalseExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFalseExpr(AFalseExpr node)
    {
        inAFalseExpr(node);
        if(node.getFalse() != null)
        {
            node.getFalse().apply(this);
        }
        outAFalseExpr(node);
    }

    public void inAErrorExpr(AErrorExpr node)
    {
        defaultIn(node);
    }

    public void outAErrorExpr(AErrorExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAErrorExpr(AErrorExpr node)
    {
        inAErrorExpr(node);
        if(node.getKerror() != null)
        {
            node.getKerror().apply(this);
        }
        outAErrorExpr(node);
    }

    public void inASuperExpr(ASuperExpr node)
    {
        defaultIn(node);
    }

    public void outASuperExpr(ASuperExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASuperExpr(ASuperExpr node)
    {
        inASuperExpr(node);
        if(node.getKsuper() != null)
        {
            node.getKsuper().apply(this);
        }
        outASuperExpr(node);
    }

    public void inAThisExpr(AThisExpr node)
    {
        defaultIn(node);
    }

    public void outAThisExpr(AThisExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAThisExpr(AThisExpr node)
    {
        inAThisExpr(node);
        outAThisExpr(node);
    }

    public void inAAddOp(AAddOp node)
    {
        defaultIn(node);
    }

    public void outAAddOp(AAddOp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAddOp(AAddOp node)
    {
        inAAddOp(node);
        outAAddOp(node);
    }

    public void inADivOp(ADivOp node)
    {
        defaultIn(node);
    }

    public void outADivOp(ADivOp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseADivOp(ADivOp node)
    {
        inADivOp(node);
        outADivOp(node);
    }

    public void inAEqOp(AEqOp node)
    {
        defaultIn(node);
    }

    public void outAEqOp(AEqOp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAEqOp(AEqOp node)
    {
        inAEqOp(node);
        outAEqOp(node);
    }

    public void inAAndOp(AAndOp node)
    {
        defaultIn(node);
    }

    public void outAAndOp(AAndOp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAndOp(AAndOp node)
    {
        inAAndOp(node);
        outAAndOp(node);
    }

    public void inANotOp(ANotOp node)
    {
        defaultIn(node);
    }

    public void outANotOp(ANotOp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANotOp(ANotOp node)
    {
        inANotOp(node);
        outANotOp(node);
    }

    public void inAOrOp(AOrOp node)
    {
        defaultIn(node);
    }

    public void outAOrOp(AOrOp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAOrOp(AOrOp node)
    {
        inAOrOp(node);
        outAOrOp(node);
    }

    public void inANeqOp(ANeqOp node)
    {
        defaultIn(node);
    }

    public void outANeqOp(ANeqOp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANeqOp(ANeqOp node)
    {
        inANeqOp(node);
        outANeqOp(node);
    }

    public void inALowereqOp(ALowereqOp node)
    {
        defaultIn(node);
    }

    public void outALowereqOp(ALowereqOp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALowereqOp(ALowereqOp node)
    {
        inALowereqOp(node);
        outALowereqOp(node);
    }

    public void inALowerOp(ALowerOp node)
    {
        defaultIn(node);
    }

    public void outALowerOp(ALowerOp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALowerOp(ALowerOp node)
    {
        inALowerOp(node);
        outALowerOp(node);
    }

    public void inABiggereqOp(ABiggereqOp node)
    {
        defaultIn(node);
    }

    public void outABiggereqOp(ABiggereqOp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseABiggereqOp(ABiggereqOp node)
    {
        inABiggereqOp(node);
        outABiggereqOp(node);
    }

    public void inABiggerOp(ABiggerOp node)
    {
        defaultIn(node);
    }

    public void outABiggerOp(ABiggerOp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseABiggerOp(ABiggerOp node)
    {
        inABiggerOp(node);
        outABiggerOp(node);
    }

    public void inAMinusOp(AMinusOp node)
    {
        defaultIn(node);
    }

    public void outAMinusOp(AMinusOp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMinusOp(AMinusOp node)
    {
        inAMinusOp(node);
        outAMinusOp(node);
    }

    public void inAModOp(AModOp node)
    {
        defaultIn(node);
    }

    public void outAModOp(AModOp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAModOp(AModOp node)
    {
        inAModOp(node);
        outAModOp(node);
    }

    public void inAMulOp(AMulOp node)
    {
        defaultIn(node);
    }

    public void outAMulOp(AMulOp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMulOp(AMulOp node)
    {
        inAMulOp(node);
        outAMulOp(node);
    }

    public void inAAinverseOp(AAinverseOp node)
    {
        defaultIn(node);
    }

    public void outAAinverseOp(AAinverseOp node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAinverseOp(AAinverseOp node)
    {
        inAAinverseOp(node);
        outAAinverseOp(node);
    }
}
