package latmod.core.nbt;
import latmod.lib.*;

import java.util.*;

/** Made by LatvianModder */
public class NBTMap extends NBTBase implements INBTParent
{
	public FastMap<String, NBTBase> map = new FastMap<String, NBTBase>();
	
	public NBTMap()
	{ super(NBTID.MAP); }
	
	public void read(ByteIOStream dios)
	{
		map.clear();
		int size = dios.readUnsignedShort();
		if(size <= 0) return;

		for(int i = 0; i < size; i++)
		{
			byte typeID = dios.readByte();
			String s = dios.readUTF();
			NBTBase b = NBTID.get(typeID).newBaseFromID();
			
			if(b != null)
			{
				b.read(dios);
				b.init(s, this);
				map.put(s, b);
			}
		}
	}
	
	public void write(ByteIOStream dios)
	{
		int s = size();
		
		dios.writeShort(s);
		
		if(s > 0)
		{
			for(Map.Entry<String, NBTBase> e : map.entrySet())
			{
				NBTBase b = e.getValue();
				dios.writeByte(b.ID.ID);
				dios.writeUTF(e.getKey());
				b.write(dios);
			}
		}
	}
	
	public int getByteCount()
	{
		int bcount = 2;

		for(Map.Entry<String, NBTBase> e : map.entrySet())
		{
			bcount += 1;
			bcount += ByteIOStream.getUTFLength(e.getKey());
			bcount += e.getValue().getByteCount();
		}
		
		return bcount;
	}
	
	public String toString()
	{ return map.toString(); }
	
	// 'Get' methods //
	
	@SuppressWarnings("unchecked")
	protected <E> E getObject(String s)
	{ NBTBase b = map.get(s);
	return (b == null) ? null : (E)b.getData(); }
	
	public String getString(String s)
	{ return getObject(s); }
	
	public boolean getBoolean(String s)
	{ return getByte(s) == 1; }
	
	public int getByte(String s)
	{ Byte p = getObject(s);
	return (p == null) ? -1 : p.byteValue(); }
	
	public int getShort(String s)
	{ Short p = getObject(s);
	return (p == null) ? -1 : p.shortValue(); }
	
	public int getInt(String s)
	{ Integer p = getObject(s);
	return (p == null) ? -1 : p.intValue(); }
	
	public long getLong(String s)
	{ Long p = getObject(s);
	return (p == null) ? -1 : p.longValue(); }
	
	public float getFloat(String s)
	{ Float p = getObject(s);
	return (p == null) ? -1F : p.floatValue(); }
	
	public double getDouble(String s)
	{ Double p = getObject(s);
	return (p == null) ? -1D : p.doubleValue(); }
	
	public NBTList getList(String s)
	{ NBTBase p = map.get(s);
	return (p == null) ? null : (NBTList)p; }
	
	public NBTMap getMap(String s)
	{ NBTBase p = map.get(s);
	return (p == null) ? null : (NBTMap)p; }
	
	public byte[] getByteArray(String s)
	{ return getObject(s); }
	
	public int[] getIntArray(String s)
	{ return getObject(s); }
	
	public double[] getDoubleArray(String s)
	{ return getObject(s); }
	
	public NBTBase getBase(String s)
	{ return map.get(s); }
	
	public int size()
	{ return map.size(); }
	
	// 'Set' methods //
	
	public void setString(String s, String p)
	{ map.put(s, new NBTString(p).init(s, this)); }
	
	public void setBoolean(String s, boolean p)
	{ map.put(s, new NBTByte(p ? (byte)1 : (byte)0).init(s, this)); }
	
	public void setByte(String s, int p)
	{ map.put(s, new NBTByte((byte)p).init(s, this)); }
	
	public void setShort(String s, int p)
	{ map.put(s, new NBTShort((short)p).init(s, this)); }
	
	public void setInt(String s, int p)
	{ map.put(s, new NBTInt(p).init(s, this)); }
	
	public void setLong(String s, long p)
	{ map.put(s, new NBTLong(p).init(s, this)); }
	
	public void setFloat(String s, float p)
	{ map.put(s, new NBTFloat(p).init(s, this)); }
	
	public void setDouble(String s, double p)
	{ map.put(s, new NBTDouble(p).init(s, this)); }
	
	public void setList(String s, NBTList p)
	{ map.put(s, p.init(s, this)); }
	
	public void setMap(String s, NBTMap p)
	{ map.put(s, p.init(s, this)); }
	
	public void setByteArray(String s, byte... ai)
	{ map.put(s, new NBTByteArray(ai).init(s, this)); }
	
	public void setIntArray(String s, int... ai)
	{ map.put(s, new NBTIntArray(ai).init(s, this)); }
	
	public void setDoubleArray(String s, double... ai)
	{ map.put(s, new NBTDoubleArray(ai).init(s, this)); }
	
	public void setBase(NBTBase b)
	{ if(b != null) map.put(b.name, b); }
	
	public void remove(String s)
	{ map.remove(s); }
	
	public void clear()
	{ map.clear(); }
	
	public boolean hasKey(String s)
	{ return map.containsKey(s); }
	
	public NBTMap clone()
	{
		NBTMap map1 = new NBTMap();
		map1.init(name, parent);
		map1.map.putAll(map);
		return map1;
	}
	
	public boolean equals(Object o) { if(o == null) return false;
	return (o instanceof NBTMap) && ((NBTMap)o).map.equals(map); }
	
	public boolean isEmpty()
	{ return map.isEmpty(); }
	
	public Collection<NBTBase> getChildren()
	{ return map.values(); }
}