package latmod.core.nbt;

import latmod.lib.ByteIOStream;

public class NBTByte extends NBTBase
{
	public byte data;
	
	public NBTByte(byte p)
	{ super(NBTID.BYTE); data = p; }

	public void read(ByteIOStream dios)
	{ data = (byte)dios.readByte(); }

	public void write(ByteIOStream dios)
	{ dios.writeByte(data); }

	public int getByteCount()
	{ return 1; }
	
	public String toString()
	{ return Byte.toString(data); }
}