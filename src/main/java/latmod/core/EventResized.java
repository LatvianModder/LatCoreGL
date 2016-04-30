package latmod.core;

public class EventResized extends Event
{
	public final int prevWidth;
	public final int prevHeight;
	
	public EventResized(IWindow w, int pW, int pH)
	{
		super(w, false);
		prevWidth = pW;
		prevHeight = pH;
	}
}