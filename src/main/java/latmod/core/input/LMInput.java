package latmod.core.input;

import latmod.core.*;
import latmod.lib.LMUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.system.Retainable;

import java.util.*;

public class LMInput
{
	private static IWindow window;
	private static List<Retainable> retainables;
	private static Map<Integer, Long> keyboardDelays;
	private static Map<Integer, Long> mouseDelays;
	
	public static double mouseX, mouseY, mouseWheel;
	public static double mouseDX, mouseDY, mouseDWheel;
	
	public static void init(IWindow w)
	{
		release();
		
		window = w;
		retainables = new ArrayList<>();
		keyboardDelays = new HashMap<>();
		mouseDelays = new HashMap<>();
		
		GLFW.glfwSetInputMode(window.getWindowID(), GLFW.GLFW_STICKY_KEYS, 1);
		Retainable r;
		
		r = new GLFWKeyCallback()
		{
			public void invoke(long windowID, int key, int scancode, int action, int mods)
			{
				switch(action)
				{
					case GLFW.GLFW_PRESS:
					{
						window.onKeyPressed(new EventKeyPressed(window, key, 'a', false));
						keyboardDelays.put(key, LMUtils.millis());
						break;
					}
					case GLFW.GLFW_RELEASE:
					{
						window.onKeyReleased(new EventKeyReleased(window, key, LMUtils.millis() - keyboardDelays.get(key)));
						keyboardDelays.remove(key);
						break;
					}
					case GLFW.GLFW_REPEAT:
					{
						window.onKeyPressed(new EventKeyPressed(window, key, 'a', true));
						break;
					}
				}
			}
		};
		
		GLFW.glfwSetKeyCallback(window.getWindowID(), (GLFWKeyCallback) r);
		retainables.add(r);
		
		r = new GLFWCursorPosCallback()
		{
			public void invoke(long windowID, double xpos, double ypos)
			{
				mouseX = xpos;
				mouseY = LatCoreGL.window.getHeight() - ypos - 1D;
			}
		};
		
		GLFW.glfwSetCursorPosCallback(window.getWindowID(), (GLFWCursorPosCallback) r);
		retainables.add(r);
		
		r = new GLFWMouseButtonCallback()
		{
			public void invoke(long windowID, int button, int action, int mods)
			{
				switch(action)
				{
					case GLFW.GLFW_PRESS:
					{
						window.onMousePressed(new EventMousePressed(window, button));
						mouseDelays.put(button, LMUtils.millis());
						break;
					}
					case GLFW.GLFW_RELEASE:
					{
						window.onMouseReleased(new EventMouseReleased(window, button, LMUtils.millis() - mouseDelays.get(button)));
						mouseDelays.remove(button);
						break;
					}
				}
			}
		};
		
		GLFW.glfwSetMouseButtonCallback(window.getWindowID(), (GLFWMouseButtonCallback) r);
		retainables.add(r);
	}
	
	public static void setMouseGrabbed(boolean b)
	{
		GLFW.glfwSetInputMode(window.getWindowID(), GLFW.GLFW_CURSOR, b ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
	}
	
	public static boolean isMouseGrabbed()
	{
		return GLFW.glfwGetInputMode(window.getWindowID(), GLFW.GLFW_CURSOR) == GLFW.GLFW_CURSOR_DISABLED;
	}
	
	public static void release()
	{
		if(retainables == null) return;
		
		for(Retainable r : retainables)
		{
			r.release();
		}
		
		retainables.clear();
		retainables = null;
	}
	
	public static boolean isMouseDown(int i)
	{ return GLFW.glfwGetMouseButton(window.getWindowID(), i) == GLFW.GLFW_PRESS; }
	
	public static boolean isKeyDown(int k)
	{ return GLFW.glfwGetKey(window.getWindowID(), k) == GLFW.GLFW_PRESS; }
	
	public static boolean isCtrlDown()
	{ return isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) || isKeyDown(GLFW.GLFW_KEY_RIGHT_CONTROL); }
	
	public static boolean isShiftDown()
	{ return isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT); }
}