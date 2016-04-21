package latmod.core;

import latmod.core.gui.*;
import latmod.core.input.*;
import latmod.core.rendering.*;
import latmod.core.sound.SoundManager;
import latmod.lib.LMColorUtils;
import latmod.lib.net.Response;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

/**
 * Made by LatvianModder
 */
public class LMFrame implements Runnable, IWindow
{
	public final MainArgs mainArgs;
	public int width, height;
	public Thread renderThread;
	private int rawFPS = 0;
	private long lastFPS;
	public int FPS;
	public long renderTick;
	private boolean shouldDestroy = false;
	
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
		
		renderThread = new Thread(this, "LMFrame");
		renderThread.start();
	}
	
	public void onUpdate() throws Exception
	{
	}
	
	public void onLoaded() throws Exception
	{
		LatCoreGL.window = this;
		openGui(new GuiInit());
		
		Display.setDisplayMode(new DisplayMode(width, height));
		setTitle("LMFrame");
		Display.setResizable(isResizable());
		Display.create();
		Renderer.init(width, height);
		Renderer.enter2D();
		
		GLHelper.blending.enable();
		GLHelper.blendFunc.setDefault();
		
		soundManager = new SoundManager(this);
		textureManager = new TextureManager(this);
		
		lastFPS = Time.millis();
		
		GLHelper.texture.enable();
		setIcon("gui/logo_16.png", "gui/logo_32.png", "gui/logo_128.png");
		
		font = new Font(textureManager, Resource.getTexture("font.png"));
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
			onLoaded();
			
			while(renderThread != null && !shouldDestroy)
			{
				if(Display.isCloseRequested()) destroy();
				
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
				
				LMMouse.update();
				LMKeyboard.update();
				onUpdate();
				
				if(Display.wasResized())
				{
					int w = Display.getWidth();
					int h = Display.getHeight();
					
					if(w != width || h != height)
					{
						int pW = width;
						int pH = height;
						
						width = w;
						height = h;
						Renderer.init(width, height);
						
						EventGroup.DEFAULT.send(new EventResized(this, pW, pH));
					}
					
					gui.init();
				}
				
				GLHelper.clear();
				GLHelper.background.setI(LMColorUtils.DARK_GRAY);
				GLHelper.color.setDefault();
				
				onRender();
				textureManager.updateCustomTextures();
				
				Display.update();
				Display.sync(60);
				
				renderTick++;
				if(renderTick < 0) renderTick = 0;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		LatCoreGL.logger.info("Stopping Frame...");
		
		Widget.playSound = false;
		
		try
		{
			EventGroup.DEFAULT.send(new EventDestroy());
			onDestroyed();
			LMMouse.destroy();
			LMKeyboard.destroy();
			Display.destroy();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		renderThread = null;
		System.exit(0);
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
	
	public void setTitle(String s)
	{ if(s != null) Display.setTitle(s); }
	
	public final int getWidth()
	{ return width; }
	
	public final int getHeight()
	{ return height; }
	
	public Response getData(Resource r) throws Exception
	{ return new Response(LMFrame.class.getResourceAsStream(r.getID())); }
	
	public final SoundManager getSoundManager()
	{ return soundManager; }
	
	public final TextureManager getTextureManager()
	{ return textureManager; }
	
	public final Font getFont()
	{ return font; }
	
	public final Gui getGui()
	{ return gui; }
	
	public final void openGui(Gui g)
	{
		gui = g;
	}
	
	public final void destroy()
	{ shouldDestroy = true; }
	
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