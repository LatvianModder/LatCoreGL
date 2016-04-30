package latmod.core;

import latmod.lib.LMUtils;
import latmod.lib.util.FinalIDObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by LatvianModder on 30.04.2016.
 */
public class EventHandler extends FinalIDObject
{
	public static final EventHandler MAIN = new EventHandler("main");
	
	private final Map<Class<?>, Collection<Consumer<?>>> map;
	private boolean isDisabled = false;
	
	public EventHandler(String s)
	{
		super(s);
		map = new HashMap<>();
	}
	
	public EventHandler setDisabled(boolean b)
	{
		isDisabled = b;
		return this;
	}
	
	public <E extends Event> void addHandler(Class<E> c, Consumer<E> consumer)
	{
		if(consumer == null) return;
		Collection<Consumer<?>> collection = map.get(c);
		
		if(collection == null)
		{
			collection = new HashSet<>();
			map.put(c, collection);
		}
		
		collection.add(consumer);
	}
	
	public <E extends Event> boolean send(E e)
	{
		if(e == null || isDisabled) return false;
		
		if(map.containsKey(e.getClass()))
		{
			Collection<Consumer<?>> l = map.get(e.getClass());
			
			if(l != null)
			{
				for(Consumer<?> c : l)
				{
					try
					{
						c.accept(LMUtils.convert(e));
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
					
					if(e.isCancelled())
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
}