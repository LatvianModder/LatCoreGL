package latmod.core.rendering;
import org.lwjgl.opengl.GL11;

import latmod.core.MathHelperLM;
import latmod.core.res.Resource;
import latmod.core.util.*;

/** <b>Made by LatvianModder</b> <br>
Character length code taken from Minecraft:FontRenderer*/
public class Font
{
	public final TextureManager texManager;
	public final Texture texture;
	public final double[] charWidth = new double[256];
	private boolean drawAtributes = true;
	private double masterAlpha = 1D;
	private double masterScale = 1D;
	private double shadowSize = 0D;
	private double fontSize = 16D;
	
	public Font(TextureManager tm, Resource r)
	{
		texManager = tm;
		texture = texManager.getTexture(r);
		texture.blured = bluredTexure();
		texture.update();
		
		int i = texture.pixels.width;
		int j = texture.pixels.height;
		int k = i / 16;
		int l = j / 16;
		double f = (double)i / 128F;
		int ai[] = texture.pixels.pixels;

		for (int i1 = 0; i1 < 256; i1++)
		{
			int j1 = i1 % 16;
			int l1 = i1 / 16;
			int j2 = k - 1;
			
			while(true)
			{
				if (j2 < 0) break;
				
				int l2 = j1 * k + j2;
				boolean flag1 = true;
				
				for (int k3 = 0; k3 < l && flag1; k3++)
				{
					int i4 = (l1 * l + k3) * i;
					int i5 = LMColorUtils.getAlpha(ai[l2 + i4]);// & 0xFF;
					
					if (i5 > 16) flag1 = false;
				}
				
				if (!flag1) break;
				
				j2--;
			}
			if (i1 == 32) j2 = (int)(1.5D * (double)f);
			charWidth[i1] = ((double)(j2 + 1) / f + 1F) * 2F;
		}
	}
	
	public boolean bluredTexure()
	{ return false; }
	
	public boolean hasSqSpacing()
	{ return false; }
	
	public void reset()
	{
		drawAtributes = true;
		masterAlpha = 1D;
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
	
	public void setAlpha(double a)
	{ masterAlpha = MathHelperLM.clamp(a, 0D, 2D); }
	
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
	
	public void drawText(double x, double y, String s)
	{ drawText(x, y, TextPart.get(s)); }
	
	public void drawText(double x, double y, TextPart txt)
	{
		if(txt == null || txt.length() == 0) return;
		
		FastList<TextPart> parts = txt.toList();
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		Resource s2 = texManager.currentTexture;
		texManager.setTexture(texture);
		
		Renderer.push();
		Renderer.translate(x, y + (1D - masterScale) * 16D);
		Renderer.scale(masterScale);
		double posX = 0D;
		double cs = 1D / 16D;
		
		for(int i = 0; i < parts.size(); i++)
		{
			TextPart p = parts.get(i);
			
			int col = p.getColor().color;
			double a = masterAlpha * (LMColorUtils.getAlpha(col) / 255D);
			
			LMColorUtils.setGLColor(col, (int)(a * 255D));
			
			boolean bold = drawAtributes && p.isBold();
			//boolean italic = drawAtributes && p.isItalic();
			//boolean underline = drawAtributes && p.hasUnderline();
			
			for(int j = 0; j < p.text.length(); j++)
			{
				char c = p.text.charAt(j);
				
				double f = getCharWidth(c);
				double x1 = ((int)(c % 16)) * cs;
				double y1 = ((int)(c / 16)) * cs;
				Renderer.rect(posX, 0D, fontSize, fontSize, x1, y1, cs, cs);
				if(bold) Renderer.rect(posX + 1D, 1D, fontSize, fontSize, x1, y1, cs, cs);
				posX += f;
			}
		}
		
		if(s2 != null) texManager.setTexture(s2);
		Renderer.pop();
		GL11.glColor4f(1F, 1F, 1F, 1F);
	}
	
	public double getCharWidth(int c)
	{
		if(hasSqSpacing()) return fontSize;
		if(c >= charWidth.length) return 16D;
		if(c <= 0) return 0D; return charWidth[c];
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
		double f = 0F; for(int i = 0; i < s.length; i++)
		{ double f1 = textWidth(s[i]);
		if(f1 > f) f = f1; } return f;
	}
	
	public String limitToWidth(String s, double w)
	{
		String s1 = "";
		for(int i = 0; i < s.length(); i++)
		if(textWidth(s1 + "" + s.charAt(i)) <= w)
		s1 += s.charAt(i); return s1;
	}
}