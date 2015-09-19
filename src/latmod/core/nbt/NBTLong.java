package latmod.core.nbt;

import latmod.core.util.DataIOStream;

public class NBTLong extends NBTNumber
{
	public long data;
	
	public NBTLong(long p)
	{ super(NBTID.LONG); data = p; }

	public void read(DataIOStream dios) throws Exception
	{ data = dios.readLong(); }

	public void write(DataIOStream dios) throws Exception
	{ dios.writeLong(data); }
	
	public int getByteCount()
	{ return 8; }
	
	public Number getNumber()
	{ return Long.valueOf(data); }
}