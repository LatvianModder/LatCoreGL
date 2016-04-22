package latmod.core;

import latmod.lib.IIDObject;

public final class Resource implements IIDObject, Comparable<Resource>
{
	public static final Resource NULL = new Resource("core", "null");
	
	private final String base;
	private final String path;
	private String ID;
	
	public Resource(String b, String p)
	{
		if(b == null || b.isEmpty()) b = "game";
		
		if(p == null || p.isEmpty()) throw new NullPointerException("Resource path can't be null!");
		
		base = b;
		path = p;
	}
	
	public Resource(String p)
	{
		if(p == null || p.isEmpty()) throw new NullPointerException("Resource path can't be null!");
		
		int i = p.indexOf(':');
		
		if(i == -1) throw new IllegalArgumentException("Missing ':' in " + p + '!');
		
		base = p.substring(0, i + 1);
		path = p.substring(i, p.length() - 1);
	}
	
	public String getBase()
	{ return base; }
	
	public String getPath()
	{ return path; }
	
	public String getID()
	{
		if(ID == null)
		{
			ID = base + ':' + path;
		}
		
		return ID;
	}
	
	public int hashCode()
	{ return getID().hashCode(); }
	
	public String toString()
	{ return getID(); }
	
	public boolean equals(Object o)
	{
		if(o == null) return false;
		else if(o == this) return true;
		else if(o instanceof Resource)
		{
			Resource r = (Resource) o;
			return r.base.equalsIgnoreCase(base) && r.path.equalsIgnoreCase(path);
		}
		return o.toString().equalsIgnoreCase(getID());
	}
	
	public int compareTo(Resource o)
	{
		int i = base.compareToIgnoreCase(o.base);
		return (i == 0) ? path.compareToIgnoreCase(o.path) : i;
	}
}