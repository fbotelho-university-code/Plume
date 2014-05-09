/* This file was generated by SableCC (http://www.sablecc.org/). */

package plume.node;

import plume.analysis.*;

@SuppressWarnings("nls")
public final class AOpExpr extends PExpr
{
    private PExpr _left_;
    private POp _op_;
    private PExpr _right_;

    public AOpExpr()
    {
        // Constructor
    }

    public AOpExpr(
        @SuppressWarnings("hiding") PExpr _left_,
        @SuppressWarnings("hiding") POp _op_,
        @SuppressWarnings("hiding") PExpr _right_)
    {
        // Constructor
        setLeft(_left_);

        setOp(_op_);

        setRight(_right_);

    }

    @Override
    public Object clone()
    {
        return new AOpExpr(
            cloneNode(this._left_),
            cloneNode(this._op_),
            cloneNode(this._right_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAOpExpr(this);
    }

    public PExpr getLeft()
    {
        return this._left_;
    }

    public void setLeft(PExpr node)
    {
        if(this._left_ != null)
        {
            this._left_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._left_ = node;
    }

    public POp getOp()
    {
        return this._op_;
    }

    public void setOp(POp node)
    {
        if(this._op_ != null)
        {
            this._op_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._op_ = node;
    }

    public PExpr getRight()
    {
        return this._right_;
    }

    public void setRight(PExpr node)
    {
        if(this._right_ != null)
        {
            this._right_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._right_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._left_)
            + toString(this._op_)
            + toString(this._right_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._left_ == child)
        {
            this._left_ = null;
            return;
        }

        if(this._op_ == child)
        {
            this._op_ = null;
            return;
        }

        if(this._right_ == child)
        {
            this._right_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._left_ == oldChild)
        {
            setLeft((PExpr) newChild);
            return;
        }

        if(this._op_ == oldChild)
        {
            setOp((POp) newChild);
            return;
        }

        if(this._right_ == oldChild)
        {
            setRight((PExpr) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
