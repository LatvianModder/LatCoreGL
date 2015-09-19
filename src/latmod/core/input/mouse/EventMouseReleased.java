package latmod.core.input.mouse;

public class EventMouseReleased extends EventMouse
{
	public final int button;
	public final long millis;
	
	public EventMouseReleased(int b, long m)
	{
		button = b;
		millis = m;
	}
}