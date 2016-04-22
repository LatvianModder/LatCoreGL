package latmod.core.rendering;

import latmod.core.Resource;
import latmod.lib.*;

/**
 * <b>Made by LatvianModder</b> <br>
 * Character length code taken from Minecraft:FontRenderer
 */
public class Font
{
	public final TextureManager texManager;
	public final Texture texture;
	public final double[] charWidth = new double[256];
	private boolean drawAtributes = true;
	private float masterAlpha = 1F;
	private double masterScale = 1D;
	private double shadowSize = 0D;
	private double fontSize = 16D;
	
	public Font(TextureManager tm, Resource r)
	{
		texManager = tm;
		texture = new Texture(r);
		texture.setFlag(Texture.FLAG_STORE_PIXELS, true);
		texture.setFlag(Texture.FLAG_BLUR, bluredTexure());
		tm.bind(texture);
		
		int i = texture.pixelBuffer.width;
		int j = texture.pixelBuffer.height;
		int k = i / 16;
		int l = j / 16;
		double f = i / 128D;
		int ai[] = texture.pixelBuffer.pixels;
		
		for(int i1 = 0; i1 < 256; i1++)
		{
			int j1 = i1 % 16;
			int l1 = i1 / 16;
			int j2 = k - 1;
			
			while(true)
			{
				if(j2 < 0) break;
				
				int l2 = j1 * k + j2;
				boolean flag1 = true;
				
				for(int k3 = 0; k3 < l && flag1; k3++)
				{
					int i4 = (l1 * l + k3) * i;
					int i5 = LMColorUtils.getAlpha(ai[l2 + i4]);// & 0xFF;
					
					if(i5 > 16) flag1 = false;
				}
				
				if(!flag1) break;
				
				j2--;
			}
			if(i1 == 32) j2 = (int) (1.5D * f);
			charWidth[i1] = ((double) (j2 + 1) / f + 1F) * 2F;
		}
	}
	
	public boolean bluredTexure()
	{ return false; }
	
	public boolean hasSqSpacing()
	{ return false; }
	
	public void reset()
	{
		drawAtributes = true;
		masterAlpha = 1F;
		masterScale = 1D;
		shadowSize = 0D;
		fontSize = 16D;
	}
	
	public void copyFrom(Font f)
	{
		f.drawAtributes = drawAtributes;
		f.masterAlpha = masterAlpha;
		f.masterScale = masterScale;
		f.shadowSize = shadowSize;
		f.fontSize = fontSize;
	}
	
	public void setDrawAtributes(boolean b)
	{ drawAtributes = b; }
	
	public void setAlpha(float a)
	{ masterAlpha = MathHelperLM.clampFloat(a, 0F, 2F); }
	
	public void setMasterScale(double d)
	{ masterScale = Math.max(d, 0D); }
	
	public void setShadowSize(double s)
	{ shadowSize = Math.max(s, 0D); }
	
	public void setShadowEnabled(boolean b)
	{ setShadowSize(b ? 1D : 0D); }
	
	public void setFontSize(double s)
	{ fontSize = Math.max(s, 0D); }
	
	public double getFontSize()
	{ return fontSize; }
	
	public static String removeFormatting(String s)
	{
		if(s == null || s.isEmpty()) return s;
		StringBuilder sb = new StringBuilder();
		boolean hadCode = false;
		for(int i = 0; i < s.length(); i++)
		{
			if(hadCode)
			{
				hadCode = false;
				continue;
			}
			
			char c = s.charAt(i);
			
			if(c == TextColor.CHAR)
			{
				hadCode = true;
				continue;
			}
			
			sb.append(c);
		}
		return sb.toString();
	}
	
	public void drawText(double x, double y, String s)
	{
		if(s == null || removeFormatting(s).isEmpty()) return;
		
		GLHelper.color.setDefault();
		
		Texture s2 = texManager.currentTexture;
		texManager.bind(texture);
		
		GLHelper.push();
		GLHelper.translate(x, y + (1D - masterScale) * 16D);
		GLHelper.scale(masterScale);
		double posX = 0D;
		double cs = 1D / 16D;
		
		boolean hadCode = false;
		boolean isBold = false;
		boolean isItalic = false;
		boolean hasUnderline = false;
		int color = TextColor.WHITE.color;
		
		for(int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			
			if(hadCode)
			{
				hadCode = false;
				
				if(drawAtributes)
				{
					if(c == TextColor.BOLD.code) isBold = !isBold;
					else if(c == TextColor.ITALIC.code) isItalic = !isItalic;
					else if(c == TextColor.UNDERLINE.code) hasUnderline = !hasUnderline;
					else
					{
						TextColor tc = TextColor.getColor(c);
						if(tc != null) color = tc.color;
					}
				}
				
				continue;
			}
			
			if(c == TextColor.CHAR)
			{
				hadCode = true;
				continue;
			}
			
			//boolean italic = drawAtributes && p.isItalic();
			//boolean underline = drawAtributes && p.hasUnderline();
			
			GLHelper.color.setI(color, (int) (masterAlpha * 255F));
			
			double f = getCharWidth(c);
			double x1 = c % 16 * cs;
			double y1 = c / 16 * cs;
			Renderer.rect(posX, 0D, fontSize, fontSize, x1, y1, cs, cs);
			if(isBold) Renderer.rect(posX + 1D, 1D, fontSize, fontSize, x1, y1, cs, cs);
			posX += f;
		}
		
		if(s2 != null) texManager.bind(s2);
		GLHelper.pop();
		GLHelper.color.setDefault();
	}
	
	public double getCharWidth(int c)
	{
		if(hasSqSpacing()) return fontSize;
		if(c >= charWidth.length) return 16D;
		if(c <= 0) return 0D;
		return charWidth[c];
	}
	
	public double textWidth(TextPart txt)
	{
		if(txt == null || txt.length() == 0) return 0D;
		return textWidth(txt.toString());
	}
	
	public double textWidth(String s)
	{
		if(s == null || s.length() == 0) return 0D;
		double w = 0D;
		for(int i = 0; i < s.length(); i++)
			w += (getCharWidth(s.charAt(i)) + (hasSqSpacing() ? 0 : 1)) * masterScale * (fontSize / 16D);
		return w;
	}
	
	public double getCenterX(TextPart txt, double w)
	{ return (w - textWidth(txt)) / 2F; }
	
	public double longestTextWidth(TextPart... s)
	{
		double f = 0F;
		for(int i = 0; i < s.length; i++)
		{
			double f1 = textWidth(s[i]);
			if(f1 > f) f = f1;
		}
		return f;
	}
	
	public String limitToWidth(String s, double w)
	{
		StringBuilder sb = new StringBuilder();
		double pw = 0;
		
		for(int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			pw += getCharWidth(c);
			if(pw >= w) return sb.toString();
			else sb.append(c);
		}
		
		return sb.toString();
	}
}