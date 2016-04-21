package latmod.core;

import latmod.lib.util.FinalIDObject;

public final class Resource extends FinalIDObject
{
	public static final Resource NULL = new Resource("null");
	
	private Resource(String s)
	{ super(('/' + String.valueOf(s).trim()).replace("//", "/")); }
	
	public static Resource get(String s)
	{ return new Resource(s); }
	
	public static Resource getTexture(String s)
	{ return get("textures/" + s); }
	
	public static Resource getSound(String s)
	{ return get("sounds/" + s + ".wav"); }
}