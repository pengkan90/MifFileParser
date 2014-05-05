package org.philhosoft.mif.model.data;


public class None implements MifData
{
	@Override
	public <IN, OUT> OUT accept(Visitor<IN, OUT> visitor, IN in) throws Exception
	{
		return visitor.visit(this, in);
	}
}

