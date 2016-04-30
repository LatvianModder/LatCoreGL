package latmod.core;

import latmod.core.gui.Gui;
import latmod.core.gui.GuiInit;
import latmod.core.gui.Widget;
import latmod.core.input.EventKeyPressed;
import latmod.core.input.EventKeyReleased;
import latmod.core.input.EventMousePressed;
import latmod.core.input.EventMouseReleased;
import latmod.core.input.EventMouseScrolled;
import latmod.core.input.LMInput;
import latmod.core.rendering.Font;
import latmod.core.rendering.GLHelper;
import latmod.core.rendering.Renderer;
import latmod.core.rendering.Texture;
import latmod.core.rendering.TextureManager;
import latmod.core.sound.SoundManager;
import latmod.lib.LMColorUtils;
import latmod.lib.LMUtils;
import latmod.lib.net.Response;
import latmod.lib.util.Pos2I;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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
	
	private GLFWErrorCallback errorCallback;
	private long windowID;
	
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
		run();
	}
	
	public void run()
	{
		GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		
		if(GLFW.glfwInit() != GLFW.GLFW_TRUE) throw new IllegalStateException("Unable to initialize GLFW");
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, isResizable() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		
		// Create the windowID
		windowID = GLFW.glfwCreateWindow(width, height, "Hello World!", MemoryUtil.NULL, MemoryUtil.NULL);
		if(windowID == MemoryUtil.NULL) throw new RuntimeException("Failed to create the GLFW window");
		
		LMInput.init(this);
		
		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(windowID, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		GLFW.glfwMakeContextCurrent(windowID);
		// Enable v-sync
		GLFW.glfwSwapInterval(1);
		GLFW.glfwShowWindow(windowID);
		
		GL.createCapabilities();
		onLoaded();
		GLHelper.background.setF(1F, 1F, 1F, 1F);
		
		while(GLFW.glfwWindowShouldClose(windowID) == GLFW.GLFW_FALSE)
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
			GLFW.glfwGetWindowSize(windowID, bufferW, bufferH);
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
			textureManager.tickTextures();
			
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
			GLFW.glfwDestroyWindow(windowID);
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
		{
			Texture t = new Texture(iconPaths[i]).setFlag(Texture.FLAG_STORE_PIXELS, true);
			textureManager.bind(t);
			list[i] = LatCoreGL.toByteBuffer(t.pixelBuffer.pixels, true);
		}
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
	{ if(s != null) GLFW.glfwSetWindowTitle(windowID, s); }
	
	@Override
	public final long getWindowID()
	{ return windowID; }
	
	@Override
	public final int getWidth()
	{ return width; }
	
	@Override
	public final int getHeight()
	{ return height; }
	
	@Override
	public Response getData(Resource r) throws Exception
	{ return new Response(LMUtils.class.getResourceAsStream("/assets/" + r.getBase() + '/' + r.getPath())); }
	
	@Override
	public final SoundManager getSoundManager()
	{ return soundManager; }
	
	@Override
	public final TextureManager getTextureManager()
	{ return textureManager; }
	
	@Override
	public final Font getFont()
	{ return font; }
	
	@Override
	public final Gui getGui()
	{ return gui; }
	
	@Override
	public final Gui openGui(Gui g)
	{
		Gui prevGui = gui;
		gui = g;
		gui.init();
		return prevGui;
	}
	
	@Override
	public final void destroy()
	{
		GLFW.glfwSetWindowShouldClose(windowID, GLFW.GLFW_TRUE);
	}
	
	@Override
	public void onKeyPressed(EventKeyPressed e)
	{
	}
	
	@Override
	public void onKeyReleased(EventKeyReleased e)
	{
	}
	
	@Override
	public void onMousePressed(EventMousePressed e)
	{
	}
	
	@Override
	public void onMouseReleased(EventMouseReleased e)
	{
	}
	
	@Override
	public void onMouseScrolled(EventMouseScrolled e)
	{
	}
}