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
	public final TextureManager texManager;
	public final Resource res;
	public PixelBuffer pixels;
	public int textureID = 0;
	public boolean exists = true;
	public boolean blured = false;
	public boolean clamp = false;
	
	public final boolean isCustom = (this instanceof TextureCustom);
	
	public Texture(TextureManager tm, Resource r, PixelBuffer pb)
	{
		texManager = tm;
		res = r;
		pixels = pb;
	}
	
	public void onLoaded() { }
	
	public int hashCode()
	{ return LMUtils.hashCode(textureID, pixels.hashCode()); }
	
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof Texture)) return false;
		return o.hashCode() == hashCode();
	}
	
	public final void bind()
	{
		GLHelper.bound_texture.set(textureID);
		texManager.currentTexture = this;
	}
	
	public final void update()
	{
		ByteBuffer buffer = LatCoreGL.toByteBuffer(pixels.pixels, true);
		bind();
		GLHelper.blured_texture.set(blured);
		GLHelper.blured_texture.onSet();
		GLHelper.warp_texture.set(!clamp);
		GLHelper.warp_texture.onSet();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, pixels.width, pixels.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	}
	
	public void destroy()
	{
		GL11.glDeleteTextures(textureID);
		texManager.textureMap.remove(res);
		textureID = 0;
	}
}