package latmod.core.rendering;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.Display;

import latmod.core.*;

/** Made by LatvianModder */
public final class ThreadScreenshot extends Thread
{
	public static File outputFolder = null;
	
	public ByteBuffer pixels = null;
	public Time time = null;
	
	public ThreadScreenshot()
	{ super("Screenshot"); pixels = Renderer.getScreenPixels(); }
	
	public void run()
	{
		int w = Display.getWidth();
		int h = Display.getHeight();
		
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		
		for(int y = 0; y < h; y++)
		for(int x = 0; x < w; x++)
		{
			int i = (x + (w * y)) * 4;
			int r = pixels.get(i) & 255;
		 	int g = pixels.get(i + 1) & 255;
		 	int b = pixels.get(i + 2) & 255;
		 	image.setRGB(x, h - (y + 1), (255 << 24) | (r << 16) | (g << 8) | b);
		}
		
		Time time = Time.get();
		
		StringBuilder sb = new StringBuilder();
		sb.append(time.getDateStringInv());
		sb.append(", ");
		sb.append(time.getTimeString().replace(':', '.'));
		sb.append(".png");
		
		File file = LatCoreGL.newFile("screenshots/" + sb.toString());
		
		if(!EventGroup.DEFAULT.send(new EventScreenshot(this, time, image, file)))
		{
			try  { ImageIO.write(image, "PNG", file); }
			catch (Exception e) { e.printStackTrace(); }
			
			Renderer.logger.info("Saved screenshot " + file.getName());
		}
	}
}