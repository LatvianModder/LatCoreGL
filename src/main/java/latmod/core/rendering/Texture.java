package latmod.core.rendering;

import latmod.core.*;
import latmod.lib.*;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

/**
 * Made by LatvianModder
 */
public class Texture
{
	public static final byte FLAG_UNKNOWN = 0;
	public static final byte FLAG_BLUR = 1;
	public static final byte FLAG_WARP = 2;
	public static final byte FLAG_CUSTOM = 3;
	public static final byte FLAG_TICK = 4;
	public static final byte FLAG_STORE_PIXELS = 5;
	
	public final Resource res;
	public int textureID = 0;
	public byte flags = 0;
	public PixelBuffer pixelBuffer;
	
	public Texture(Resource r)
	{
		if(r == null) throw new NullPointerException("Texture ID can't be null!");
		res = r;
		
		setFlag(FLAG_UNKNOWN, false);
		setFlag(FLAG_BLUR, false);
		setFlag(FLAG_WARP, true);
		setFlag(FLAG_CUSTOM, false);
		setFlag(FLAG_TICK, false);
		setFlag(FLAG_STORE_PIXELS, false);
	}
	
	public Texture setFlag(byte flag, boolean b)
	{
		flags = Bits.setBit(flags, flag, b);
		return this;
	}
	
	public boolean getFlag(byte flag)
	{ return Bits.getBit(flags, flag); }
	
	public final int hashCode()
	{ return res.hashCode(); }
	
	public final boolean equals(Object o)
	{
		if(o == null || !(o instanceof Texture)) return false;
		return o.hashCode() == hashCode();
	}
	
	public final String toString()
	{ return res.toString(); }
	
	public void onLoaded(TextureManager tm)
	{
	}
	
	public void onUpdate(TextureManager tm)
	{
	}
	
	public final void uploadPixelBuffer(PixelBuffer pixels)
	{
		if(pixels == null || textureID <= 0) return;
		ByteBuffer buffer = LatCoreGL.toByteBuffer(pixels.pixels, true);
		GLHelper.bound_texture.set(textureID);
		GLHelper.blured_texture.setN(getFlag(FLAG_BLUR));
		GLHelper.blured_texture.onSet();
		GLHelper.warp_texture.setN(getFlag(FLAG_WARP));
		GLHelper.warp_texture.onSet();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, pixels.width, pixels.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		GLHelper.bound_texture.set(0);
	}
	
	public void delete()
	{
		GL11.glDeleteTextures(textureID);
		textureID = 0;
	}
}