package latmod.core.rendering;

import latmod.core.Event;

public class EventTextureLoaded extends Event
{
	public final Texture texture;
	
	public EventTextureLoaded(Texture t)
	{
		texture = t;
	}
}