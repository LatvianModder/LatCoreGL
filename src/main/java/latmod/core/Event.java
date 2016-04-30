package latmod.core;

/**
 * Created by LatvianModder on 30.04.2016.
 */
public abstract class Event
{
	public final IWindow window;
	private final boolean canCancel;
	private boolean isCancelled;
	
	public Event(IWindow w, boolean b)
	{
		window = w;
		canCancel = b;
	}
	
	public void cancel()
	{ isCancelled = true; }
	
	public boolean isCancelled()
	{ return canCancel && isCancelled; }
}