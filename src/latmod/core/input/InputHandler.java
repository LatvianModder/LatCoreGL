package latmod.core.input;
import latmod.core.input.keys.*;
import latmod.core.input.mouse.*;
import latmod.core.util.FastList;

public class InputHandler implements IInputEvents
{
	public final FastList<IKeyPressed> keyPressed;
	public final FastList<IKeyReleased> keyReleased;
	public final FastList<IMousePressed> mousePressed;
	public final FastList<IMouseReleased> mouseReleased;
	public final FastList<IMouseScrolled> mouseScrolled;
	public final FastList<IMouseMoved> mouseMoved;
	public final FastList<InputHandler> subHandlers;
	
	public InputHandler()
	{
		keyPressed = new FastList<IKeyPressed>();
		keyReleased = new FastList<IKeyReleased>();
		mousePressed = new FastList<IMousePressed>();
		mouseReleased = new FastList<IMouseReleased>();
		mouseScrolled = new FastList<IMouseScrolled>();
		mouseMoved = new FastList<IMouseMoved>();
		subHandlers = new FastList<InputHandler>();
	}
	
	public void add(IInputEvents o)
	{
		if(o == null) return;
		if(o instanceof InputHandler && !subHandlers.contains(o)) subHandlers.add((InputHandler)o);
		if(o instanceof IKeyPressed && !keyPressed.contains(o)) keyPressed.add((IKeyPressed)o);
		if(o instanceof IKeyReleased && !keyReleased.contains(o)) keyReleased.add((IKeyReleased)o);
		if(o instanceof IMousePressed && !mousePressed.contains(o)) mousePressed.add((IMousePressed)o);
		if(o instanceof IMouseReleased && !mouseReleased.contains(o)) mouseReleased.add((IMouseReleased)o);
		if(o instanceof IMouseScrolled && !mouseScrolled.contains(o)) mouseScrolled.add((IMouseScrolled)o);
		if(o instanceof IMouseMoved && !mouseMoved.contains(o)) mouseMoved.add((IMouseMoved)o);
	}
	
	public void remove(IInputEvents o)
	{
		if(o == null) return;
		subHandlers.remove(o);
		keyPressed.remove(o);
		keyReleased.remove(o);
		mousePressed.remove(o);
		mouseReleased.remove(o);
		mouseScrolled.remove(o);
		mouseMoved.remove(o);
	}
	
	public void clear()
	{
		subHandlers.clear();
		keyPressed.clear();
		keyReleased.clear();
		mousePressed.clear();
		mouseReleased.clear();
		mouseScrolled.clear();
		mouseMoved.clear();
	}
	
	public void onKeyPressed(EventKeyPressed e)
	{
		for(IKeyPressed l : keyPressed)
		{ l.onKeyPressed(e); if(e.isCancelled()) return; }
	}
	
	public void onKeyReleased(EventKeyReleased e)
	{ for(IKeyReleased l : keyReleased) l.onKeyReleased(e); }
	
	public void onMousePressed(EventMousePressed e)
	{
		for(IMousePressed l : mousePressed)
		{ l.onMousePressed(e); if(e.isCancelled()) return; }
	}
	
	public void onMouseReleased(EventMouseReleased e)
	{ for(IMouseReleased l : mouseReleased) l.onMouseReleased(e); }
	
	public void onMouseScrolled(EventMouseScrolled e)
	{ for(IMouseScrolled l : mouseScrolled) l.onMouseScrolled(e); }
	
	public void onMouseMoved(EventMouseMoved e)
	{ for(IMouseMoved l: mouseMoved) l.onMouseMoved(e); }
}