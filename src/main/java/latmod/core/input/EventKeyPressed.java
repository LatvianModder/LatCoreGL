package latmod.core.input;

import latmod.core.IWindow;
import latmod.lib.LMStringUtils;
import org.lwjgl.glfw.GLFW;

public class EventKeyPressed extends EventKey
{
	public final char keyChar;
	public final boolean repeat;
	
	public EventKeyPressed(IWindow w, int k, char c, boolean r)
	{
		super(w, k);
		keyChar = c;
		repeat = r;
	}
	
	@Override
	public boolean canCancel()
	{ return true; }
	
	public boolean isASCIIChar()
	{ return LMStringUtils.isASCIIChar(keyChar); }
	
	public boolean isEnter()
	{ return key == GLFW.GLFW_KEY_ENTER; }
	
	public boolean isBackspace()
	{ return key == GLFW.GLFW_KEY_BACKSPACE; }
	
	public boolean isTab()
	{ return key == GLFW.GLFW_KEY_TAB; }
}