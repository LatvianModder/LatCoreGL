package latmod.core.gui;

import latmod.core.LatCoreGL;
import latmod.core.Resource;
import latmod.core.input.EventKeyPressed;
import latmod.core.input.EventKeyReleased;
import latmod.core.input.EventMousePressed;
import latmod.core.input.EventMouseReleased;
import latmod.core.input.EventMouseScrolled;
import latmod.core.input.IInputHandler;
import latmod.core.input.LMInput;
import latmod.core.rendering.GLHelper;
import latmod.core.rendering.Renderer;
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
	
	@Override
	public void onKeyPressed(EventKeyPressed e)
	{
	}
	
	@Override
	public void onKeyReleased(EventKeyReleased e)
	{
	}
	
	@Override
	public void onMousePressed(EventMousePressed e)
	{
	}
	
	@Override
	public void onMouseReleased(EventMouseReleased e)
	{
	}
	
	@Override
	public void onMouseScrolled(EventMouseScrolled e)
	{
	}
}