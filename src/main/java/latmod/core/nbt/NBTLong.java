package latmod.core.nbt;

import latmod.lib.ByteIOStream;

public class NBTLong extends NBTBase
{
	public long data;
	
	public NBTLong(long p)
	{
		super(NBTID.LONG);
		data = p;
	}
	
	@Override
	public void read(ByteIOStream dios)
	{ data = dios.readLong(); }
	
	@Override
	public void write(ByteIOStream dios)
	{ dios.writeLong(data); }
	
	@Override
	public int getByteCount()
	{ return 8; }
	
	@Override
	public String toString()
	{ return Long.toString(data); }
}