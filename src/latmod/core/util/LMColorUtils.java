package latmod.core.util;

import org.lwjgl.opengl.GL11;

import latmod.core.MathHelperLM;

public class LMColorUtils
{
	public static final int[] chatFormattingColors = new int[16];
	private static final float[] staticHSB = new float[3];
	
	static
	{
		for(int i = 0; i < 16; i++)
        {
            int j = (i >> 3 & 1) * 85;
            int r = (i >> 2 & 1) * 170 + j;
            int g = (i >> 1 & 1) * 170 + j;
            int b = (i >> 0 & 1) * 170 + j;
            if(i == 6) r += 85;
            chatFormattingColors[i] = getRGBA(r, g, b, 255);
        }
	}
	
	public static final int DARK_GRAY = 30;
	public static final int GRAY = 100;
	public static final int LIGHT_GRAY = 200;
	public static final int WIDGETS = 119;
	
	public static int getRGBA(int r, int g, int b, int a)
	{ return ((a & 255) << 24) | ((r & 255) << 16) | ((g & 255) << 8) | ((b & 255) << 0); }
	
	public static int getRed(int c)
	{ return (c >> 16) & 255; }
	
	public static int getGreen(int c)
	{ return (c >> 8) & 255; }
	
	public static int getBlue(int c)
	{ return (c >> 0) & 255; }
	
	public static int getAlpha(int c)
	{ return (c >> 24) & 255; }
	
	public static String getHex(int c)
	{ return "#" + Integer.toHexString(getRGBA(c, 255)).substring(2).toUpperCase(); }
	
	public static int getRGBA(int c, int a)
	{ return getRGBA(getRed(c), getGreen(c), getBlue(c), a); }
	
	public static void setGLColor(int c, int a)
	{
		if(c >= 0 && c <= 255)
			GL11.glColor4f(c / 255F, c / 255F, c / 255F, a);
		else
			GL11.glColor4f(getRed(c) / 255F, getGreen(c) / 255F, getBlue(c) / 255F, a / 255F);
	}
	
	public static void setGLColor(int c)
	{ setGLColor(c, getAlpha(c)); }
	
	public static int getHSB(float h, float s, float b)
	{ return java.awt.Color.HSBtoRGB(h, s, b); }
	
	public static void setHSB(int r, int g, int b)
	{ java.awt.Color.RGBtoHSB(r, g, b, staticHSB); }
	
	public static void setHSB(int c)
	{ setHSB(getRed(c), getGreen(c), getBlue(c)); }
	
	public static float getHSBHue()
	{ return staticHSB[0]; }
	
	public static float getHSBSaturation()
	{ return staticHSB[1]; }
	
	public static float getHSBBrightness()
	{ return staticHSB[2]; }
	
	public static int addBrightness(int c, int b)
	{
		int red = MathHelperLM.clampInt(getRed(c) + b, 0, 255);
		int green = MathHelperLM.clampInt(getGreen(c) + b, 0, 255);
		int blue = MathHelperLM.clampInt(getBlue(c) + b, 0, 255);
		return getRGBA(red, green, blue, getAlpha(c));
	}

	public static int lerp(int col0, int col1, double d, int a)
	{
		return col0;
	}
	
	public static int lerp(int col0, int col1, double d)
	{ return lerp(col0, col1, d, getAlpha(col0)); }
}