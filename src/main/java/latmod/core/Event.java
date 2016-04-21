package latmod.core;

/**
 * Made by LatvianModder
 */
public abstract class Event
{
	private boolean cancelled = false;
	
	public boolean cancel()
	{
		if(!cancelled && canCancel())
		{
			cancelled = true;
			return true;
		}
		
		return false;
	}
	
	public boolean isCancelled()
	{ return cancelled; }
	
	public boolean canCancel()
	{ return false; }
}