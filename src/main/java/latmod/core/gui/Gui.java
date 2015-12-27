package latmod.core.gui;

import latmod.core.*;
import latmod.core.input.*;
import latmod.core.rendering.*;
import latmod.lib.FastList;

/** Made by LatvianModder */
public abstract class Gui implements IInputEvents
{
	public final LMFrame parent;
	public final FastList<Widget> widgets;
	public final InputHandler handler;
	public final TextureManager texManager;
	
	public Gui(LMFrame i)
	{
		parent = i;
		widgets = new FastList<>();
		handler = new InputHandler();
		texManager = parent.textureManager;
	}
	
	public abstract void loadWidgets();
	
	public final void onLoaded()
	{
		widgets.clear();
		
		LatCoreGL.mainFrame.inputHandler.add(this);
		LatCoreGL.mainFrame.inputHandler.add(handler);
		
		loadWidgets();
	}
	
	public final void onUnloaded()
	{
		widgets.clear();
		handler.clear();
		
		LatCoreGL.mainFrame.inputHandler.remove(this);
		LatCoreGL.mainFrame.inputHandler.remove(handler);
	}
	
	public void onDestroyed()
	{
	}
	
	public void addWidget(Widget w)
	{
		if(w != null)
		{
			w.widgetID = widgets.size();
			widgets.add(w);
			handler.add(w);
		}
	}
	
	public void onRender()
	{
		GLHelper.color.setDefault();
		
		for(int i = 0; i < widgets.size(); i++)
			widgets.get(i).onRender();
		
		for(int i = 0; i < widgets.size(); i++)
			widgets.get(i).onPostRender();

		GLHelper.texture.enable();
		GLHelper.color.setDefault();
	}
	
	public Widget getWidgetAt(Widget w0, float x, float y)
	{
		for(int i = 0; i < widgets.size(); i++)
		{
			Widget w = widgets.get(i);
			if((w0 == null || (w0 != null && w.widgetID != w0.widgetID)) && w.isAt(x, y)) return w;
		}
		
		return null;
	}
}