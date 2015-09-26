package latmod.core.rendering;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.*;

import latmod.core.LatCoreGL;
import latmod.core.res.Resource;
import latmod.core.util.*;

/** Made by LatvianModder */
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
	{ if(o == null || !(o instanceof Texture)) return false;
	return o.hashCode() == hashCode(); }
	
	public final void update()
	{
		ByteBuffer buffer = LatCoreGL.toByteBuffer(pixels.pixels, true);
		Renderer.bind(textureID);
		int i = blured ? GL11.GL_LINEAR : GL11.GL_NEAREST;
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, i);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, i);
		i = clamp ? GL12.GL_CLAMP_TO_EDGE : GL11.GL_REPEAT;
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, i);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, i);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, pixels.width, pixels.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	}
	
	public void destroy()
	{
		GL11.glDeleteTextures(textureID);
		texManager.textureMap.remove(res);
		textureID = 0;
	}
}