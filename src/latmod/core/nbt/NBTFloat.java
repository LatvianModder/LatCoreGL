package latmod.core.nbt;

import latmod.core.util.DataIOStream;

public class NBTFloat extends NBTNumber
{
	public float data;
	
	public NBTFloat(float p)
	{ super(NBTID.FLOAT); data = p; }

	public void read(DataIOStream dios) throws Exception
	{ data = dios.readFloat(); }

	public void write(DataIOStream dios) throws Exception
	{ dios.writeFloat(data); }
	
	public int getByteCount()
	{ return 4; }
	
	public Number getNumber()
	{ return Float.valueOf(data); }
}