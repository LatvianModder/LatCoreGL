package latmod.core.gui;

import latmod.core.LatCoreGL;
import latmod.core.rendering.*;
import latmod.lib.util.FinalIDObject;

/**
 * Made by LatvianModder
 */
public abstract class Gui extends FinalIDObject
{
	public final Panel mainPanel;
	public Font font;
	private boolean isDirty = true;
	
	public Gui(String id)
	{
		super(id);
		
		mainPanel = new Panel(id, 0, 0, 0, 0)
		{
			public void loadWidgets()
			{
				Gui.this.loadWidgets();
			}
		};
	}
	
	public final void markDirty()
	{ isDirty = true; }
	
	public boolean grabMouse()
	{ return false; }
	
	public void init()
	{
		mainPanel.width = LatCoreGL.window.getWidth();
		mainPanel.height = LatCoreGL.window.getHeight();
		font = LatCoreGL.window.getFont();
	}
	
	public abstract void loadWidgets();
	
	public void onClosed()
	{
	}
	
	public final void renderGui()
	{
		if(isDirty)
		{
			mainPanel.widgets.clear();
			mainPanel.loadWidgets();
			isDirty = false;
		}
		
		onRender();
	}
	
	public void onRender()
	{
		GLHelper.color.setDefault();
		mainPanel.renderWidget();
		GLHelper.texture.enable();
		GLHelper.color.setDefault();
	}
}