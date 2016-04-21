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
	
	public void read(ByteIOStream dios)
	{ data = dios.readInt(); }
	
	public void write(ByteIOStream dios)
	{ dios.writeInt(data); }
	
	public int getByteCount()
	{ return 4; }
	
	public String toString()
	{ return Integer.toString(data); }
}