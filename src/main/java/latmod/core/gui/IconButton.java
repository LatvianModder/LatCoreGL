package latmod.core.gui;
import org.lwjgl.opengl.GL11;

import latmod.core.input.LMMouse;
import latmod.core.rendering.*;
import latmod.core.res.Resource;

/** Made by LatvianModder */
public abstract class IconButton extends Button
{
	public Resource icon = null;
	public boolean smoothTexture = false;
	public String iconName = null;
	
	public IconButton(Gui s, double x, double y, double w, double h, String s1)
	{
		super(s, x, y, w, h, s1);
		iconName = s1;
		icon = Resource.getTexture(iconName + ".png");
	}
	
	public void onRender()
	{
		super.onRender();
		
		Renderer.enableTexture();
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		TextureManager.instance.setTexture(icon);
		Renderer.rect(posX, posY, width, height);
	}
	
	public void onPostRender()
	{
		if(mouseOver())
		{
			Renderer.enableTexture();
			gui.parent.font.drawText(LMMouse.x + 8D, LMMouse.y - 16D, getText());
		}
	}

	public IconButton setSmooth()
	{ smoothTexture = true; return this; }
}