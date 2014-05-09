/* This file was generated by SableCC (http://www.sablecc.org/). */

package plume.node;

import java.util.*;
import plume.analysis.*;

@SuppressWarnings("nls")
public final class ANewObjExpr extends PExpr
{
    private TKnew _knew_;
    private PType _type_;
    private final LinkedList<PExpr> _expr_ = new LinkedList<PExpr>();

    public ANewObjExpr()
    {
        // Constructor
    }

    public ANewObjExpr(
        @SuppressWarnings("hiding") TKnew _knew_,
        @SuppressWarnings("hiding") PType _type_,
        @SuppressWarnings("hiding") List<PExpr> _expr_)
    {
        // Constructor
        setKnew(_knew_);

        setType(_type_);

        setExpr(_expr_);

    }

    @Override
    public Object clone()
    {
        return new ANewObjExpr(
            cloneNode(this._knew_),
            cloneNode(this._type_),
            cloneList(this._expr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseANewObjExpr(this);
    }

    public TKnew getKnew()
    {
        return this._knew_;
    }

    public void setKnew(TKnew node)
    {
        if(this._knew_ != null)
        {
            this._knew_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._knew_ = node;
    }

    public PType getType()
    {
        return this._type_;
    }

    public void setType(PType node)
    {
        if(this._type_ != null)
        {
            this._type_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._type_ = node;
    }

    public LinkedList<PExpr> getExpr()
    {
        return this._expr_;
    }

    public void setExpr(List<PExpr> list)
    {
        this._expr_.clear();
        this._expr_.addAll(list);
        for(PExpr e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._knew_)
            + toString(this._type_)
            + toString(this._expr_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._knew_ == child)
        {
            this._knew_ = null;
            return;
        }

        if(this._type_ == child)
        {
            this._type_ = null;
            return;
        }

        if(this._expr_.remove(child))
        {
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._knew_ == oldChild)
        {
            setKnew((TKnew) newChild);
            return;
        }

        if(this._type_ == oldChild)
        {
            setType((PType) newChild);
            return;
        }

        for(ListIterator<PExpr> i = this._expr_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PExpr) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        throw new RuntimeException("Not a child.");
    }
}