package latmod.core.nbt;

import latmod.core.util.DataIOStream;

public class NBTInt extends NBTNumber
{
	public int data;
	
	public NBTInt(int p)
	{ super(NBTID.INT); data = p; }

	public void read(DataIOStream dios) throws Exception
	{ data = dios.readInt(); }

	public void write(DataIOStream dios) throws Exception
	{ dios.writeInt(data); }
	
	public int getByteCount()
	{ return 4; }
	
	public Number getNumber()
	{ return Integer.valueOf(data); }
}