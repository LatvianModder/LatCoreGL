package latmod.core.input;

import latmod.core.IWindow;

public class EventMousePressed extends EventMouse
{
	public final int button;
	
	public EventMousePressed(IWindow w, int b)
	{
		super(w);
		button = b;
	}
	
	public boolean canCancel()
	{ return true; }
}