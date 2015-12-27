package latmod.core.nbt;

import latmod.lib.ByteIOStream;

public class NBTLong extends NBTBase
{
	public long data;
	
	public NBTLong(long p)
	{ super(NBTID.LONG); data = p; }

	public void read(ByteIOStream dios)
	{ data = dios.readLong(); }

	public void write(ByteIOStream dios)
	{ dios.writeLong(data); }
	
	public int getByteCount()
	{ return 8; }
	
	public String toString()
	{ return Long.toString(data); }
}