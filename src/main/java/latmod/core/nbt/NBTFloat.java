package latmod.core.nbt;

import latmod.lib.ByteIOStream;

public class NBTFloat extends NBTBase
{
	public float data;
	
	public NBTFloat(float p)
	{
		super(NBTID.FLOAT);
		data = p;
	}
	
	@Override
	public void read(ByteIOStream dios)
	{ data = dios.readFloat(); }
	
	@Override
	public void write(ByteIOStream dios)
	{ dios.writeFloat(data); }
	
	@Override
	public int getByteCount()
	{ return 4; }
	
	@Override
	public String toString()
	{ return Float.toString(data); }
}