/* This file was generated by SableCC (http://www.sablecc.org/). */

package plume.node;

import plume.analysis.*;

@SuppressWarnings("nls")
public final class AErrorExpr extends PExpr
{
    private TKerror _kerror_;

    public AErrorExpr()
    {
        // Constructor
    }

    public AErrorExpr(
        @SuppressWarnings("hiding") TKerror _kerror_)
    {
        // Constructor
        setKerror(_kerror_);

    }

    @Override
    public Object clone()
    {
        return new AErrorExpr(
            cloneNode(this._kerror_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAErrorExpr(this);
    }

    public TKerror getKerror()
    {
        return this._kerror_;
    }

    public void setKerror(TKerror node)
    {
        if(this._kerror_ != null)
        {
            this._kerror_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._kerror_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._kerror_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._kerror_ == child)
        {
            this._kerror_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._kerror_ == oldChild)
        {
            setKerror((TKerror) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
