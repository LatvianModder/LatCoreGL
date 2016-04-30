package latmod.core.input;

import latmod.core.LatCoreGL;
import latmod.lib.LMUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.system.Retainable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LMInput
{
	private static List<Retainable> retainables;
	private static Map<Integer, Long> keyboardDelays;
	private static Map<Integer, Long> mouseDelays;
	
	public static double mouseX, mouseY, mouseWheel;
	public static double mouseDX, mouseDY, mouseDWheel;
	
	public static void init()
	{
		release();
		
		retainables = new ArrayList<>();
		keyboardDelays = new HashMap<>();
		mouseDelays = new HashMap<>();
		
		GLFW.glfwSetInputMode(LatCoreGL.window.getWindowID(), GLFW.GLFW_STICKY_KEYS, 1);
		Retainable r;
		
		r = new GLFWKeyCallback()
		{
			@Override
			public void invoke(long windowID, int key, int scancode, int action, int mods)
			{
				switch(action)
				{
					case GLFW.GLFW_PRESS:
					{
						LatCoreGL.window.onKeyPressed(new EventKeyPressed(LatCoreGL.window, key, 'a', false));
						keyboardDelays.put(key, LMUtils.millis());
						break;
					}
					case GLFW.GLFW_RELEASE:
					{
						LatCoreGL.window.onKeyReleased(new EventKeyReleased(LatCoreGL.window, key, LMUtils.millis() - (keyboardDelays.containsKey(key) ? keyboardDelays.get(key) : 0)));
						keyboardDelays.remove(key);
						break;
					}
					case GLFW.GLFW_REPEAT:
					{
						LatCoreGL.window.onKeyPressed(new EventKeyPressed(LatCoreGL.window, key, 'a', true));
						break;
					}
				}
			}
		};
		
		GLFW.glfwSetKeyCallback(LatCoreGL.window.getWindowID(), (GLFWKeyCallback) r);
		retainables.add(r);
		
		r = new GLFWCursorPosCallback()
		{
			@Override
			public void invoke(long windowID, double xpos, double ypos)
			{
				mouseX = xpos;
				mouseY = LatCoreGL.window.getHeight() - ypos - 1D;
			}
		};
		
		GLFW.glfwSetCursorPosCallback(LatCoreGL.window.getWindowID(), (GLFWCursorPosCallback) r);
		retainables.add(r);
		
		r = new GLFWMouseButtonCallback()
		{
			@Override
			public void invoke(long windowID, int button, int action, int mods)
			{
				switch(action)
				{
					case GLFW.GLFW_PRESS:
					{
						LatCoreGL.window.onMousePressed(new EventMousePressed(LatCoreGL.window, button));
						mouseDelays.put(button, LMUtils.millis());
						break;
					}
					case GLFW.GLFW_RELEASE:
					{
						LatCoreGL.window.onMouseReleased(new EventMouseReleased(LatCoreGL.window, button, LMUtils.millis() - mouseDelays.get(button)));
						mouseDelays.remove(button);
						break;
					}
				}
			}
		};
		
		GLFW.glfwSetMouseButtonCallback(LatCoreGL.window.getWindowID(), (GLFWMouseButtonCallback) r);
		retainables.add(r);
	}
	
	public static void setMouseGrabbed(boolean b)
	{
		GLFW.glfwSetInputMode(LatCoreGL.window.getWindowID(), GLFW.GLFW_CURSOR, b ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
	}
	
	public static boolean isMouseGrabbed()
	{
		return GLFW.glfwGetInputMode(LatCoreGL.window.getWindowID(), GLFW.GLFW_CURSOR) == GLFW.GLFW_CURSOR_DISABLED;
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
	{ return GLFW.glfwGetMouseButton(LatCoreGL.window.getWindowID(), i) == GLFW.GLFW_PRESS; }
	
	public static boolean isKeyDown(int k)
	{ return GLFW.glfwGetKey(LatCoreGL.window.getWindowID(), k) == GLFW.GLFW_PRESS; }
	
	public static boolean isCtrlDown()
	{ return isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) || isKeyDown(GLFW.GLFW_KEY_RIGHT_CONTROL); }
	
	public static boolean isShiftDown()
	{ return isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT); }
}