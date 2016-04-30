package latmod.core.rendering;

import latmod.core.EventHandler;
import latmod.core.IWindow;
import latmod.core.LatCoreGL;
import latmod.core.Time;
import latmod.lib.LMFileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

/**
 * Made by LatvianModder
 */
public final class ThreadScreenshot extends Thread
{
	public final IWindow window;
	public File outputFolder = null;
	
	public ByteBuffer pixels = null;
	public Time time = null;
	
	public ThreadScreenshot(IWindow w)
	{
		super("Screenshot");
		window = w;
		outputFolder = new File("/screenshots");
		pixels = Renderer.getScreenPixels();
	}
	
	@Override
	public void run()
	{
		int w = window.getWidth();
		int h = window.getHeight();
		
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
		
		File file = LMFileUtils.newFile(new File(outputFolder, sb.toString()));
		
		EventScreenshot event = new EventScreenshot(window, this, time, image, file);
		if(!EventHandler.MAIN.send(event))
		{
			try { ImageIO.write(event.image, "PNG", event.file); }
			catch(Exception e) { e.printStackTrace(); }
			
			LatCoreGL.logger.info("Saved screenshot " + event.file.getName());
		}
	}
}