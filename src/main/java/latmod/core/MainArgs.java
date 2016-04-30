package latmod.core;

import com.google.gson.JsonObject;
import latmod.lib.LMJsonUtils;
import latmod.lib.LMStringUtils;
import latmod.lib.MathHelperLM;

public class MainArgs
{
	private JsonObject jsonObject;
	
	public MainArgs(String[] args)
	{
		String json = "{" + ((args == null) ? "" : LMStringUtils.unsplit(args, " ")) + "}";
		jsonObject = LMJsonUtils.fromJson(json).getAsJsonObject();
	}
	
	public JsonObject getJson()
	{ return jsonObject; }
	
	public boolean has(String s)
	{ return jsonObject.has(s); }
	
	public String get(String s)
	{ return has(s) ? jsonObject.get(s).getAsString() : null; }
	
	public String get(String s, String def)
	{
		String s1 = get(s);
		return (s1 == null) ? def : s1;
	}
	
	public int getI(String s, int def, int min, int max)
	{
		int val = has(s) ? jsonObject.get(s).getAsInt() : def;
		return MathHelperLM.clampInt(val, min, max);
	}
	
	public double getD(String s, double def, double min, double max)
	{
		double val = has(s) ? jsonObject.get(s).getAsDouble() : def;
		return MathHelperLM.clamp(val, min, max);
	}
}