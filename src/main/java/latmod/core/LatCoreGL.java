package latmod.core;

import latmod.core.gui.Widget;
import latmod.core.input.*;
import latmod.core.res.FileResManager;
import latmod.lib.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.nio.*;
import java.util.logging.*;

/** Made by LatvianModder */
public final class LatCoreGL
{
	public static final Logger logger = Logger.getLogger("LatCore");
	private static final Logger systemOutLogger = Logger.getLogger("System.out");
	static { initLogger(); }
	
	public static LMFrame mainFrame;
	
	public static FileResManager projectResManager = null;
	
	public static boolean enableLogging = true;
	private static final FastList<String> log = new FastList<>();
	
	public static void initLogger()
	{
		if(logger.getUseParentHandlers())
		{
			logger.setUseParentHandlers(false);
			
			logger.addHandler(new Handler()
			{
				public void publish(LogRecord record)
				{
					StringBuilder sb = new StringBuilder();
					
					sb.append('[');
					sb.append(Time.get().getTimeString());
					sb.append(']');
					sb.append('[');
					sb.append(record.getLoggerName());
					sb.append("]: ");
					sb.append(record.getMessage());
					
					if(record.getLevel().intValue() <= Level.INFO.intValue())
					System.out.println(sb.toString()); else System.err.println(sb.toString());
					
					if(enableLogging)
					{
						StringBuilder sb1 = new StringBuilder();
						sb1.append('[');
						sb1.append(record.getLevel());
						sb1.append(']');
						sb1.append('[');
						sb1.append(sb);
						log.add(sb1.toString());
					}
				}

				public void flush()
				{
				}

				public void close() throws SecurityException
				{
				}
			});
			
			logger.info("Logger inited!");
			
			systemOutLogger.setParent(logger);
			
			/*
			System.setOut(new PrintStream(System.out) { public void println(String s) { super.println(s);
			if(enableLogging) log.add("[" + Time.getTimeString() + "][System.out]: " + s); } });
			
			System.setErr(new PrintStream(System.err) { public void println(String s) { super.println(s);
			if(enableLogging) log.add("[" + Time.getTimeString() + "][System.out]: " + s); } });
			*/
		}
	}
	
	public static void stop()
	{
		logger.info("Stopping Frame...");
		
		Widget.playSound = false;
		
		try
		{
			EventGroup.DESTROY.send(new EventDestroy());
			EventGroup.DEFAULT.send(new EventSaveLog(log));
			mainFrame.onDestroyed();
			LMMouse.destroy();
			LMKeyboard.destroy();
			Display.destroy();
		}
		catch(Exception e)
		{ e.printStackTrace(); }
		
		mainFrame.renderThread = null;
		System.exit(0);
	}
	
	public static int getWidth()
	{ return mainFrame.width; }
	
	public static int getHeight()
	{ return mainFrame.height; }
	
	private static FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(4);
	public static FloatBuffer floatBuffer(float a, float b, float c, float d)
	{ floatBuffer.clear(); floatBuffer.put(new float[] {a, b, c, d}); floatBuffer.flip(); return floatBuffer; }
	
	public static File file(String s)
	{
		if(projectResManager != null)
		return new File(projectResManager.baseDirectory, s);
		return new File(s);
	}
	
	public static File newFile(String s)
	{ return LMFileUtils.newFile(file(s)); }
	
	public static File newFile(File f0, String s)
	{ return LMFileUtils.newFile(file(f0.getAbsolutePath() + '/' + s)); }
	
	public static void setProjectLocation(File f)
	{
		projectResManager = new FileResManager(f);
		logger.info("Set project directory to " + f.getAbsolutePath());
	}
	
	public static ByteBuffer toByteBuffer(int pixels[], boolean alpha)
	{
		ByteBuffer b = BufferUtils.createByteBuffer(pixels.length * 4);
		
		byte a = (byte)255;
		
		for(int i = 0; i < pixels.length; i++)
		{
			int c = pixels[i];
			b.put((byte)LMColorUtils.getRed(c));
			b.put((byte)LMColorUtils.getGreen(c));
			b.put((byte)LMColorUtils.getBlue(c));
			b.put(alpha ? (byte)LMColorUtils.getAlpha(c) : a);
		}
		
		b.flip();
		return b;
	}

	// End of class //
}