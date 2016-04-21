package latmod.core.nbt;

import latmod.lib.ByteIOStream;

public class NBTDouble extends NBTBase
{
	public double data;
	
	public NBTDouble(double p)
	{
		super(NBTID.DOUBLE);
		data = p;
	}
	
	public void read(ByteIOStream dios)
	{ data = dios.readDouble(); }
	
	public void write(ByteIOStream dios)
	{ dios.writeDouble(data); }
	
	public int getByteCount()
	{ return 8; }
	
	public String toString()
	{ return Double.toString(data); }
}