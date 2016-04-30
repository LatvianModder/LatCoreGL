package latmod.core.input;

import latmod.core.IWindow;

public class EventKeyReleased extends EventKey
{
	public final long millis;
	
	public EventKeyReleased(IWindow w, int k, long m)
	{
		super(w, false, k);
		millis = m;
	}
}