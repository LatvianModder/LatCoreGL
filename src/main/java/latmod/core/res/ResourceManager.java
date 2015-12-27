package latmod.core.res;
import java.io.*;

import latmod.lib.FastList;

public abstract class ResourceManager
{
	public static enum Type
	{
		STREAM,
		FILE,
		URL
	};
	
	public final Type type;
	
	public ResourceManager(Type t)
	{ type = t; }
	
	public abstract InputStream getInputStream(Resource r) throws Exception;
	public abstract String getPath(Resource r);
	
	/** Might not support large files */
	public byte[] readBytes(Resource r) throws Exception
	{
		InputStream is = getInputStream(r);
		byte[] b = new byte[is.available()];
		is.read(b); return b;
	}
	
	/** Might not support large files */
	public String readText(Resource r)
	{
		try
		{
			byte[] b = readBytes(r);
			return new String(b);
		}
		catch(Exception e)
		{ e.printStackTrace(); }
		
		return null;
	}
	
	/** Might be better than readText() */
	public FastList<String> readTextList(Resource r)
	{
		if(r == null) return null;
		
		FastList<String> al = new FastList<String>();
		
		try
		{
			InputStream is = getInputStream(r);
			BufferedReader br = new BufferedReader(new InputStreamReader(is), is.available());
			String s = null; while((s = br.readLine()) != null) al.add(s);
		}
		catch(Exception e)
		{ e.printStackTrace(); }
		
		return al;
	}
}