/* This file was generated by SableCC (http://www.sablecc.org/). */

package plume.node;

import plume.analysis.*;

@SuppressWarnings("nls")
public final class TKmethod extends Token
{
    public TKmethod()
    {
        super.setText("method");
    }

    public TKmethod(int line, int pos)
    {
        super.setText("method");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TKmethod(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTKmethod(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TKmethod text.");
    }
}