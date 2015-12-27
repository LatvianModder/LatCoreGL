package latmod.core.gui;

import latmod.core.input.LMMouse;
import latmod.core.rendering.*;
import latmod.core.res.Resource;

/** Made by LatvianModder */
public abstract class IconButton extends Button
{
	public Texture icon = null;
	public boolean smoothTexture = false;
	public String iconName = null;
	
	public IconButton(Gui s, double x, double y, double w, double h, String s1)
	{
		super(s, x, y, w, h, s1);
		iconName = s1;
		icon = s.texManager.getTexture(Resource.getTexture(iconName + ".png"));
	}
	
	public void onRender()
	{
		super.onRender();

		GLHelper.texture.enable();
		GLHelper.color.setDefault();
		icon.bind();
		Renderer.rect(posX, posY, width, height);
	}
	
	public void onPostRender()
	{
		if(mouseOver())
		{
			GLHelper.texture.enable();
			gui.parent.font.drawText(LMMouse.x + 8D, LMMouse.y - 16D, getText());
		}
	}

	public IconButton setSmooth()
	{ smoothTexture = true; return this; }
}