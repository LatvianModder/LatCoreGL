package latmod.core.rendering;

import latmod.core.res.Resource;
import latmod.lib.PixelBuffer;

import java.util.Random;

/** Made by LatvianModder */
public class TextureCustom extends Texture
{
	public final boolean updatePixels;
	public Random rand = null;
	public int tick = 0;
	
	public TextureCustom(TextureManager tm, Resource r, PixelBuffer pb, boolean update)
	{
		super(tm, r, pb);
		updatePixels = update;
		pixels.fill(0xFFFFFFFF);
		rand = new Random(textureID);
	}
	
	public void onLoaded()
	{
	}
	
	public void onUpdate()
	{
	}
}