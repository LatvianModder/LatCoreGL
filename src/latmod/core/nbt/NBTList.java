package latmod.core.nbt;
import java.util.Iterator;

import latmod.core.util.*;

/** Made by LatvianModder */
public class NBTList extends NBTBase implements INBTParent
{
	public final FastList<NBTBase> list;
	public NBTID listID = NBTID.NONE;
	
	public NBTList()
	{
		super(NBTID.LIST);
		list = new FastList<NBTBase>();
	}
	
	public String toString()
	{
		if(size() == 0) return "NBTList '" + name + "' [0 x None] { }";
		return "NBTList '" + name + "' [" + size() + " x " + getObj(0).getClass().getSimpleName() + "] { " + list.toString() + " }";
	}
	
	public void read(DataIOStream dios) throws Exception
	{
		list.clear();

		int size = dios.readShort();

		if(size > 0)
		{
			listID = NBTID.get(dios.readByte());

			for(int i = 0; i < size; i++)
			{
				NBTBase b = listID.newBaseFromID();
				b.read(dios);
				add(b);
			}
		}
	}
	
	public void write(DataIOStream dios) throws Exception
	{
		int s = size();
		
		dios.writeShort(s);

		if(s > 0)
		{
			for(int i = 0; i < size(); i++)
			{
				if(i == 0) dios.writeByte(listID.ID);
				get(i).write(dios);
			}
		}
	}
	
	public Object getData()
	{ return list; }
	
	public int getByteCount()
	{
		int bcount = 2;
		
		if(size() > 0)
		{
			for(int i = 0; i < size(); i++)
			{
				if(i == 0) bcount += 1;
				bcount += get(i).getByteCount();
			}
		}
		
		return bcount;
	}
	
	public int size()
	{ return list.size(); }
	
	public void add(NBTBase b)
	{
		if(b != null && (size() == 0 || listID == NBTID.NONE || b.ID == listID))
		{ listID = b.ID; list.add(b.init(null, this)); }
	}
	
	public void addObj(Object o)
	{
		NBTBase b = NBTID.newBaseFromObject(o);
		if(b != null) add(b);
	}
	
	public NBTBase get(int i)
	{ return list.get(i); }
	
	@SuppressWarnings("unchecked")
	public <E> E getObj(int i)
	{ NBTBase b = get(i);
	return (b == null) ? null : (E)b.getData(); }
	
	public void clear()
	{ list.clear(); }
	
	public void remove(int i)
	{ list.remove(i); }
	
	public <E> Iterator<E> getIterator()
	{ return new NBTListIterator<E>(); }
	
	private class NBTListIterator<E> implements Iterator<E>
	{
		public int pos = 0;

		public boolean hasNext()
		{ return pos < list.size(); }

		@SuppressWarnings("unchecked")
		public E next()
		{
			Object o = getObj(pos);
			pos++;
			return (E)o;
		}

		public void remove() { }
	}
	
	public NBTList clone()
	{
		NBTList list1 = new NBTList();
		list1.init(name, parent);
		list1.list.addAll(list);
		return list1;
	}
	
	public boolean equals(Object o) { if(o == null) return false;
	return (o instanceof NBTList) && ((NBTList)o).list.equals(list); }
	
	public FastList<NBTBase> getChildren()
	{ return list; }
}