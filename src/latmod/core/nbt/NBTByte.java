package latmod.core.nbt;

import latmod.core.util.DataIOStream;

public class NBTByte extends NBTNumber
{
	public byte data;
	
	public NBTByte(byte p)
	{ super(NBTID.BYTE); data = p; }

	public void read(DataIOStream dios) throws Exception
	{ data = (byte)dios.readByte(); }

	public void write(DataIOStream dios) throws Exception
	{ dios.writeByte(data); }

	public int getByteCount()
	{ return 1; }
	
	public Number getNumber()
	{ return Byte.valueOf(data); }
}