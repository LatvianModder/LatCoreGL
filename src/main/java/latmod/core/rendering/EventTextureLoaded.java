package latmod.core.rendering;

import latmod.core.Event;
import latmod.core.IWindow;

public class EventTextureLoaded extends Event
{
	public final Texture texture;
	
	public EventTextureLoaded(IWindow w, Texture t)
	{
		super(w, false);
		texture = t;
	}
}