package latmod.core.input;

public abstract class EventMouse extends EventInput
{
	public final int x, y;
	
	public EventMouse(int px, int py)
	{
		x = px;
		y = py;
	}
}