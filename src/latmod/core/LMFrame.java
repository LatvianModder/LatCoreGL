package latmod.core;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.*;

import latmod.core.input.*;
import latmod.core.rendering.*;
import latmod.core.res.*;
import latmod.core.sound.SoundManager;
import latmod.core.util.LMColorUtils;

/** Made by LatvianModder */
public class LMFrame implements IInputEvents, Runnable
{
	public final MainArgs mainArgs;
	public final InputHandler inputHandler;
	public int width, height, screenWidth, screenHeight;
	public Thread renderThread;
	private int rawFPS = 0;
	private long lastFPS;
	public int FPS;
	public long renderTick;
	public boolean hasInited = false;
	private boolean shouldDestroy = false;
	
	public ResourceManager resManager;
	public SoundManager soundManager;
	public TextureManager textureManager;
	public Font font;
	
	public LMFrame(String[] args, int w, int h) throws Exception
	{
		mainArgs = new MainArgs(args);
		inputHandler = new InputHandler();
		LatCoreGL.mainFrame = this;
        
		width = mainArgs.getI("-width", w, 0, 16000);
		height = mainArgs.getI("-height", h, 0, 9000);
		
		DisplayMode d = Display.getDesktopDisplayMode();
		screenWidth = d.getWidth();
		screenHeight = d.getHeight();
		renderThread = new Thread(this, "LMFrame");
		renderThread.start();
	}
	
	public void onUpdate() throws Exception
	{
	}
	
	public void onLoaded() throws Exception
	{
		Display.setDisplayMode(new DisplayMode(width, height));
		setTitle("LMFrame");
		Display.setResizable(isResizable());
		Display.create();
		Renderer.init(width, height);
		Renderer.enter2D();
		Renderer.enableTexture();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Renderer.enableAlphaBlending();
		
		resManager = new StreamResManager();
		soundManager = new SoundManager(resManager);
		textureManager = new TextureManager(resManager);
		
		lastFPS = Time.millis();
		
		Renderer.enableTexture();
		setIcon("gui/logo_16.png", "gui/logo_32.png", "gui/logo_128.png");
		
		font = new Font(textureManager, Resource.getTexture("font.png"));
		
		hasInited = true;
		inputHandler.add(this);
	}
	
	protected boolean isResizable()
	{ return true; }
	
	public void setIcon(String... iconPaths)
	{
		ByteBuffer[] list = new ByteBuffer[iconPaths.length];
		for(int i = 0; i < iconPaths.length; i++)
		list[i] = LatCoreGL.toByteBuffer(textureManager.getTexture(Resource.getTexture(iconPaths[i])).pixels.pixels, true);
		Display.setIcon(list);
	}
	
	public void run()
	{
		try
		{
			LatCoreGL.mainFrame = this;
			onLoaded();
			
			while(renderThread != null)
			{
				if(Display.isCloseRequested()) destroy();
				
				long millis = Time.millis();
				
				while (millis - lastFPS > 1000L)
				{
					FPS = rawFPS;
					rawFPS = 0;
					//lastFPS += 1000L;
					lastFPS = millis;
					
					onFPSUpdate();
				}
				
				rawFPS++;
				
				LMMouse.update();
				LMKeyboard.update();
				onUpdate();
				
				if(Display.wasResized()) onResized();
				
				Renderer.clear();
				Renderer.background(LMColorUtils.DARK_GRAY);
				GL11.glColor4f(1F, 1F, 1F, 1F);
				
				onRender();
				textureManager.updateCustomTextures();
				
				Display.update();
				Display.sync(60);
				
				renderTick++;
				if(renderTick < 0) renderTick = 0;
				
				if(shouldDestroy) LatCoreGL.stop();
				//Thread.sleep(1);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LatCoreGL.stop();
		}
	}
	
	protected boolean canFullscreen()
	{ return true; }
	
	public void onRender() throws Exception
	{
	}
	
	public void onFPSUpdate()
	{
	}
	
	public void onDestroyed() throws Exception
	{
		soundManager.onDestroyed();
		textureManager.onDestroyed();
	}
	
	public void setPosition(int x, int y)
	{ Display.setLocation(x, y); }
	
	public void setTitle(String s)
	{ if(s != null) Display.setTitle(s); }
	
	public void onResized()
	{
		int w = Display.getWidth();
		int h = Display.getHeight();
		
		if(w != width || h != height)
		{
			int pW = width;
			int pH = height;
			
			width = w; height = h;
			Renderer.init(width, height);
			
			EventGroup.DEFAULT.send(new EventResized(this, pW, pH));
		}
	}

	public static final boolean isFocused()
	{ return Display.isActive(); }
	
	public static final boolean isVisible()
	{ return Display.isVisible(); }
	
	public final void destroy()
	{ shouldDestroy = true; }
}