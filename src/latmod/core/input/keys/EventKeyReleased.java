package latmod.core.input.keys;

public class EventKeyReleased extends EventKey
{
	public final long millis;
	
	public EventKeyReleased(int k, long m)
	{
		super(k);
		millis = m;
	}
}