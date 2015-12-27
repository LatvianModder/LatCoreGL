package latmod.core.input;

import latmod.core.LatCoreGL;
import latmod.core.input.keys.*;
import latmod.lib.*;
import org.lwjgl.input.Keyboard;

public class LMKeyboard
{
	private static final boolean keys[] = new boolean[Keyboard.KEYBOARD_SIZE];
	private static final long delays[] = new long[Keyboard.KEYBOARD_SIZE];
	
	public static void update()
	{
		Keyboard.poll();
		
		while(Keyboard.next())
		{
			int key = Keyboard.getEventKey();
			keys[key] = Keyboard.getEventKeyState();
			
			if(keys[key])
			{
				char keyChar = Keyboard.getEventCharacter();
				LatCoreGL.mainFrame.inputHandler.onKeyPressed(new EventKeyPressed(key, keyChar));
				delays[key] = LMUtils.millis();
			}
			else
			{
				long millis = LMUtils.millis() - delays[key];
				LatCoreGL.mainFrame.inputHandler.onKeyReleased(new EventKeyReleased(key, millis));
			}
		}
	}
	
	public static void destroy()
	{
		Keyboard.destroy();
	}
	
	public static IntList getKeysPressed()
	{
		IntList l = new IntList();
		for(int i = 0; i < keys.length; i++)
			if(keys[i]) l.add(i);
		return l;
	}
	
	public static boolean isKeyDown(int k)
	{ return k > 0 && k < keys.length && keys[k]; }
	
	public static boolean isCtrlDown()
	{ return isKeyDown(Keyboard.KEY_LCONTROL) || isKeyDown(Keyboard.KEY_RCONTROL); }
	
	public static boolean isShiftDown()
	{ return isKeyDown(Keyboard.KEY_LSHIFT) || isKeyDown(Keyboard.KEY_RSHIFT); }
}