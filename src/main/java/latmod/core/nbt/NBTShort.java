package latmod.core.nbt;

import latmod.lib.ByteIOStream;

public class NBTShort extends NBTBase
{
	public short data;
	
	public NBTShort(short p)
	{
		super(NBTID.SHORT);
		data = p;
	}
	
	@Override
	public void read(ByteIOStream dios)
	{ data = dios.readShort(); }
	
	@Override
	public void write(ByteIOStream dios)
	{ dios.writeShort(data); }
	
	@Override
	public int getByteCount()
	{ return 2; }
	
	@Override
	public String toString()
	{ return Short.toString(data); }
}