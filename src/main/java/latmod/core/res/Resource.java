package latmod.core.res;

public final class Resource implements Comparable<Resource>
{
	public static final Resource NULL = new Resource("null");
	
	public final String path;
	public final int hashCode;
	
	private Resource(String s)
	{
		s = String.valueOf(s).trim().replace("//", "/");
		if(s.charAt(0) != '/') s = '/' + s;
		path = s;
		hashCode = path.toLowerCase().hashCode();
	}
	
	public String toString()
	{ return path; }
	
	public int hashCode()
	{ return hashCode; }
	
	public int compareTo(Resource o)
	{ return path.compareToIgnoreCase(o.path); }
	
	public boolean equals(Object o)
	{ return o != null && (o == this || (hashCode == o.hashCode() && o.toString().equalsIgnoreCase(path))); }

	public static Resource get(String s)
	{ return new Resource(s); }

	public static Resource getTexture(String s)
	{ return get("textures/" + s); }

	public static Resource getSound(String s)
	{ return get("sounds/" + s + ".wav"); }
}