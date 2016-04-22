package latmod.core.gui;

import latmod.core.*;
import latmod.core.rendering.*;

/**
 * Made by LatvianModder
 */
public abstract class Gui extends Panel
{
	public final IWindow window;
	public final Panel mainPanel;
	public Font font;
	private boolean isDirty = true;
	
	public Gui(String id, IWindow w)
	{
		super(id, 0D, 0D, 0D, 0D);
		window = w == null ? LatCoreGL.window : w;
		
		mainPanel = new Panel(id, 0, 0, 0, 0)
		{
			public void addWidgets()
			{
				Gui.this.addWidgets();
			}
		};
	}
	
	public final IWindow getWindow()
	{ return window; }
	
	public final void markDirty()
	{ isDirty = true; }
	
	public boolean grabMouse()
	{ return false; }
	
	public void init()
	{
		mainPanel.width = window.getWidth();
		mainPanel.height = window.getHeight();
		font = LatCoreGL.window.getFont();
	}
	
	public abstract void addWidgets();
	
	public void onClosed()
	{
	}
	
	public final void renderGui()
	{
		if(isDirty)
		{
			mainPanel.widgets.clear();
			mainPanel.addWidgets();
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