package latmod.core.input;

public class EventMouseScrolled extends EventMouse
{
	public final boolean up;
	
	public EventMouseScrolled(int px, int py, boolean u)
	{
		super(px, py);
		up = u;
	}
}