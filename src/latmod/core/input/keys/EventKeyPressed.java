package latmod.core.input.keys;

import org.lwjgl.input.Keyboard;

import latmod.core.util.LMStringUtils;

public class EventKeyPressed extends EventKey
{
	public final char keyChar;
	
	public EventKeyPressed(int k, char c)
	{
		super(k);
		keyChar = c;
	}
	
	public boolean canCancel()
	{ return true; }
	
	public boolean isASCIIChar()
	{ return LMStringUtils.isASCIIChar(keyChar); }
	
	public boolean isEnter()
	{ return key == Keyboard.KEY_RETURN; }
	
	public boolean isBackspace()
	{ return key == Keyboard.KEY_BACK; }
	
	public boolean isTab()
	{ return key == Keyboard.KEY_TAB; }
}