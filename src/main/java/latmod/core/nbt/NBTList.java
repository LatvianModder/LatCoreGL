package latmod.core.nbt;

import latmod.lib.ByteIOStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Made by LatvianModder
 */
public class NBTList extends NBTBase implements INBTParent
{
	public final List<NBTBase> list;
	public NBTID listID = NBTID.NONE;
	
	public NBTList()
	{
		super(NBTID.LIST);
		list = new ArrayList<>();
	}
	
	@Override
	public void read(ByteIOStream dios)
	{
		list.clear();
		
		int size = dios.readUnsignedShort();
		
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
	
	@Override
	public void write(ByteIOStream dios)
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
	
	@Override
	public String toString()
	{ return list.toString(); }
	
	@Override
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
		{
			listID = b.ID;
			list.add(b.init(null, this));
		}
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
	{
		NBTBase b = get(i);
		return (b == null) ? null : (E) b.getData();
	}
	
	public void clear()
	{ list.clear(); }
	
	public void remove(int i)
	{ list.remove(i); }
	
	public <E> Iterator<E> getIterator()
	{ return new NBTListIterator<E>(); }
	
	private class NBTListIterator<E> implements Iterator<E>
	{
		public int pos = 0;
		
		@Override
		public boolean hasNext()
		{ return pos < list.size(); }
		
		@Override
		@SuppressWarnings("unchecked")
		public E next()
		{
			Object o = getObj(pos);
			pos++;
			return (E) o;
		}
		
		@Override
		public void remove() { }
	}
	
	@Override
	public NBTList clone()
	{
		NBTList list1 = new NBTList();
		list1.init(name, parent);
		list1.list.addAll(list);
		return list1;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null) return false;
		return (o instanceof NBTList) && ((NBTList) o).list.equals(list);
	}
	
	@Override
	public Collection<NBTBase> getChildren()
	{ return list; }
}