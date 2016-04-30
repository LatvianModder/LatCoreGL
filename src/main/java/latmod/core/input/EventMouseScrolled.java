package latmod.core.input;

import latmod.core.IWindow;

public class EventMouseScrolled extends EventMouse
{
	public final boolean up;
	
	public EventMouseScrolled(IWindow w, boolean u)
	{
		super(w, true);
		up = u;
	}
}