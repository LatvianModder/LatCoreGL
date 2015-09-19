package latmod.core.gui;

import latmod.core.input.LMMouse;
import latmod.core.input.mouse.*;
import latmod.core.rendering.*;
import latmod.core.util.LMColorUtils;

/** Made by LatvianModder */
public abstract class Button extends Widget implements IMousePressed
{
	public boolean leftAlign = false;
	
	public Button(Gui s, double x, double y, double w, double h, String s1)
	{
		super(s, x, y, w, h);
		txt = TextPart.get(s1);
		color = LMColorUtils.WIDGETS;
	}
	
	public void onRender()
	{
		Renderer.disableTexture();
		LMColorUtils.setGLColor(mouseOver() ? LMColorUtils.lerp(color, 0xFF000000, 0.5F) : color);
		Renderer.rect(posX, posY, width, height);
	}
	
	public void onPostRender()
	{
		Renderer.enableTexture();
		
		TextPart s = getText();
		if(s != null && s.length() > 0)
		{
			double x = 0; if(leftAlign) x = posX + 8F;
			else x = posX + (width - gui.parent.font.textWidth(s)) / 2F + 8F;
			gui.parent.font.drawText(x, posY + height / 2F - 8F, s);
		}
	}
	
	public TextPart getText()
	{ return txt; }

	public void onMousePressed(EventMousePressed e)
	{
		if(LMMouse.isOver(this))
		{
			playClickSound();
			onPressed(e);
			e.cancel();
		}
	}
	
	public abstract void onPressed(EventMousePressed e);
}