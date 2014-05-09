/* This file was generated by SableCC (http://www.sablecc.org/). */

package plume.node;

import plume.analysis.*;

@SuppressWarnings("nls")
public final class TKabstract extends Token
{
    public TKabstract()
    {
        super.setText("abstract");
    }

    public TKabstract(int line, int pos)
    {
        super.setText("abstract");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TKabstract(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTKabstract(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TKabstract text.");
    }
}
