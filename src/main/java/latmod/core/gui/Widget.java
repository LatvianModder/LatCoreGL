package latmod.core.gui;

import latmod.core.*;
import latmod.core.input.*;
import latmod.core.rendering.*;
import latmod.lib.util.FinalIDObject;

import java.util.List;

/**
 * Made by LatvianModder
 */
public class Widget extends FinalIDObject implements IInputHandler
{
	public static boolean playSound = true;//IScreen
	
	public static final Resource sound_click = new Resource("core", "click");
	
	public static boolean playClickSound()
	{
		if(playSound) LatCoreGL.window.getSoundManager().playSound(sound_click);
		return playSound;
	}
	
	public Panel parentPanel;
	public double posX, posY, width, height;
	
	public Widget(String id, double x, double y, double w, double h)
	{
		super(id);
		posX = x;
		posY = y;
		width = w;
		height = h;
	}
	
	public final Widget setParentPanel(Panel p)
	{
		parentPanel = p;
		return this;
	}
	
	public final Gui getGui()
	{ return LatCoreGL.window.getGui(); }
	
	public double getAX()
	{ return (parentPanel == null) ? posX : (parentPanel.getAX() + posX); }
	
	public double getAY()
	{ return (parentPanel == null) ? posY : (parentPanel.getAY() + posY); }
	
	public void renderWidget()
	{
		GLHelper.texture.disable();
		GLHelper.color.setI(0, 0, 0, 50);
		Renderer.rect(getAX(), getAY(), width, height);
	}
	
	public void getMouseOverText(List<String> list)
	{
	}
	
	public boolean mouseOver()
	{
		return isAt(LMInput.mouseX, LMInput.mouseY);
	}
	
	public boolean isAt(double x, double y)
	{
		double ax = getAX();
		double ay = getAY();
		if(x < ax || y < ay || x > ax + width) return false;
		return y <= posY + height;
	}
	
	public void onKeyPressed(EventKeyPressed e)
	{
	}
	
	public void onKeyReleased(EventKeyReleased e)
	{
	}
	
	public void onMousePressed(EventMousePressed e)
	{
	}
	
	public void onMouseReleased(EventMouseReleased e)
	{
	}
	
	public void onMouseScrolled(EventMouseScrolled e)
	{
	}
}