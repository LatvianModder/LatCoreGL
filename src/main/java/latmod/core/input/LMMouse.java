package latmod.core.input;

import org.lwjgl.input.Mouse;

import latmod.core.LatCoreGL;
import latmod.core.input.mouse.*;
import latmod.lib.LMUtils;
import latmod.lib.util.Box2D;

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
		y = LatCoreGL.getHeight() - Mouse.getY() - 1;
		dx = Mouse.getDX();
		dy = Mouse.getDY();
		
		if(dx != 0 || dy != 0) LatCoreGL.mainFrame.inputHandler.onMouseMoved(new EventMouseMoved());
		
		while(Mouse.next())
		{
			int button = Mouse.getEventButton();
			
			if(button >= 0 && button < buttons.length)
			{
				buttons[button] = Mouse.getEventButtonState();
				
				if(buttons[button])
				{
					delays[button] = LMUtils.millis();
					LatCoreGL.mainFrame.inputHandler.onMousePressed(new EventMousePressed(button));
				}
				else
				{
					long millis = LMUtils.millis() - delays[button];
					LatCoreGL.mainFrame.inputHandler.onMouseReleased(new EventMouseReleased(button, millis));
				}
			}
		}
		
		dwheel = Mouse.getDWheel();
		
		if(dwheel != 0)
		{
			wheel += dwheel;
			LatCoreGL.mainFrame.inputHandler.onMouseScrolled(new EventMouseScrolled());
		}
	}
	
	public static void destroy()
	{ Mouse.destroy(); }
	
	public static boolean isButtonDown(int i)
	{ return i >= 0 && i <= buttons.length && buttons[i]; }
	
	public static boolean isOver(Box2D b)
	{ return b.isAt(x, y); }
}