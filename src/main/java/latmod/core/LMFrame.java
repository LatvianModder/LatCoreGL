package latmod.core;

import latmod.core.gui.*;
import latmod.core.input.*;
import latmod.core.rendering.*;
import latmod.core.sound.SoundManager;
import latmod.lib.LMColorUtils;
import latmod.lib.net.Response;
import latmod.lib.util.Pos2I;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.*;

/**
 * Made by LatvianModder
 */
public class LMFrame implements IWindow
{
	public final MainArgs mainArgs;
	public int width, height;
	private int rawFPS = 0;
	private long lastFPS;
	public int FPS;
	public long renderTick;
	
	private final GLFWErrorCallback errorCallback;
	private final long window;
	
	private SoundManager soundManager;
	private TextureManager textureManager;
	private Font font;
	private Gui gui;
	
	public LMFrame(String[] args, int w, int h) throws Exception
	{
		LatCoreGL.init();
		mainArgs = new MainArgs(args);
		width = mainArgs.getI("width", w, 200, Short.MAX_VALUE);
		height = mainArgs.getI("height", h, 150, Short.MAX_VALUE);
		
		GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		
		if(GLFW.glfwInit() != GLFW.GLFW_TRUE) throw new IllegalStateException("Unable to initialize GLFW");
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, isResizable() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		
		// Create the window
		window = GLFW.glfwCreateWindow(width, height, "Hello World!", MemoryUtil.NULL, MemoryUtil.NULL);
		if(window == MemoryUtil.NULL) throw new RuntimeException("Failed to create the GLFW window");
		
		LMInput.init(this);
		
		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		GLFW.glfwMakeContextCurrent(window);
		// Enable v-sync
		GLFW.glfwSwapInterval(1);
		GLFW.glfwShowWindow(window);
		
		run();
		
		//key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE
	}
	
	public void run()
	{
		GL.createCapabilities();
		onLoaded();
		GLHelper.background.setF(1F, 1F, 1F, 1F);
		
		while(GLFW.glfwWindowShouldClose(window) == GLFW.GLFW_FALSE)
		{
			GLHelper.clear();
			
			long millis = Time.millis();
			
			while(millis - lastFPS > 1000L)
			{
				FPS = rawFPS;
				rawFPS = 0;
				//lastFPS += 1000L;
				lastFPS = millis;
				
				onFPSUpdate();
			}
			
			rawFPS++;
			
			GLFW.glfwPollEvents();
			onUpdate();
			
			IntBuffer bufferW = BufferUtils.createIntBuffer(1);
			IntBuffer bufferH = BufferUtils.createIntBuffer(1);
			GLFW.glfwGetWindowSize(window, bufferW, bufferH);
			int w = bufferW.get(0);
			int h = bufferH.get(0);
			
			if(w != width || h != height)
			{
				int pW = width;
				int pH = height;
				
				width = w;
				height = h;
				
				GL11.glViewport(0, 0, width, height);
				Renderer.enter2D();
				
				EventGroup.DEFAULT.send(new EventResized(this, pW, pH));
				gui.init();
			}
			
			GLHelper.clear();
			GLHelper.background.setI(LMColorUtils.DARK_GRAY);
			GLHelper.color.setDefault();
			
			onRender();
			textureManager.updateCustomTextures();
			
			renderTick++;
			if(renderTick < 0) renderTick = 0;
		}
		
		LatCoreGL.logger.info("Stopping Frame...");
		
		Widget.playSound = false;
		
		try
		{
			EventGroup.DEFAULT.send(new EventDestroy());
			onDestroyed();
			LMInput.release();
			GLFW.glfwDestroyWindow(window);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	public void onUpdate()
	{
	}
	
	public void onLoaded()
	{
		LatCoreGL.window = this;
		openGui(new GuiInit());
		
		GL11.glViewport(0, 0, width, height);
		Renderer.enter2D();
		
		GLHelper.blending.enable();
		GLHelper.blendFunc.setDefault();
		
		soundManager = new SoundManager(this);
		textureManager = new TextureManager(this);
		
		lastFPS = Time.millis();
		
		GLHelper.texture.enable();
		setIcon(new Resource("core", "textures/logo_16.png"), new Resource("core", "textures/logo_32.png"), new Resource("core", "textures/logo_128.png"));
		
		font = new Font(textureManager, new Resource("core", "textures/font.png"));
	}
	
	protected boolean isResizable()
	{ return true; }
	
	public void setIcon(Resource... iconPaths)
	{
		ByteBuffer[] list = new ByteBuffer[iconPaths.length];
		for(int i = 0; i < iconPaths.length; i++)
			list[i] = LatCoreGL.toByteBuffer(textureManager.getTexture(iconPaths[i]).pixels.pixels, true);
		//Display.setIcon(list);
	}
	
	protected Pos2I startFullscreen()
	{ return null; }
	
	public void onRender()
	{
	}
	
	public void onFPSUpdate()
	{
	}
	
	public void onDestroyed()
	{
		soundManager.onDestroyed();
		textureManager.onDestroyed();
	}
	
	public void setTitle(String s)
	{ if(s != null) GLFW.glfwSetWindowTitle(window, s); }
	
	public final long getWindowID()
	{ return window; }
	
	public final int getWidth()
	{ return width; }
	
	public final int getHeight()
	{ return height; }
	
	public Response getData(Resource r) throws Exception
	{ return new Response(Thread.currentThread().getContextClassLoader().getResourceAsStream("/assets/" + r.getBase() + '/' + r.getPath())); }
	
	public final SoundManager getSoundManager()
	{ return soundManager; }
	
	public final TextureManager getTextureManager()
	{ return textureManager; }
	
	public final Font getFont()
	{ return font; }
	
	public final Gui getGui()
	{ return gui; }
	
	public final Gui openGui(Gui g)
	{
		Gui prevGui = gui;
		gui = g;
		gui.init();
		return prevGui;
	}
	
	public final void destroy()
	{
		GLFW.glfwSetWindowShouldClose(window, GLFW.GLFW_TRUE);
	}
	
	public void onKeyPressed(EventKeyPressed e)
	{
	}
	
	public void onKeyReleased(EventKeyReleased e)
	{
	}
	
	public void onMousePressed(EventMousePressed e)
	{
	}
	
	public void onMouseReleased(EventMouseReleased e)
	{
	}
	
	public void onMouseScrolled(EventMouseScrolled e)
	{
	}
}