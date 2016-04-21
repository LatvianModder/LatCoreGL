package latmod.core.rendering;

import latmod.core.*;
import latmod.lib.PixelBuffer;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.util.*;
import java.util.logging.Logger;

public class TextureManager
{
	public static final Logger logger = Logger.getLogger("TextureManager");
	
	static { logger.setParent(LatCoreGL.logger); }
	
	public static TextureManager instance = null;
	
	public final IWindow window;
	public final Map<Resource, Texture> textureMap;
	public final List<TextureCustom> updateableCustomTextures;
	public boolean loadTexturesBlured = false;
	public Texture currentTexture = null;
	private static PixelBuffer unknownTexture = createUnknownTexture();
	public long animTick = 0;
	private final Texture nullTexture;
	
	public TextureManager(IWindow w)
	{
		window = w;
		textureMap = new HashMap<>();
		updateableCustomTextures = new ArrayList<>();
		nullTexture = new Texture(this, Resource.NULL, createUnknownTexture());
		nullTexture.textureID = 0;
	}
	
	private static PixelBuffer createUnknownTexture()
	{
		PixelBuffer pixels = new PixelBuffer(4, 4);
		pixels.setPixels(new int[] {0xFF000000, 0xFFFF00FF, 0xFF000000, 0xFFFF00FF, 0xFFFF00FF, 0xFFFF0000, 0xFFFFD800, 0xFF000000, 0xFF000000, 0xFF0094FF, 0xFF00FF21, 0xFFFF00FF, 0xFFFF00FF, 0xFF000000, 0xFFFF00FF, 0xFF000000});
		return pixels;
	}
	
	private Texture getTextureFromList(Resource r)
	{
		if(r == null || textureMap.isEmpty()) return null;
		return textureMap.get(r);
	}
	
	/**
	 * Returns texture, if texture isn't loaded, then it is loaded
	 */
	public Texture getTexture(Resource r)
	{
		Texture t1 = getTextureFromList(r);
		if(t1 != null) return t1;
		Texture t = setupTexture(r, loadImage(r));
		return (t == null) ? nullTexture : t;
	}
	
	/**
	 * Sends an update for all custom textures<br>
	 * (They won't be able to move without calling this
	 * <br> method before drawing them)
	 */
	public void updateCustomTextures()
	{
		GLHelper.texture.enable();
		
		for(int i = 0; i < updateableCustomTextures.size(); i++)
		{
			TextureCustom tc = updateableCustomTextures.get(i);
			tc.onUpdate();
			tc.tick++;
			tc.update();
		}
		
		animTick++;
	}
	
	private Texture setupTexture(Resource r, PixelBuffer image)
	{
		if(r == null) return null;
		
		GLHelper.texture.enable();
		
		boolean failed = false;
		if(image == null) failed = true;
		if(failed) image = unknownTexture.copy();
		Texture t = new Texture(this, r, image);
		t.textureID = GL11.glGenTextures();
		t.blured = loadTexturesBlured;
		t.update();
		GLHelper.bound_texture.set(0);
		
		logger.info("Added texture " + r + " with id " + t.textureID + (failed ? " (Unknown)" : ""));
		
		t.exists = !failed;
		t.onLoaded();
		textureMap.put(r, t);
		
		EventGroup.DEFAULT.send(new EventTextureLoaded(t, !failed, loadTexturesBlured));
		return t;
	}
	
	/**
	 * Registers and setups a custom texture
	 */
	public void addCustomTexture(TextureCustom tc)
	{
		if(tc == null)
		{
			logger.warning("Custom texture can't be null!");
			return;
		}
		tc.textureID = GL11.glGenTextures();
		
		if(tc.res == null) logger.info("Added texture with id " + tc.textureID + " (Subtexture)");
		else logger.info("Added " + tc.res + " with id " + tc.textureID + " (Custom)");
		
		tc.rand = new Random(tc.textureID);
		tc.onLoaded();
		tc.update();
		GLHelper.bound_texture.set(0);
		
		if(tc.res != null)
		{
			if(tc.updatePixels) updateableCustomTextures.add(tc);
			textureMap.put(tc.res, tc);
		}
	}
	
	public PixelBuffer loadImage(Resource r)
	{
		try { return new PixelBuffer(ImageIO.read(window.getData(r).stream)); }
		catch(Exception e) { logger.warning("Can't load image '" + r + "'!"); }
		return null;
	}
	
	public void onDestroyed()
	{
		for(Texture t : textureMap.values())
			GL11.glDeleteTextures(t.textureID);
	}
}