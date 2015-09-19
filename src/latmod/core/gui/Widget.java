package latmod.core.gui;

import latmod.core.*;
import latmod.core.input.*;
import latmod.core.rendering.*;
import latmod.core.res.Resource;
import latmod.core.util.LMColorUtils;

/** Made by LatvianModder */
public class Widget extends Box2D implements IInputEvents
{
	public static boolean playSound = true;//IScreen
	
	public static final Resource sound_click = Resource.getSound("click");
	
	public static boolean playClickSound()
	{ if(playSound) playSound(sound_click); return playSound; }
	
	public final Gui gui;
	public int widgetID = 0;
	
	public TextPart txt = null;
	public boolean selected = false;
	public int color = 0xFFFFFFFF;
	
	public Widget(Gui s, double x, double y, double w, double h)
	{
		gui = s;
		posX = x;
		posY = y;
		width = w;
		height = h;
		
		if(centerX()) posX -= width / 2D;
		if(centerY()) posY -= height / 2D;
	}
	
	public boolean centerX()
	{ return false; }
	
	public boolean centerY()
	{ return false; }
	
	public final int hashCode()
	{ return widgetID; }
	
	public Widget setText(TextPart t)
	{ txt = t; return this; }
	
	public void onRender()
	{
		Renderer.disableTexture();
		LMColorUtils.setGLColor(0, 50);
		Renderer.rect(posX, posY, width, height);
	}
	
	public void onPostRender()
	{
	}
	
	public boolean mouseOver()
	{ return LMMouse.isOver(this); }
	
	public void textCentred(double x, double y, TextPart s)
	{ gui.parent.font.drawText(x - gui.parent.font.textWidth(s) / 2D, y - 8D, s); }
	
	public Widget setColor(int c)
	{ color = c; return this; }
	
	public static void playSound(Resource r)
	{ LatCoreGL.mainFrame.soundManager.playSound(r); }
}