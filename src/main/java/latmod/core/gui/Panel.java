package latmod.core.gui;

import latmod.core.input.*;

import java.util.*;

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
	
	public void loadWidgets()
	{
	}
	
	public void add(Widget w)
	{
		if(w != null)
		{
			widgets.add(w);
			w.setPanel(this);
		}
	}
	
	public final Widget getWidget(Object id)
	{
		int idx = widgets.indexOf(id);
		return idx == -1 ? null : widgets.get(idx);
	}
	
	public void renderWidget()
	{
		for(Widget w : widgets)
		{
			w.renderWidget();
		}
	}
	
	public void onKeyPressed(EventKeyPressed e)
	{
		for(Widget w : widgets)
		{
			w.onKeyPressed(e);
			if(e.isCancelled()) return;
		}
	}
	
	public void onKeyReleased(EventKeyReleased e)
	{
		for(Widget w : widgets)
		{
			w.onKeyReleased(e);
		}
	}
	
	public void onMousePressed(EventMousePressed e)
	{
		for(Widget w : widgets)
		{
			w.onMousePressed(e);
			if(e.isCancelled()) return;
		}
	}
	
	public void onMouseReleased(EventMouseReleased e)
	{
		for(Widget w : widgets)
		{
			w.onMouseReleased(e);
		}
	}
	
	public void onMouseScrolled(EventMouseScrolled e)
	{
		for(Widget w : widgets)
		{
			w.onMouseScrolled(e);
		}
	}
}