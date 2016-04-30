package latmod.core.nbt;

import latmod.lib.ByteIOStream;

public class NBTByte extends NBTBase
{
	public byte data;
	
	public NBTByte(byte p)
	{
		super(NBTID.BYTE);
		data = p;
	}
	
	@Override
	public void read(ByteIOStream dios)
	{ data = dios.readByte(); }
	
	@Override
	public void write(ByteIOStream dios)
	{ dios.writeByte(data); }
	
	@Override
	public int getByteCount()
	{ return 1; }
	
	@Override
	public String toString()
	{ return Byte.toString(data); }
}