package latmod.core.nbt;

import latmod.core.util.DataIOStream;

public class NBTDouble extends NBTNumber
{
	public double data;
	
	public NBTDouble(double p)
	{ super(NBTID.DOUBLE); data = p; }

	public void read(DataIOStream dios) throws Exception
	{ data = dios.readDouble(); }

	public void write(DataIOStream dios) throws Exception
	{ dios.writeDouble(data); }
	
	public int getByteCount()
	{ return 8; }
	
	public Number getNumber()
	{ return Double.valueOf(data); }
}