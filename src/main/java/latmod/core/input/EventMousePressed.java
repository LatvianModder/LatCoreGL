package latmod.core.input;

public class EventMousePressed extends EventMouse
{
	public final int button;
	
	public EventMousePressed(int px, int py, int b)
	{
		super(px, py);
		button = b;
	}
	
	public boolean canCancel()
	{ return true; }
}