package latmod.core.input.keys;

import latmod.core.input.EventInput;

public abstract class EventKey extends EventInput
{
	public final int key;
	
	public EventKey(int k)
	{
		key = k;
	}
}