package latmod.core.input;

public class EventMouseReleased extends EventMouse
{
	public final int button;
	public final long millis;
	
	public EventMouseReleased(int px, int py, int b, long m)
	{
		super(px, py);
		button = b;
		millis = m;
	}
}