package latmod.core.res;

public final class Resource implements Comparable<Resource>
{
	public static final Resource NULL = new Resource("");
	
	public final String path;
	
	private Resource(String s)
	{ path = s; }
	
	public String toString()
	{ return path; }
	
	public int hashCode()
	{ return path.hashCode(); }
	
	public int compareTo(Resource o)
	{ return path.compareTo(o.path); }
	
	public boolean equals(Object o)
	{ return o.toString().equals(path); }
	
	public static Resource get(String s)
	{ return new Resource("/" + s); }
	
	public static Resource getTexture(String s)
	{ return get("textures/" + s); }
	
	public static Resource getSound(String s)
	{ return get("sounds/" + s + ".wav"); }
}