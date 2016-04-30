package latmod.core.input;

import latmod.core.IWindow;

public abstract class EventKey extends EventInput
{
	public final int key;
	
	public EventKey(IWindow w, boolean b, int k)
	{
		super(w, b);
		key = k;
	}
}