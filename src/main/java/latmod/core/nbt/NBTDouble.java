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
	
	@Override
	public void read(ByteIOStream dios)
	{ data = dios.readDouble(); }
	
	@Override
	public void write(ByteIOStream dios)
	{ dios.writeDouble(data); }
	
	@Override
	public int getByteCount()
	{ return 8; }
	
	@Override
	public String toString()
	{ return Double.toString(data); }
}