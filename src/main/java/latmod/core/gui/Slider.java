package latmod.core.gui;

import latmod.core.LatCoreGL;
import latmod.core.input.LMMouse;
import latmod.core.input.mouse.*;
import latmod.core.rendering.*;
import latmod.lib.*;

/** Made by LatvianModder */
public class Slider extends Widget implements IMousePressed, IMouseReleased
{
	public static final String MOVED = "slider_moved";
	
	public double value = 0.5D;
	public double minValue = 0D;
	public double maxValue = 1D;
	
	public double normalValue = 0D;
	protected boolean firstEvent = false;
	public boolean isHorizontal = true;
	
	public Slider(Gui g, double x, double y, double w, double h, String s)
	{
		super(g, x, y, w, h);
		txt = TextPart.get(s);
		color = LMColorUtils.WIDGETS;
	}
	
	public void onRender()
	{
		double val = value;
		
		if(selected)
		{
			value = MathHelperLM.clamp(MathHelperLM.map(LMMouse.x, posX, posX + width, minValue, maxValue), minValue, maxValue);
			if(val != value) onMoved(value - val);
		}
		
		normalValue = (minValue == 0F && maxValue == 1F) ? value : MathHelperLM.map(value, minValue, maxValue, 0F, 1F);
		
		Renderer.disableTexture();
		
		LatCoreGL.setColor(color);
		Renderer.rect(posX, posY, width, height);
		double x = posX + normalValue * (width - 16D);
		
		//Renderer.colorize(Renderer.lerpColor(color, 0, 0.5F, 255), 255);
		LatCoreGL.setColor(LMColorUtils.lerp(color, 0xFF000000, 0.5D, 255));
		Renderer.rect(x, posY, 16D, height);
		
		if(!firstEvent)
		{
			onMoved(0D);
			firstEvent = true;
		}
	}
	
	public void onPostRender()
	{
		Renderer.enableTexture();
		if(txt != null && (selected || LMMouse.isOver(this))) gui.parent.font.drawText(LMMouse.x + 4, LMMouse.y - 10, txt);
	}
	
	public void onMousePressed(EventMousePressed e)
	{
		if(mouseOver())
		{
			selected = true;
			e.cancel();
		}
	}
	
	public void onMouseReleased(EventMouseReleased e)
	{
		selected = false;
	}
	
	public void onMoved(double delta)
	{
	}
}