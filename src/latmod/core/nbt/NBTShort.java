package latmod.core.nbt;

import latmod.core.util.DataIOStream;

public class NBTShort extends NBTNumber
{
	public short data;
	
	public NBTShort(short p)
	{ super(NBTID.SHORT); data = p; }

	public void read(DataIOStream dios) throws Exception
	{ data = dios.readShort(); }

	public void write(DataIOStream dios) throws Exception
	{ dios.writeShort(data); }
	
	public int getByteCount()
	{ return 2; }
	
	public Number getNumber()
	{ return Short.valueOf(data); }
}