package latmod.core.input;

import latmod.core.Event;
import latmod.core.IWindow;

public abstract class EventInput extends Event
{
	public EventInput(IWindow w, boolean b)
	{
		super(w, b);
	}
}