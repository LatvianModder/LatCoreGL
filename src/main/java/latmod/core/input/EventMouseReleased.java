package latmod.core.input;

import latmod.core.IWindow;

public class EventMouseReleased extends EventMouse
{
	public final int button;
	public final long millis;
	
	public EventMouseReleased(IWindow w, int b, long m)
	{
		super(w);
		button = b;
		millis = m;
	}
}