package latmod.core;

public class EventResized extends Event
{
	public LMFrame frame;
	public final int prevWidth;
	public final int prevHeight;
	
	public EventResized(LMFrame f, int pW, int pH)
	{
		frame = f;
		prevWidth = pW;
		prevHeight = pH;
	}
}