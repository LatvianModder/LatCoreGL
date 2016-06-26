package com.latmod.latcoregl;

import com.latmod.lib.util.LMColorUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Made by LatvianModder
 */
public final class LatCoreGL
{
    public static final Logger logger = Logger.getLogger("LatCoreGL");
    private static final Logger systemOutLogger = Logger.getLogger("System.out");

    public static int screenWidth, screenHeight;
    public static IWindow window;
    public static boolean enableLogging = false;
    private static FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(4);

    public static void init()
    {
        if(logger.getUseParentHandlers())
        {
            logger.setUseParentHandlers(false);

            logger.addHandler(new Handler()
            {
                @Override
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
                    {
                        System.out.println(sb.toString());
                    }
                    else
                    {
                        System.err.println(sb.toString());
                    }

                    if(enableLogging)
                    {
                        StringBuilder sb1 = new StringBuilder();
                        sb1.append('[');
                        sb1.append(record.getLevel());
                        sb1.append(']');
                        sb1.append('[');
                        sb1.append(sb);
                    }
                }

                @Override
                public void flush()
                {
                }

                @Override
                public void close() throws SecurityException
                {
                }
            });

            systemOutLogger.setParent(logger);
            
			/*
            System.setOut(new PrintStream(System.out) { public void println(String s) { super.println(s);
			if(enableLogging) log.add("[" + Time.getTimeString() + "][System.out]: " + s); } });
			
			System.setErr(new PrintStream(System.err) { public void println(String s) { super.println(s);
			if(enableLogging) log.add("[" + Time.getTimeString() + "][System.out]: " + s); } });
			*/
        }

        logger.info("LWJGL " + Version.getVersion());
    }

    public static FloatBuffer floatBuffer(float a, float b, float c, float d)
    {
        floatBuffer.clear();
        floatBuffer.put(new float[] {a, b, c, d});
        floatBuffer.flip();
        return floatBuffer;
    }

    public static ByteBuffer toByteBuffer(int pixels[], boolean alpha)
    {
        ByteBuffer b = BufferUtils.createByteBuffer(pixels.length * 4);

        for(int i = 0; i < pixels.length; i++)
        {
            int c = pixels[i];
            b.put((byte) LMColorUtils.getRed(c));
            b.put((byte) LMColorUtils.getGreen(c));
            b.put((byte) LMColorUtils.getBlue(c));
            b.put(alpha ? (byte) LMColorUtils.getAlpha(c) : (byte) 255);
        }

        b.flip();
        return b;
    }

    // End of class //
}