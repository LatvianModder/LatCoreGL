package latmod.core.input;

import latmod.core.LatCoreGL;
import latmod.lib.LMUtils;
import org.lwjgl.input.Mouse;

public class LMMouse
{
	public static int x, y, wheel;
	public static int dx, dy, dwheel;
	private static final boolean[] buttons = new boolean[16];
	private static final long[] delays = new long[buttons.length];
	
	public static void update()
	{
		Mouse.poll();
		
		x = Mouse.getX();
		y = LatCoreGL.window.getHeight() - Mouse.getY() - 1;
		dx = Mouse.getDX();
		dy = Mouse.getDY();
		
		while(Mouse.next())
		{
			int button = Mouse.getEventButton();
			
			if(button >= 0 && button < buttons.length)
			{
				buttons[button] = Mouse.getEventButtonState();
				
				if(buttons[button])
				{
					delays[button] = LMUtils.millis();
					LatCoreGL.window.onMousePressed(new EventMousePressed(x, y, button));
				}
				else
				{
					long millis = LMUtils.millis() - delays[button];
					LatCoreGL.window.onMouseReleased(new EventMouseReleased(x, y, button, millis));
				}
			}
		}
		
		dwheel = Mouse.getDWheel();
		
		if(dwheel != 0)
		{
			wheel += dwheel;
			LatCoreGL.window.onMouseScrolled(new EventMouseScrolled(x, y, dwheel < 0));
		}
	}
	
	public static void destroy()
	{ Mouse.destroy(); }
	
	public static boolean isButtonDown(int i)
	{ return i >= 0 && i <= buttons.length && buttons[i]; }
}