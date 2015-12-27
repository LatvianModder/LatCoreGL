package latmod.core;

import latmod.lib.FastList;

public class EventSaveLog extends Event
{
	public FastList<String> log;
	
	public EventSaveLog(FastList<String> al)
	{ log = al; }
}