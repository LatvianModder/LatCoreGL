package latmod.core.input;

public abstract class EventKey extends EventInput
{
	public final int key;
	
	public EventKey(int k)
	{
		key = k;
	}
}