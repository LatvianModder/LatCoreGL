package latmod.core.gui;

import latmod.core.IWindow;
import latmod.core.input.EventKeyPressed;
import latmod.core.input.EventKeyReleased;
import latmod.core.input.EventMousePressed;
import latmod.core.input.EventMouseReleased;
import latmod.core.input.EventMouseScrolled;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 10.04.2016.
 */
public class Panel extends Widget
{
	public final List<Widget> widgets;
	
	public Panel(String id, double x, double y, double w, double h)
	{
		super(id, x, y, w, h);
		widgets = new ArrayList<>();
	}
	
	public IWindow getWindow()
	{ return parentPanel == null ? null : parentPanel.getWindow(); }
	
	public void addWidgets()
	{
	}
	
	public void add(Widget w)
	{
		if(w != null)
		{
			widgets.add(w);
			w.setParentPanel(this);
		}
	}
	
	public final Widget getWidget(Object id)
	{
		int idx = widgets.indexOf(id);
		return idx == -1 ? null : widgets.get(idx);
	}
	
	@Override
	public void renderWidget()
	{
		for(Widget w : widgets)
		{
			w.renderWidget();
		}
	}
	
	@Override
	public void onKeyPressed(EventKeyPressed e)
	{
		for(Widget w : widgets)
		{
			w.onKeyPressed(e);
			if(e.isCancelled()) return;
		}
	}
	
	@Override
	public void onKeyReleased(EventKeyReleased e)
	{
		for(Widget w : widgets)
		{
			w.onKeyReleased(e);
		}
	}
	
	@Override
	public void onMousePressed(EventMousePressed e)
	{
		for(Widget w : widgets)
		{
			w.onMousePressed(e);
			if(e.isCancelled()) return;
		}
	}
	
	@Override
	public void onMouseReleased(EventMouseReleased e)
	{
		for(Widget w : widgets)
		{
			w.onMouseReleased(e);
		}
	}
	
	@Override
	public void onMouseScrolled(EventMouseScrolled e)
	{
		for(Widget w : widgets)
		{
			w.onMouseScrolled(e);
		}
	}
}