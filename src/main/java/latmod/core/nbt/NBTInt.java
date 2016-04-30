package latmod.core.nbt;

import latmod.lib.ByteIOStream;

public class NBTInt extends NBTBase
{
	public int data;
	
	public NBTInt(int p)
	{
		super(NBTID.INT);
		data = p;
	}
	
	@Override
	public void read(ByteIOStream dios)
	{ data = dios.readInt(); }
	
	@Override
	public void write(ByteIOStream dios)
	{ dios.writeInt(data); }
	
	@Override
	public int getByteCount()
	{ return 4; }
	
	@Override
	public String toString()
	{ return Integer.toString(data); }
}