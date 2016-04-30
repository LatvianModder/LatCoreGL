package latmod.core.input;

import latmod.core.IWindow;

public abstract class EventMouse extends EventInput
{
	public EventMouse(IWindow w, boolean b)
	{
		super(w, b);
	}
}