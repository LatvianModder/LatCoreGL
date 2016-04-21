package latmod.core.rendering;

import latmod.core.*;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Made by LatvianModder
 */
public class EventScreenshot extends Event
{
	public final ThreadScreenshot thread;
	public final Time time;
	public BufferedImage image;
	public File file;
	
	public EventScreenshot(ThreadScreenshot t, Time ti, BufferedImage img, File f)
	{
		thread = t;
		time = ti;
		image = img;
		file = f;
	}
	
	public boolean canCancel()
	{ return true; }
}