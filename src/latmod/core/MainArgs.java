package latmod.core;

import latmod.core.util.*;

public class MainArgs
{
	private final String[] args;
	
	public MainArgs(String[] s)
	{
		args = (s == null) ? new String[0] : s;
	}
	
	public boolean has(String s)
	{
		if(s == null || s.isEmpty()) return false;
		for(int i = 0; i < args.length; i++)
			if(args[i].equals(s)) return true;
		return false;
	}
	
	public String get(String s)
	{
		if(args.length < 2) return null;
		for(int i = 0; i < args.length - 1; i++)
			if(args[i].equals(s)) return args[i + 1];
		return null;
	}
	
	public String get(String s, String def)
	{
		String s1 = get(s);
		return (s1 == null) ? def : s1;
	}
	
	public int getI(String s, int def, int min, int max)
	{
		String s1 = get(s);
		if(s1 == null) return def;
		return MathHelperLM.clampInt(Converter.toInt(s, def), min, max);
	}
	
	public float getF(String s, float def, float min, float max)
	{
		String s1 = get(s);
		if(s1 == null) return def;
		return MathHelperLM.clampFloat(Converter.toFloat(s, def), min, max);
	}
}