package latmod.core.gui;

import latmod.core.input.EventMousePressed;
import latmod.core.rendering.*;
import latmod.lib.LMColorUtils;

import java.util.List;

/**
 * Made by LatvianModder
 */
public abstract class Button extends Widget
{
	public boolean leftAlign = false;
	public int color;
	public String title;
	
	public Button(String id, double x, double y, double w, double h, String s1)
	{
		super(id, x, y, w, h);
		title = s1;
		color = LMColorUtils.WIDGETS;
	}
	
	public void renderWidget()
	{
		GLHelper.texture.disable();
		GLHelper.color.setI(mouseOver() ? LMColorUtils.lerp(color, 0xFF000000, 0.5F) : color);
		Renderer.rect(posX, posY, width, height);
	}
	
	public void getMouseOverText(List<String> list)
	{
		if(title != null && !title.isEmpty())
		{
			double x = leftAlign ? posX + 8D : posX + (width - getGui().font.textWidth(title)) / 2D + 8D;
			getGui().font.drawText(x, posY + height / 2F - 8F, title);
		}
	}
	
	public void onMousePressed(EventMousePressed e)
	{
		if(mouseOver())
		{
			playClickSound();
			onPressed(e);
		}
	}
	
	public abstract void onPressed(EventMousePressed e);
}