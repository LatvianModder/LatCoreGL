package latmod.core;
import java.io.*;
import java.net.*;
import java.nio.FloatBuffer;
import java.util.logging.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

import latmod.core.gui.Widget;
import latmod.core.input.*;
import latmod.core.res.FileResManager;
import latmod.core.util.*;

/** Made by LatvianModder */
public final class LatCoreGL
{
	public static final Logger logger = Logger.getLogger("LatCore");
	private static final Logger systemOutLogger = Logger.getLogger("System.out");
	static { initLogger(); }
	
	public static LMFrame mainFrame;
	
	public static FileResManager projectResManager = null;
	
	public static boolean enableLogging = true;
	private static FastList<String> log = new FastList<String>();
	
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
					sb.append("][");
					sb.append(record.getLoggerName());
					sb.append("]: ");
					sb.append(record.getMessage());
					
					String s = sb.toString();
					
					if(record.getLevel().intValue() <= Level.INFO.intValue())
					System.out.println(s); else System.err.println(s);
					
					if(enableLogging) log.add("[" + record.getLevel() + "][" + s);
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
	
	public static String getHostAddress()
	{
		try { return InetAddress.getLocalHost().getHostAddress(); }
		catch(Exception e) { } return null;
	}
	
	public static String getExternalAddress()
	{
		try
		{
			URL url = new URL("http://checkip.amazonaws.com");
			InputStream is = url.openStream();
			byte[] b = new byte[is.available()];
			is.read(b);
			return new String(b).trim();
		}
		catch(Exception e) { } return null;
	}

	// End of class //
}