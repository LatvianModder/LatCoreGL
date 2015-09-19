package latmod.core.input.mouse;

public class EventMousePressed extends EventMouse
{
	public final int button;
	
	public EventMousePressed(int b)
	{
		button = b;
	}
	
	public boolean canCancel()
	{ return true; }
}