package latmod.core;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.lwjgl.BufferUtils;

import latmod.core.util.LMColorUtils;

public class PixelBuffer
{
	public final int width, height;
	public final int[] pixels;
	
	public PixelBuffer(int w, int h)
	{
		width = w;
		height = h;
		pixels = new int[w * h];
	}
	
	public PixelBuffer(BufferedImage img)
	{
		this(img.getWidth(), img.getHeight());
		img.getRGB(0, 0, width, height, pixels, 0, width);
	}
	
	public void setPixels(int[] rgbArray)
	{
		if(rgbArray.length == pixels.length)
			System.arraycopy(rgbArray, 0, pixels, 0, pixels.length);
	}
	
	public void setRGB(int x, int y, int col)
	{ pixels[x + y * width] = col; }
	
	public int getRGB(int x, int y)
	{ return pixels[x + y * width]; }
	
	public int[] getRGB(int startX, int startY, int w, int h)
	{
		int ai[] = new int[w * h];
		int idx = -1;
		for(int y = startY; y < startY + h; y++)
		for(int x = startX; x < startX + w; x++)
			ai[++idx] = getRGB(x, y);
		return ai;
	}
	
	public void setRGB(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize)
	{
		int yoff = offset;
		int off;
		
		for(int y = startY; y < startY + h; y++, yoff += scansize)
		{
			off = yoff;
			
			for(int x = startX; x < startX + w; x++)
				setRGB(x, y, rgbArray[off++]);
		}
	}
	
	public void setRGB(int startX, int startY, PixelBuffer buffer)
	{ setRGB(startX, startY, buffer.width, buffer.height, buffer.pixels, 0, buffer.width); }
	
	public BufferedImage toImage(int type)
	{
		BufferedImage image = new BufferedImage(width, height, type);
		image.setRGB(0, 0, width, height, pixels, 0, width);
		return image;
	}
	
	public void fill(int col)
	{ Arrays.fill(pixels, col); }
	
	public void fill(int startX, int startY, int w, int h, int col)
	{
		for(int y = startY; y < startY + h; y++)
		for(int x = startX; x < startX + w; x++)
			setRGB(x, y, col);
	}

	public ByteBuffer toByteBuffer(boolean alpha)
	{
		ByteBuffer bb = BufferUtils.createByteBuffer(width * height * 4);
		byte alpha255 = (byte)255;
		
		for(int i = 0; i < width * height; i++)
		{
			bb.put((byte)LMColorUtils.getRed(pixels[i]));
			bb.put((byte)LMColorUtils.getGreen(pixels[i]));
			bb.put((byte)LMColorUtils.getBlue(pixels[i]));
			bb.put(alpha ? (byte)LMColorUtils.getAlpha(pixels[i]) : alpha255);
		}
		
		bb.flip();
		return bb;
	}
	
	public PixelBuffer clone()
	{
		PixelBuffer pb = new PixelBuffer(width, height);
		System.arraycopy(pixels, 0, pb.pixels, 0, pixels.length);
		return pb;
	}
}