package latmod.core.rendering;
import latmod.core.Event;

public class EventTextureLoaded extends Event
{
	public final Texture texture;
	public final boolean isUnknown;
	public final boolean isBlured;
	
	public EventTextureLoaded(Texture t, boolean b, boolean b1)
	{
		texture = t;
		isUnknown = b;
		isBlured = b1;
	}
}