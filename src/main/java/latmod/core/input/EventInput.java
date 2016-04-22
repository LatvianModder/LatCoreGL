package latmod.core.input;

import latmod.core.*;

public abstract class EventInput extends Event
{
	public final IWindow window;
	
	public EventInput(IWindow w)
	{
		window = w;
	}
}