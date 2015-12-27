package latmod.core.nbt;

import latmod.lib.ByteIOStream;

public class NBTShort extends NBTBase
{
	public short data;
	
	public NBTShort(short p)
	{ super(NBTID.SHORT); data = p; }
	
	public void read(ByteIOStream dios)
	{ data = dios.readShort(); }
	
	public void write(ByteIOStream dios)
	{ dios.writeShort(data); }
	
	public int getByteCount()
	{ return 2; }
	
	public String toString()
	{ return Short.toString(data); }
}