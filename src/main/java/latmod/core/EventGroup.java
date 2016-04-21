package latmod.core;

import latmod.lib.LMUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Made by LatvianModder
 */
public class EventGroup
{
	public static final EventGroup DEFAULT = new EventGroup("default");
	
	public final String groupName;
	public final List<EventListenerObject> eventHandlers;
	public boolean isDisabled = false;
	
	public EventGroup(String s)
	{
		groupName = s;
		eventHandlers = new ArrayList<>();
	}
	
	public EventGroup setDisabled(boolean b)
	{
		isDisabled = b;
		return this;
	}
	
	/**
	 * Adds event listener for this class
	 */
	public void addListener(Object o)
	{
		if(o == null) return;
		
		Method m[] = o.getClass().getMethods();
		
		if(m != null && m.length > 0) for(int i = 0; i < m.length; i++)
		{
			if(containsEA(m[i].getDeclaredAnnotations()) && m[i].getParameterTypes().length == 1)
			{
				Class<?> c = m[i].getParameterTypes()[0];
				
				if(Event.class.isAssignableFrom(c) && !c.equals(Event.class))
				{
					List<Class<?>> al = LMUtils.addSubclasses(c, null, true);
					al.add(c);
					for(Class<?> c1 : al) eventHandlers.add(new EventListenerObject(o, m[i], c1));
				}
			}
		}
	}
	
	private boolean containsEA(Annotation[] a)
	{
		if(a != null && a.length > 0) for(int i = 0; i < a.length; i++)
		{ if(a[i] instanceof EventHandler) return true; }
		return false;
	}
	
	/**
	 * Removes event listener for this class <br>
	 *
	 * @Deprecated Because you don't really need to remove event<br>
	 * listener nor there is way to remove only from one event listening
	 */
	public void removeListener(Object o)
	{
		if(o == null) return;
		
		for(int i = 0; i < eventHandlers.size(); i++)
		{
			if(eventHandlers.get(i).obj == o) eventHandlers.remove(i);
		}
	}
	
	public boolean send(Event e)
	{
		if(e == null || isDisabled) return false;
		
		for(int i = 0; i < eventHandlers.size(); i++)
		{
			EventListenerObject el = eventHandlers.get(i);
			
			if(el.canHandleEvent(e))
			{
				try
				{
					el.invoke(e);
					if(e.canCancel() && e.isCancelled()) return true;
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		
		return false;
	}
	
	public static class EventListenerObject
	{
		public Object obj;
		public Method method;
		public Class<?> eventClass;
		
		public EventListenerObject(Object o, Method m, Class<?> c)
		{
			obj = o;
			method = m;
			eventClass = c;
		}
		
		public Object invoke(Event e) throws Exception
		{ return method.invoke(obj, e); }
		
		public boolean canHandleEvent(Event e)
		{ return e.getClass().isAssignableFrom(eventClass); }
	}
}