/* This file was generated by SableCC (http://www.sablecc.org/). */

package plume.node;

import plume.analysis.*;

@SuppressWarnings("nls")
public final class ABiggerOp extends POp
{

    public ABiggerOp()
    {
        // Constructor
    }

    @Override
    public Object clone()
    {
        return new ABiggerOp();
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseABiggerOp(this);
    }

    @Override
    public String toString()
    {
        return "";
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        throw new RuntimeException("Not a child.");
    }
}
