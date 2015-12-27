package latmod.core.nbt;

import latmod.lib.ByteIOStream;

public class NBTFloat extends NBTBase
{
	public float data;
	
	public NBTFloat(float p)
	{ super(NBTID.FLOAT); data = p; }

	public void read(ByteIOStream dios)
	{ data = dios.readFloat(); }

	public void write(ByteIOStream dios)
	{ dios.writeFloat(data); }
	
	public int getByteCount()
	{ return 4; }
	
	public String toString()
	{ return Float.toString(data); }
}