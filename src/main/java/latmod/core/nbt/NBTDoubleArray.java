package latmod.core.nbt;

import latmod.lib.*;

public class NBTDoubleArray extends NBTBase
{
	public double[] data = null;
	
	public NBTDoubleArray(double[] p)
	{
		super(NBTID.DOUBLE_ARRAY);
		data = p;
	}
	
	public void read(ByteIOStream dios)
	{
		int s = ARRAY_BYTE_COUNT.read(dios);
		if(s == -1)
		{
			data = null;
			return;
		}
		data = new double[s];
		for(int i = 0; i < s; i++)
			data[i] = dios.readDouble();
	}
	
	public void write(ByteIOStream dios)
	{
		ARRAY_BYTE_COUNT.write(dios, (data == null) ? -1 : data.length);
		if(data != null) for(int i = 0; i < data.length; i++)
			dios.writeDouble(data[i]);
	}
	
	public int getByteCount()
	{ return ARRAY_BYTE_COUNT.bytes + ((data == null) ? 0 : data.length * 8); }
	
	public String toString()
	{ return "[ " + LMStringUtils.stripD(data) + " ]"; }
}