package latmod.core.gui;

import latmod.core.IWindow;
import latmod.core.LatCoreGL;
import latmod.core.rendering.Font;
import latmod.core.rendering.GLHelper;

/**
 * Made by LatvianModder
 */
public abstract class Gui extends Panel
{
	public final IWindow window;
	public final Panel mainPanel;
	public Font font;
	private boolean isDirty = true;
	
	public Gui(IWindow w, String id)
	{
		super(id, 0D, 0D, 0D, 0D);
		window = w;
		
		mainPanel = new Panel(id, 0, 0, 0, 0)
		{
			@Override
			public void addWidgets()
			{
				Gui.this.addWidgets();
			}
		};
	}
	
	@Override
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
	
	@Override
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